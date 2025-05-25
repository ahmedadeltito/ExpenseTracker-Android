package com.ahmedadeltito.expensetracker.presentation.features.addexpense

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ahmedadeltito.expensetracker.core.BaseViewModel
import com.ahmedadeltito.expensetracker.domain.model.Expense
import com.ahmedadeltito.expensetracker.domain.usecase.AddExpenseUseCase
import com.ahmedadeltito.expensetracker.domain.usecase.GetExpenseByIdUseCase
import com.ahmedadeltito.expensetracker.domain.usecase.UpdateExpenseUseCase
import com.ahmedadeltito.expensetracker.presentation.navigation.AppDestination
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class AddExpenseViewModel(
    savedStateHandle: SavedStateHandle,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val getExpenseByIdUseCase: GetExpenseByIdUseCase,
    private val updateExpenseUseCase: UpdateExpenseUseCase
) : BaseViewModel<AddExpenseContract.State, AddExpenseContract.Event, AddExpenseContract.Effect>() {

    private val expenseId: String? = savedStateHandle.get<String>(AppDestination.EditExpense.EXPENSE_ID_ARG)

    init {
        if (expenseId != null) {
            setState { copy(isEditMode = true, screenTitle = "Edit Expense", expenseId = this@AddExpenseViewModel.expenseId) }
            loadExpenseForEditing(expenseId)
        } else {
            setState { copy(isEditMode = false, screenTitle = "Add Expense") }
        }
    }

    override fun createInitialState(): AddExpenseContract.State {
        return AddExpenseContract.State(date = Date())
    }

    override fun handleEvent(event: AddExpenseContract.Event) {
        when (event) {
            is AddExpenseContract.Event.OnAmountChange -> setState {
                copy(
                    amount = event.amount,
                    amountError = null,
                    error = null
                )
            }
            is AddExpenseContract.Event.OnCategoryChange -> setState {
                copy(
                    category = event.category,
                    categoryError = null,
                    error = null
                )
            }
            is AddExpenseContract.Event.OnDescriptionChange -> setState {
                copy(
                    description = event.description,
                    error = null
                )
            }
            is AddExpenseContract.Event.OnDateChange -> setState {
                copy(
                    date = event.date,
                    error = null
                )
            }
            is AddExpenseContract.Event.OnCurrencyChange -> setState {
                copy(
                    currencyCode = event.currency.uppercase(),
                    currencyError = null,
                    error = null
                )
            }
            AddExpenseContract.Event.OnSaveClick -> saveOrUpdateExpense()
            AddExpenseContract.Event.OnBackClick -> triggerSideEffect(
                effect = AddExpenseContract.Effect.NavigateToBackScreen
            )
            AddExpenseContract.Event.OnDismissError -> setState {
                copy(
                    error = null,
                    amountError = null,
                    categoryError = null,
                    currencyError = null
                )
            }
        }
    }

    private fun loadExpenseForEditing(id: String) {
        getExpenseByIdUseCase(id).
            onStart { setState { copy(isLoadingExpense = true) } }
            .onEach { result ->
                result.fold(
                    onSuccess = { expense ->
                        if (expense != null) {
                            setState {
                                copy(
                                    isLoadingExpense = false,
                                    amount = expense.amount.toString(),
                                    category = expense.category,
                                    description = expense.description ?: "",
                                    date = expense.date,
                                    currencyCode = expense.currencyCode
                                )
                            }
                        } else {
                            setState {
                                copy(
                                    isLoadingExpense = false,
                                    error = "Failed to load expense details."
                                )
                            }
                        }
                    },
                    onFailure = {
                        setState {
                            copy(
                                isLoadingExpense = false,
                                error = "Failed to load expense details."
                            )
                        }
                    }
                )
            }
            .catch { exception ->
                setState {
                    copy(isLoadingExpense = false, error = "An unexpected error occurred: ${exception.message}")
                }
            }
            .launchIn(scope = viewModelScope)

    }

    private fun saveOrUpdateExpense() {
        if (!validateInputs()) {
            return
        }
        val currentState = uiState.value

        if (currentState.isEditMode && currentState.expenseId?.isNotBlank() == true) {
            val expenseToUpdate = Expense(
                id = currentState.expenseId,
                amount = currentState.amount.toDouble(),
                category = currentState.category,
                description = currentState.description.takeIf { it.isNotBlank() },
                date = currentState.date,
                currencyCode = currentState.currencyCode,
                creationTimestamp = System.currentTimeMillis()
            )
            performUpdate(expenseToUpdate)
        } else {
            val expenseToAdd = Expense(
                id = UUID.randomUUID().toString(),
                amount = currentState.amount.toDouble(),
                category = currentState.category,
                description = currentState.description.takeIf { it.isNotBlank() },
                date = currentState.date,
                currencyCode = currentState.currencyCode,
                creationTimestamp = System.currentTimeMillis()
            )
            performAdd(expenseToAdd)
        }
    }

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

    private fun performAdd(expense: Expense) {
        setState { copy(isLoading = true, error = null) }
        viewModelScope.launch {
            addExpenseUseCase(expense).fold(
                onSuccess = {
                    setState { copy(isLoading = false) }
                    triggerSideEffect(AddExpenseContract.Effect.ExpenseSavedSuccessfully)
                },
                onFailure = {
                    setState { copy(isLoading = false, error = "Failed to save expense.") }
                }
            )
        }
    }

    private fun performUpdate(expense: Expense) {
        setState { copy(isLoading = true, error = null) }
        viewModelScope.launch {
            updateExpenseUseCase(expense).fold(
                onSuccess = {
                    setState { copy(isLoading = false) }
                    triggerSideEffect(AddExpenseContract.Effect.ExpenseUpdatedSuccessfully)
                },
                onFailure = {
                    setState { copy(isLoading = false, error = "Failed to update expense.") }
                }
            )
        }
    }
}