package com.ahmedadeltito.expensetracker.presentation.features.expenselist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmedadeltito.expensetracker.ui.theme.ExpenseTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    state: ExpenseListContract.State,
    onEvent: (ExpenseListContract.Event) -> Unit
) {
    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Expenses") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(ExpenseListContract.Event.OnAddExpenseClicked) }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Expense")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .pullToRefresh(
                    isRefreshing = state.isLoading && state.expenses.isNotEmpty(),
                    state = pullRefreshState,
                    onRefresh = { onEvent(ExpenseListContract.Event.OnRefresh) }
                )
        ) {
            when {
                // Initial loading state (list is empty and isLoading is true)
                state.isLoading && state.expenses.isEmpty() && state.error == null -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                // Error state
                state.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.error,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Button(onClick = { onEvent(ExpenseListContract.Event.OnRetry) }) {
                            Text("Retry")
                        }
                    }
                }
                // Empty list state (no error, not loading, but list is empty)
                state.isListEmpty && !state.isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "No expenses yet!",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Tap the '+' button to add your first expense.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    PullToRefreshBox(
                        isRefreshing = state.isLoading,
                        onRefresh = { onEvent(ExpenseListContract.Event.OnRefresh) },
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.expenses, key = { it.id }) { expenseItem ->
                                ExpenseListItem(
                                    item = expenseItem,
                                    onClick = {
                                        onEvent(
                                            ExpenseListContract.Event.OnExpenseClicked(
                                                expenseItem.id
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseListItem(
    item: ExpenseListItemUiModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.category,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                item.description?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(item.date, style = MaterialTheme.typography.bodySmall, fontSize = 12.sp)
            }
            Text(
                item.amount,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// Previews
@Preview(showBackground = true, name = "Expense List Content")
@Composable
fun ExpenseListScreenContentPreview() {
    ExpenseTrackerTheme {
        ExpenseListScreen(
            state = ExpenseListContract.State(
                expenses = listOf(
                    ExpenseListItemUiModel("1", "€10.50", "Food", "15 May 2025", "Lunch"),
                    ExpenseListItemUiModel("2", "€25.00", "Transport", "14 May 2025", "Taxi")
                ),
                isLoading = false,
                error = null,
                isListEmpty = false
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Expense List Loading")
@Composable
fun ExpenseListScreenLoadingPreview() {
    ExpenseTrackerTheme {
        ExpenseListScreen(
            state = ExpenseListContract.State(isLoading = true, expenses = emptyList()),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Expense List Empty")
@Composable
fun ExpenseListScreenEmptyPreview() {
    ExpenseTrackerTheme {
        ExpenseListScreen(
            state = ExpenseListContract.State(
                isListEmpty = true,
                isLoading = false,
                expenses = emptyList()
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Expense List Error")
@Composable
fun ExpenseListScreenErrorPreview() {
    ExpenseTrackerTheme {
        ExpenseListScreen(
            state = ExpenseListContract.State(
                error = "Failed to load expenses. Please try again.",
                isLoading = false,
                expenses = emptyList()
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Expense List Refreshing Content")
@Composable
fun ExpenseListScreenRefreshingContentPreview() {
    ExpenseTrackerTheme {
        ExpenseListScreen(
            state = ExpenseListContract.State(
                expenses = listOf(
                    ExpenseListItemUiModel("1", "€10.50", "Food", "15 May 2025", "Lunch")
                ),
                isLoading = true, // Simulating refresh
                error = null,
                isListEmpty = false
            ),
            onEvent = {}
        )
    }
}