package com.ahmedadeltito.expensetracker.presentation.features.addexpense

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahmedadeltito.expensetracker.di.AppContainer
import com.ahmedadeltito.expensetracker.presentation.navigation.AppNavigator

@Composable
fun AddExpenseRoute(
    appNavigator: AppNavigator, // Use AppNavigator
    snackbarHostState: SnackbarHostState
) {
    val addExpenseUseCase = AppContainer.addExpenseUseCase
    val viewModel: AddExpenseViewModel = viewModel(
        factory = AddExpenseViewModelFactory(addExpenseUseCase)
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is AddExpenseContract.Effect.ExpenseSavedSuccessfully -> {
                    appNavigator.navigateBack()
                }

                AddExpenseContract.Effect.NavigateToBackScreen -> {
                    appNavigator.navigateBack()
                }
            }
        }
    }

    AddExpenseScreen(
        state = uiState,
        onEvent = viewModel::onEvent
    )
}