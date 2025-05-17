package com.ahmedadeltito.expensetracker.presentation.features.addexpense

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ahmedadeltito.expensetracker.ui.theme.ExpenseTrackerTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    state: AddExpenseContract.State,
    onEvent: (AddExpenseContract.Event) -> Unit,
    // onNavigateBack: () -> Unit // Could be passed for a back button if not using scaffold top bar
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Date Picker Dialog state
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = state.date // Sync calendar with current state date

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            val newDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDayOfMonth)
            }.time
            onEvent(AddExpenseContract.Event.OnDateChange(newDate))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Formatter for displaying the date
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Expense") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Make the column scrollable
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.error != null) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clickable { onEvent(AddExpenseContract.Event.OnDismissError) }
                )
            }

            OutlinedTextField(
                value = state.amount,
                onValueChange = { onEvent(AddExpenseContract.Event.OnAmountChange(it)) },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = state.amountError != null,
                supportingText = { if (state.amountError != null) Text(state.amountError) }
            )

            OutlinedTextField(
                value = state.category,
                onValueChange = { onEvent(AddExpenseContract.Event.OnCategoryChange(it)) },
                label = { Text("Category") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = state.categoryError != null,
                supportingText = { if (state.categoryError != null) Text(state.categoryError) }
            )

            OutlinedTextField(
                value = state.description,
                onValueChange = { onEvent(AddExpenseContract.Event.OnDescriptionChange(it)) },
                label = { Text("Description (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            OutlinedTextField(
                value = dateFormatter.format(state.date),
                onValueChange = { /* Read-only, changed by dialog */ },
                label = { Text("Date") },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = "Select Date",
                        modifier = Modifier.clickable { datePickerDialog.show() }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.currencyCode,
                onValueChange = { onEvent(AddExpenseContract.Event.OnCurrencyChange(it)) },
                label = { Text("Currency Code (e.g., USD)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                modifier = Modifier.fillMaxWidth(),
                isError = state.currencyError != null,
                supportingText = { if (state.currencyError != null) Text(state.currencyError) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onEvent(AddExpenseContract.Event.OnSaveClick) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Save Expense")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddExpenseScreenPreview() {
    ExpenseTrackerTheme {
        AddExpenseScreen(
            state = AddExpenseContract.State(
                amount = "10.99",
                category = "Food",
                description = "Lunch with colleagues",
                currencyCode = "EUR",
                isLoading = false,
                error = null,
                amountError = "Amount cannot be zero",
                categoryError = null,
                currencyError = "Invalid currency"
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddExpenseScreenLoadingPreview() {
    ExpenseTrackerTheme {
        AddExpenseScreen(
            state = AddExpenseContract.State(isLoading = true),
            onEvent = {}
        )
    }
}