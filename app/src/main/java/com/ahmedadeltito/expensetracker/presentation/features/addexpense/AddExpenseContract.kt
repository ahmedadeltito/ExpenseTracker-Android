package com.ahmedadeltito.expensetracker.presentation.features.addexpense

import androidx.compose.runtime.Immutable
import com.ahmedadeltito.expensetracker.core.UiEvent
import com.ahmedadeltito.expensetracker.core.UiSideEffect
import com.ahmedadeltito.expensetracker.core.UiState
import java.util.Date

object AddExpenseContract {

    /**
     * Represents the state of the Add Expense screen.
     *
     * @param amount Current value of the amount input field.
     * @param category Current value of the category input field.
     * @param description Current value of the description input field.
     * @param date Selected date for the expense.
     * @param currencyCode Current currency code (e.g., "USD").
     * @param isLoading True if an expense is being saved, false otherwise.
     * @param error Generic error message for the screen.
     * @param amountError Error message specific to the amount field.
     * @param categoryError Error message specific to the category field.
     */
    @Immutable
    data class State(
        val expenseId: String? = null,
        val isEditMode: Boolean = false,
        val screenTitle: String = "Add Expense",
        val isLoadingExpense: Boolean = false,

        val amount: String = "",
        val category: String = "",
        val description: String = "",
        val date: Date = Date(),
        val currencyCode: String = "USD", // Default currency
        val isLoading: Boolean = false,
        val error: String? = null,
        val amountError: String? = null,
        val categoryError: String? = null,
        val currencyError: String? = null // Added currencyError
    ) : UiState

    /**
     * Events that can be triggered from the Add Expense UI.
     */
    sealed class Event : UiEvent {
        data class OnAmountChange(val amount: String) : Event()
        data class OnCategoryChange(val category: String) : Event()
        data class OnDescriptionChange(val description: String) : Event()
        data class OnDateChange(val date: Date) : Event()
        data class OnCurrencyChange(val currency: String) : Event()
        data object OnSaveClick : Event()
        data object OnBackClick : Event()
        data object OnDismissError : Event()
    }

    /**
     * Side effects that the ViewModel can trigger for the Add Expense UI.
     */
    sealed class Effect : UiSideEffect {
        data object NavigateToBackScreen : Effect()
        object ExpenseUpdatedSuccessfully : Effect()
        data object ExpenseSavedSuccessfully : Effect()
    }
}