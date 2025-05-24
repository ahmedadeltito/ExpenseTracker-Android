package com.ahmedadeltito.expensetracker.presentation.features.expensedetail

import androidx.compose.runtime.Immutable
import com.ahmedadeltito.expensetracker.core.UiEvent
import com.ahmedadeltito.expensetracker.core.UiSideEffect
import com.ahmedadeltito.expensetracker.core.UiState

@Immutable
data class ExpenseDetailUiModel(
    val id: String,
    val amount: String, // Formatted amount with currency
    val category: String,
    val description: String?,
    val date: String, // Formatted date
    val currencyCode: String // Raw currency code, might be useful
)

object ExpenseDetailContract {

    @Immutable
    data class State(
        val expense: ExpenseDetailUiModel? = null,
        val isLoading: Boolean = true,
        val error: String? = null
    ) : UiState

    sealed class Event : UiEvent {
        data object OnRetryLoad : Event()
        data object OnEditClicked : Event()
        data object OnDeleteClicked : Event()
        data class OnConfirmDelete(val expenseId: String) : Event()
        data object OnNavigateBack : Event()
    }

    sealed class Effect : UiSideEffect {
        data class NavigateToEditExpense(val expenseId: String) : Effect()
        data class ShowDeleteConfirmation(val expenseId: String, val expenseDetails: String) : Effect()
        data class ExpenseDeletedSuccessfully(val message: String) : Effect() // Then navigate back
        data object NavigateBack : Effect()
    }
}