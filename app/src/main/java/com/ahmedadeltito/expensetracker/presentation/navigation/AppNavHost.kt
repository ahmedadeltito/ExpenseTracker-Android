package com.ahmedadeltito.expensetracker.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ahmedadeltito.expensetracker.presentation.features.addexpense.AddExpenseRoute
import com.ahmedadeltito.expensetracker.presentation.features.expensedetail.ExpenseDetailRoute
import com.ahmedadeltito.expensetracker.presentation.features.expenselist.ExpenseListRoute

@Composable
fun AppNavHost(
    appNavigator: AppNavigator,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    startDestination: String
) {
    NavHost(
        navController = appNavigator.navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(AppDestination.ExpenseList.destination) {
            ExpenseListRoute(
                appNavigator = appNavigator,
                snackbarHostState = snackbarHostState
            )
        }
        composable(AppDestination.AddExpense.destination) {
            AddExpenseRoute(
                appNavigator = appNavigator,
                snackbarHostState = snackbarHostState
            )
        }
        composable(
            route = AppDestination.ExpenseDetail.ROUTE_WITH_ARGS,
            arguments = AppDestination.ExpenseDetail.arguments
        ) {
            ExpenseDetailRoute(
                appNavigator = appNavigator,
                snackbarHostState = snackbarHostState
            )
        }
    }
}