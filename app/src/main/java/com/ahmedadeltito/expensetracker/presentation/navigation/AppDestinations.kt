package com.ahmedadeltito.expensetracker.presentation.navigation

import com.ahmedadeltito.expensetracker.core.navigation.NavigationCommand

/**
 * Defines the navigation destinations in the app.
 */
sealed class AppDestination(override val destination: String) : NavigationCommand {
    data object AddExpense : AppDestination(destination = "add_expense")
}