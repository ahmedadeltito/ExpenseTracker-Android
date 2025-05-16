package com.ahmedadeltito.expensetracker.core.navigation

import androidx.navigation.NavOptions
import androidx.navigation.Navigator

/**
 * Interface for a Coordinator, responsible for handling navigation logic.
 */
interface Coordinator {
    /**
     * Flow of navigation commands. ViewModels or other components can emit to this
     * to request navigation, but the Coordinator decides how to handle it.
     *
     * For a more direct approach, the Coordinator could expose methods like:
     * fun navigate(command: NavigationCommand)
     * fun navigateBack()
     *
     * Using a Flow allows for a more decoupled way for ViewModels to request navigation
     * without directly holding a Coordinator instance, though in practice, often the ViewModel
     * will trigger a side effect, and the Composable observing it will call the coordinator.
     */

    fun navigate(
        route: String,
        navOptions: NavOptions? = null,
        navigatorExtras: Navigator.Extras? = null
    )

    fun navigate(command: NavigationCommand) {
        if (command is GoBack) {
            navigateBack()
        } else {
            navigate(command.destination, command.navOptions, command.navigatorExtras)
        }
    }

    fun navigateBack()
}