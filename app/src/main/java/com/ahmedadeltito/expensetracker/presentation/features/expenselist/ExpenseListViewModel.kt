package com.ahmedadeltito.expensetracker.presentation.features.expenselist

import androidx.lifecycle.viewModelScope
import com.ahmedadeltito.expensetracker.core.BaseViewModel
import com.ahmedadeltito.expensetracker.domain.model.Expense
import com.ahmedadeltito.expensetracker.domain.usecase.GetAllExpensesUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Locale

class ExpenseListViewModel(
    private val getAllExpensesUseCase: GetAllExpensesUseCase
) : BaseViewModel<ExpenseListContract.State, ExpenseListContract.Event, ExpenseListContract.Effect>() {

    // SimpleDateFormats are not thread-safe, consider alternatives or careful management if used across threads.
    // For ViewModel use, typically on main or default dispatcher, it's often fine.
    // It's better to get Locale/Currency from system or user preferences.
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())

    init {
        loadExpenses(isInitialLoad = true)
    }

    override fun createInitialState(): ExpenseListContract.State {
        return ExpenseListContract.State(isLoading = true)
    }

    override fun handleEvent(event: ExpenseListContract.Event) {
        when (event) {
            ExpenseListContract.Event.OnAddExpenseClicked -> {
                triggerSideEffect(ExpenseListContract.Effect.NavigateToAddExpense)
            }

            is ExpenseListContract.Event.OnExpenseClicked -> {
                triggerSideEffect(ExpenseListContract.Effect.NavigateToExpenseDetail(event.expenseId))
            }

            ExpenseListContract.Event.OnRetry -> {
                loadExpenses()
            }

            ExpenseListContract.Event.OnRefresh -> {
                loadExpenses(isRefreshing = true)
            }
        }
    }

    private fun loadExpenses(isInitialLoad: Boolean = false, isRefreshing: Boolean = false) {
        viewModelScope.launch {
            getAllExpensesUseCase()
                .onStart {
                    setState { copy(isLoading = isInitialLoad, error = null) }
                }
                .catch { exception ->
                    setState {
                        copy(
                            isLoading = false,
                            error = "An unexpected error occurred: ${exception.message}",
                            expenses = if (isRefreshing) this.expenses else emptyList(),
                            isListEmpty = if (isRefreshing) this.expenses.isEmpty() else true
                        )
                    }
                }
                .collectLatest { expenseList ->
                    expenseList.fold(
                        onSuccess = { domainExpenses ->
                            val expenseListUiModel = domainExpenses.map { it.toUiModel() }
                            setState {
                                copy(
                                    expenses = expenseListUiModel,
                                    isLoading = false,
                                    error = null,
                                    isListEmpty = expenseListUiModel.isEmpty()
                                )
                            }
                        },
                        onFailure = { exception ->
                            setState {
                                copy(
                                    expenses = if (isRefreshing) this.expenses else emptyList(),
                                    isLoading = false,
                                    error = "Failed to load expenses: ${exception.message}",
                                    isListEmpty = if (isRefreshing) this.expenses.isEmpty() else true
                                )
                            }
                        }
                    )
                }
        }
    }

    private fun Expense.toUiModel(): ExpenseListItemUiModel = with(this) {
        try {
            currencyFormat.currency = Currency.getInstance(this.currencyCode)
        } catch (e: IllegalArgumentException) {
            println("Warning: Invalid currency code ${this.currencyCode}. Using default for formatting.")
        }
        ExpenseListItemUiModel(
            id = id,
            amount = currencyFormat.format(amount),
            category = category,
            date = dateFormat.format(date),
            description = description
        )
    }
}