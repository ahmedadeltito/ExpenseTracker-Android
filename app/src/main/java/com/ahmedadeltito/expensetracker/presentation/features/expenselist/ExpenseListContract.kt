package com.ahmedadeltito.expensetracker.presentation.features.expenselist

import androidx.compose.runtime.Immutable
import com.ahmedadeltito.expensetracker.core.UiEvent
import com.ahmedadeltito.expensetracker.core.UiSideEffect
import com.ahmedadeltito.expensetracker.core.UiState

// This will represent a single item in the list for UI purposes.
// It can include formatted strings or any UI-specific transformations.
@Immutable
data class ExpenseListItemUiModel(
    val id: String,
    val amount: String,
    val category: String,
    val date: String,
    val description: String?
)

object ExpenseListContract {

    @Immutable
    data class State(
        val expenses: List<ExpenseListItemUiModel> = emptyList(),
        val isLoading: Boolean = true,
        val error: String? = null,
        val isListEmpty: Boolean = false
    ) : UiState

    sealed class Event : UiEvent {
        data object OnAddExpenseClicked : Event()
        data class OnExpenseClicked(val expenseId: String) : Event()
        data object OnRetry : Event()
        data object OnRefresh : Event()
    }

    sealed class Effect : UiSideEffect {
        data object NavigateToAddExpense : Effect()
        data class NavigateToExpenseDetail(val expenseId: String) : Effect()
        data class ShowErrorMessage(val message: String) : Effect()
    }
}