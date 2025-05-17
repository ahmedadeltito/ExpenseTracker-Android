package com.ahmedadeltito.expensetracker.core.navigation

import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.ahmedadeltito.expensetracker.presentation.navigation.AppDestination

/**
 * Represents a command to navigate to a specific destination.
 */
interface NavigationCommand {
    val destination: String
    val navOptions: NavOptions? get() = null
    val navigatorExtras: Navigator.Extras? get() = null // For shared element transitions, etc.
}

/**
 * A simple implementation for navigating to a route.
 */
data class GoTo(
    override val destination: String,
    override val navOptions: NavOptions? = null,
    override val navigatorExtras: Navigator.Extras? = null
) : NavigationCommand

/**
 * Command to navigate back.
 */
object GoBack : NavigationCommand {
    override val destination: String = "" // Not used for back navigation
}

data class GoBackTo(
    override val destination: String,
    val route: String,
    val inclusive: Boolean,
) : NavigationCommand

object GoToHome : NavigationCommand {
    override val destination = AppDestination.ExpenseList.destination
}