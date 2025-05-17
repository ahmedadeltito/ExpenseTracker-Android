package com.ahmedadeltito.expensetracker.presentation.features.expenselist

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahmedadeltito.expensetracker.di.AppContainer
import com.ahmedadeltito.expensetracker.presentation.navigation.AppDestination
import com.ahmedadeltito.expensetracker.presentation.navigation.AppNavigator

typealias ShowSnackbar = suspend (String, String?) -> Unit

@Composable
fun ExpenseListRoute(
    appNavigator: AppNavigator,
    snackbarHostState: SnackbarHostState
) {
    val getAllExpensesUseCase = AppContainer.getAllExpensesUseCase
    val viewModel: ExpenseListViewModel = viewModel(
        factory = ExpenseListViewModelFactory(getAllExpensesUseCase)
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val showSnackbar: ShowSnackbar = { message, actionLabel ->
        snackbarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel
        )
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is ExpenseListContract.Effect.NavigateToAddExpense -> {
                    appNavigator.navigate(command = AppDestination.AddExpense)
                }

                is ExpenseListContract.Effect.NavigateToExpenseDetail -> {

                }

                is ExpenseListContract.Effect.ShowErrorMessage -> {
                    showSnackbar(effect.message, "Dismiss")
                }
            }
        }
    }

    ExpenseListScreen(
        state = uiState,
        onEvent = viewModel::onEvent
    )
}