package com.ahmedadeltito.expensetracker.presentation.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ahmedadeltito.expensetracker.core.navigation.NavigationCommand

/**
 * Defines the navigation destinations in the app.
 */
sealed class AppDestination(override val destination: String) : NavigationCommand {
    data object ExpenseList : AppDestination(destination = "expense_list")
    data object AddExpense : AppDestination(destination = "add_expense")
    data class EditExpense(
        val expenseId: String
    ) : AppDestination(destination = ROUTE_WITH_ARGS) {
        override val destination: String = "add_expense/$expenseId"

        companion object {
            const val EXPENSE_ID_ARG = "expenseId"
            const val ROUTE_WITH_ARGS = "add_expense/{$EXPENSE_ID_ARG}"
            val arguments = listOf(
                navArgument(EXPENSE_ID_ARG) { type = NavType.StringType }
            )
        }
    }
    data class ExpenseDetail(
        val expenseId: String
    ) : AppDestination("expense_detail/{$EXPENSE_ID_ARG}") {
        override val destination: String = "expense_detail/$expenseId"

        companion object {
            const val EXPENSE_ID_ARG = "expenseId"
            const val ROUTE_WITH_ARGS = "expense_detail/{$EXPENSE_ID_ARG}"
            val arguments = listOf(
                navArgument(EXPENSE_ID_ARG) { type = NavType.StringType }
            )
        }
    }
}