package com.ahmedadeltito.expensetracker.presentation.navigation

import androidx.navigation.NavController
import com.ahmedadeltito.expensetracker.core.navigation.Coordinator
import com.ahmedadeltito.expensetracker.core.navigation.GoBack
import com.ahmedadeltito.expensetracker.core.navigation.NavigationCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

// A more complete implementation of the Coordinator interface we defined earlier.
// For now, we might directly use NavController in NavHost for simplicity and
// pass lambdas to routes. This class can be evolved.

class AppNavigator(
    private val navController: NavController,
    private val coroutineScope: CoroutineScope // Needed if commands come via a Flow
) : Coordinator {

    // If ViewModels were to emit NavigationCommand to a flow for the Coordinator to collect:
    val navigationCommands = MutableSharedFlow<NavigationCommand>(extraBufferCapacity = 1)

    init {
        // Example of collecting commands if using the flow approach
        coroutineScope.launch {
            navigationCommands.collect { command ->
                handleNavigationCommand(command)
            }
        }
    }

    private fun handleNavigationCommand(command: NavigationCommand) {
        when (command) {
            is AppDestination -> navController.navigate(command.destination)
             is GoBack -> navigateBack()
            // Handle other generic commands from core.navigation
            else -> navController.navigate(command.destination)
        }
    }

    // Direct navigation methods from the Coordinator interface
    override fun navigate(route: String, navOptions: androidx.navigation.NavOptions?, navigatorExtras: androidx.navigation.Navigator.Extras?) {
        navController.navigate(route, navOptions, navigatorExtras)
    }

    override fun navigateBack() {
        navController.popBackStack()
    }

    // Specific navigation actions
    fun navigateToAddExpense() {
        navController.navigate(AppDestination.AddExpense.destination)
    }
}