package com.ahmedadeltito.expensetracker.presentation.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ahmedadeltito.expensetracker.core.navigation.NavigationCommand

/**
 * Defines the navigation destinations in the app.
 */
sealed class AppDestination(override val destination: String) : NavigationCommand {
    data object ExpenseList : AppDestination(destination = EXPENSE_LIST_DESTINATION)
    data object AddExpense : AppDestination(destination = ADD_EXPENSE_DESTINATION)
    data class EditExpense(
        val expenseId: String
    ) : AppDestination(destination = "$ADD_EXPENSE_DESTINATION/$expenseId") {
        companion object {
            const val EXPENSE_ID_ARG = "expenseId"
            const val ROUTE_WITH_ARGS = "$ADD_EXPENSE_DESTINATION/{$EXPENSE_ID_ARG}"
            val arguments = listOf(
                navArgument(EXPENSE_ID_ARG) { type = NavType.StringType }
            )
        }
    }
    data class ExpenseDetail(
        val expenseId: String
    ) : AppDestination(destination = "$EXPENSE_DETAIL_DESTINATION/$expenseId") {
        companion object {
            const val EXPENSE_ID_ARG = "expenseId"
            const val ROUTE_WITH_ARGS = "$EXPENSE_DETAIL_DESTINATION/{$EXPENSE_ID_ARG}"
            val arguments = listOf(
                navArgument(EXPENSE_ID_ARG) { type = NavType.StringType }
            )
        }
    }

    private companion object {
        const val EXPENSE_LIST_DESTINATION = "expense_list"
        const val ADD_EXPENSE_DESTINATION = "add_expense"
        const val EXPENSE_DETAIL_DESTINATION = "expense_detail"
    }
}