package com.ahmedadeltito.expensetracker.presentation.features.expensedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ahmedadeltito.expensetracker.core.BaseViewModel
import com.ahmedadeltito.expensetracker.domain.model.Expense
import com.ahmedadeltito.expensetracker.domain.usecase.DeleteExpenseUseCase
import com.ahmedadeltito.expensetracker.domain.usecase.GetExpenseByIdUseCase
import com.ahmedadeltito.expensetracker.presentation.navigation.AppDestination
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Locale

class ExpenseDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getExpenseByIdUseCase: GetExpenseByIdUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase
) : BaseViewModel<ExpenseDetailContract.State, ExpenseDetailContract.Event, ExpenseDetailContract.Effect>() {

    private val expenseId: String = savedStateHandle.get<String>(AppDestination.ExpenseDetail.EXPENSE_ID_ARG)
            ?: throw IllegalStateException("Expense ID not found in SavedStateHandle. Did you pass it during navigation?")

    private val dateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())

    init {
        if (expenseId.isNotBlank()) {
            loadExpenseDetails()
        } else {
            setState { copy(isLoading = false, error = "Invalid Expense ID provided.") }
        }
    }

    override fun createInitialState(): ExpenseDetailContract.State {
        return ExpenseDetailContract.State(isLoading = true)
    }

    override fun handleEvent(event: ExpenseDetailContract.Event) {
        when (event) {
            ExpenseDetailContract.Event.OnRetryLoad -> {
                loadExpenseDetails()
            }

            ExpenseDetailContract.Event.OnEditClicked -> {
                uiState.value.expense?.id?.let { id ->
                    triggerSideEffect(ExpenseDetailContract.Effect.NavigateToEditExpense(id))
                }
            }

            ExpenseDetailContract.Event.OnDeleteClicked -> {
                uiState.value.expense?.let { expense ->
                    val detailForDialog = "${expense.category} - ${expense.amount}"
                    triggerSideEffect(
                        ExpenseDetailContract.Effect.ShowDeleteConfirmation(
                            expense.id,
                            detailForDialog
                        )
                    )
                }
            }

            is ExpenseDetailContract.Event.OnConfirmDelete -> {
                performDelete(event.expenseId)
            }

            ExpenseDetailContract.Event.OnNavigateBack -> {
                triggerSideEffect(ExpenseDetailContract.Effect.NavigateBack)
            }
        }
    }

    private fun loadExpenseDetails() {
        getExpenseByIdUseCase(expenseId)
            .onStart { setState { copy(isLoading = true, isDeleting = false, error = null) } }
            .onEach { result ->
                result.fold(
                    onSuccess = { domainExpense ->
                        if (domainExpense != null) {
                            setState {
                                copy(isLoading = false, expense = domainExpense.toDetailUiModel(), error = null)
                            }
                        } else {
                            setState {
                                copy(isLoading = false, error = "Expense not found.", expense = null)
                            }
                        }
                    },
                    onFailure = { exception ->
                        setState {
                            copy(isLoading = false, error = "Failed to load expense: ${exception.message}", expense = null)
                        }
                    }
                )
            }
            .catch { exception ->
                setState {
                    copy(isLoading = false, error = "An unexpected error occurred: ${exception.message}", expense = null)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun performDelete(idToDelete: String) {
        setState { copy(error = null, isDeleting = true) }
        viewModelScope.launch {
            val result = deleteExpenseUseCase(idToDelete)
            result.fold(
                onSuccess = { deletedExpense ->
                    val deletedExpenseUiModel = deletedExpense.toDetailUiModel()
                    setState { copy(isDeleting = false) }
                    triggerSideEffect(
                        ExpenseDetailContract.Effect.ExpenseDeletedSuccessfully(
                            "Expense ${deletedExpenseUiModel.amount} - ${deletedExpenseUiModel.description ?: deletedExpenseUiModel.category}"
                        )
                    )
                },
                onFailure = { exception ->
                    setState { copy(isDeleting = false, error = "Failed to delete expense: ${exception.message}") }
                }
            )
        }
    }

    private fun Expense.toDetailUiModel(): ExpenseDetailUiModel = with(this) {
        try {
            currencyFormat.currency = Currency.getInstance(this.currencyCode.uppercase())
        } catch (e: Exception) {
            println("Warning: Invalid currency code '${this.currencyCode}' in ExpenseDetailViewModel. Using default for formatting.")
        }
        ExpenseDetailUiModel(
            id = id,
            amount = currencyFormat.format(amount),
            category = category,
            description = description,
            date = dateFormat.format(date),
            currencyCode = currencyCode
        )
    }
}