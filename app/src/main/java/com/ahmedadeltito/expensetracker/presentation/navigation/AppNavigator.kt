package com.ahmedadeltito.expensetracker.presentation.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.ahmedadeltito.expensetracker.core.navigation.Coordinator
import com.ahmedadeltito.expensetracker.core.navigation.NavigationCommand

class AppNavigator(val navController: NavHostController) : Coordinator {

    override fun navigate(
        route: String,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        navController.navigate(route, navOptions, navigatorExtras)
    }

    override fun navigate(command: NavigationCommand) {
        when (command) {
            is AppDestination -> navController.navigate(
                command.destination,
                command.navOptions,
                command.navigatorExtras
            )
        }
    }

    override fun navigateBack() {
        navController.popBackStack()
    }
}