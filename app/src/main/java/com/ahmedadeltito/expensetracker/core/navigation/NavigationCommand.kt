package com.ahmedadeltito.expensetracker.core.navigation

import androidx.navigation.NavOptions
import androidx.navigation.Navigator

/**
 * Represents a command to navigate to a specific destination.
 */
interface NavigationCommand {
    val destination: String
    val navOptions: NavOptions? get() = null
    val navigatorExtras: Navigator.Extras? get() = null // For shared element transitions, etc.
}

/**
 * Command to navigate back.
 */
object GoBack : NavigationCommand {
    override val destination: String = "" // Not used for back navigation
}