package com.ahmedadeltito.expensetracker.core.navigation

/**
 * Interface for a Coordinator, responsible for handling navigation logic.
 */
interface Coordinator {
    fun navigate(command: NavigationCommand)
    fun navigateBack()
}