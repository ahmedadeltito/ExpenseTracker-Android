package com.ahmedadeltito.expensetracker.presentation.features.expensedetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ahmedadeltito.expensetracker.ui.theme.ExpenseTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetailScreen(
    state: ExpenseDetailContract.State,
    onEvent: (ExpenseDetailContract.Event) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Details") },
                navigationIcon = {
                    IconButton(onClick = { onEvent(ExpenseDetailContract.Event.OnNavigateBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (state.expense != null && !state.isLoading) { // Show actions only if expense is loaded
                        IconButton(onClick = { onEvent(ExpenseDetailContract.Event.OnEditClicked) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit Expense")
                        }
                        IconButton(onClick = { onEvent(ExpenseDetailContract.Event.OnDeleteClicked) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete Expense")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.error != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.error,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Button(onClick = { onEvent(ExpenseDetailContract.Event.OnRetryLoad) }) {
                            Text("Retry")
                        }
                    }
                }

                state.expense != null -> {
                    ExpenseDetailsContent(expense = state.expense)
                }

                else -> { // Should not happen if error or loading is not set, but as a fallback
                    Text(
                        "No expense data available.",
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpenseDetailsContent(expense: ExpenseDetailUiModel) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DetailRow(label = "Amount:", value = expense.amount, isEmphasized = true)
        DetailRow(label = "Category:", value = expense.category)
        DetailRow(label = "Date:", value = expense.date)
        expense.description?.takeIf { it.isNotBlank() }?.let {
            DetailRow(label = "Description:", value = it)
        }
        DetailRow(label = "Currency Code:", value = expense.currencyCode)
        // Add more fields if present in ExpenseDetailUiModel
    }
}

@Composable
private fun DetailRow(label: String, value: String, isEmphasized: Boolean = false) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = if (isEmphasized) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.bodyLarge,
            fontWeight = if (isEmphasized) FontWeight.Bold else FontWeight.Normal
        )
    }
}

// Previews
@Preview(showBackground = true, name = "Expense Detail Content")
@Composable
fun ExpenseDetailScreenContentPreview() {
    ExpenseTrackerTheme {
        ExpenseDetailScreen(
            state = ExpenseDetailContract.State(
                expense = ExpenseDetailUiModel(
                    id = "1",
                    amount = "€123.45",
                    category = "Groceries",
                    description = "Weekly shopping at the supermarket, including fruits, vegetables, and dairy products.",
                    date = "15 May 2025, 10:30",
                    currencyCode = "EUR"
                ),
                isLoading = false,
                error = null
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Expense Detail Loading")
@Composable
fun ExpenseDetailScreenLoadingPreview() {
    ExpenseTrackerTheme {
        ExpenseDetailScreen(
            state = ExpenseDetailContract.State(isLoading = true),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Expense Detail Error")
@Composable
fun ExpenseDetailScreenErrorPreview() {
    ExpenseTrackerTheme {
        ExpenseDetailScreen(
            state = ExpenseDetailContract.State(
                error = "Failed to load expense details. Please try again later.",
                isLoading = false
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Expense Detail Not Found (Error variant)")
@Composable
fun ExpenseDetailScreenNotFoundPreview() {
    ExpenseTrackerTheme {
        ExpenseDetailScreen(
            state = ExpenseDetailContract.State(error = "Expense not found.", isLoading = false),
            onEvent = {}
        )
    }
}