package com.ahmedadeltito.expensetracker.presentation.features.addexpense

import androidx.lifecycle.viewModelScope
import com.ahmedadeltito.expensetracker.core.BaseViewModel
import com.ahmedadeltito.expensetracker.domain.usecase.AddExpenseUseCase
import kotlinx.coroutines.launch
import java.util.Date

class AddExpenseViewModel(
    private val addExpenseUseCase: AddExpenseUseCase
) : BaseViewModel<AddExpenseContract.State, AddExpenseContract.Event, AddExpenseContract.Effect>() {

    override fun createInitialState(): AddExpenseContract.State {
        return AddExpenseContract.State(date = Date())
    }

    override fun handleEvent(event: AddExpenseContract.Event) {
        when (event) {
            is AddExpenseContract.Event.OnAmountChange -> {
                setState { copy(amount = event.amount, amountError = null, error = null) }
            }

            is AddExpenseContract.Event.OnCategoryChange -> {
                setState { copy(category = event.category, categoryError = null, error = null) }
            }

            is AddExpenseContract.Event.OnDescriptionChange -> {
                setState { copy(description = event.description, error = null) }
            }

            is AddExpenseContract.Event.OnDateChange -> {
                setState { copy(date = event.date, error = null) }
            }

            is AddExpenseContract.Event.OnCurrencyChange -> {
                setState {
                    copy(
                        currencyCode = event.currency.uppercase(),
                        currencyError = null,
                        error = null
                    )
                }
            }

            AddExpenseContract.Event.OnSaveClick -> {
                saveExpense()
            }

            AddExpenseContract.Event.OnDismissError -> {
                setState {
                    copy(
                        error = null,
                        amountError = null,
                        categoryError = null,
                        currencyError = null
                    )
                }
            }
        }
    }

    /**
     * Validates the current form inputs and updates the UI state with any errors.
     * @return true if all inputs are valid, false otherwise.
     */
    private fun validateInputs(): Boolean {
        val currentState = uiState.value
        var isValid = true

        val amountDouble = currentState.amount.toDoubleOrNull()
        if (amountDouble == null || amountDouble <= 0) {
            setState { copy(amountError = "Please enter a valid amount.") }
            isValid = false
        } else {
            // Clear error if valid, only if an error was previously set for amount
            if (currentState.amountError != null) {
                setState { copy(amountError = null) }
            }
        }

        if (currentState.category.isBlank()) {
            setState { copy(categoryError = "Category cannot be empty.") }
            isValid = false
        } else {
            // Clear error if valid, only if an error was previously set for category
            if (currentState.categoryError != null) {
                setState { copy(categoryError = null) }
            }
        }

        if (currentState.currencyCode.isBlank() || currentState.currencyCode.length != 3) {
            setState { copy(currencyError = "Enter a valid 3-letter currency code.") }
            isValid = false
        } else {
            // Clear error if valid, only if an error was previously set for currency
            if (currentState.currencyError != null) {
                setState { copy(currencyError = null) }
            }
        }
        return isValid
    }

    private fun saveExpense() {
        if (!validateInputs()) {
            return // Don't proceed if client-side validation fails
        }

        val currentState = uiState.value
        val amountDouble = currentState.amount.toDouble() // Already validated by validateInputs

        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) } // Set loading state

            try {
                val result = addExpenseUseCase(
                    amount = amountDouble,
                    category = currentState.category,
                    description = currentState.description.takeIf { it.isNotBlank() },
                    date = currentState.date,
                    currencyCode = currentState.currencyCode
                )

                result.fold(
                    onSuccess = {
                        setState { copy(isLoading = false) }
                        triggerSideEffect(AddExpenseContract.Effect.ShowSnackbar("Expense saved successfully"))
                    },
                    onFailure = { exception ->
                        setState {
                            copy(
                                isLoading = false,
                                error = "Failed to save expense: ${exception.message}"
                            )
                        }
                    }
                )

            } catch (e: Exception) {
                setState {
                    copy(
                        isLoading = false,
                        error = "An unexpected error occurred: ${e.localizedMessage}"
                    )
                }
            }
        }
    }
}