package com.ahmedadeltito.expensetracker.presentation.features.addexpense

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahmedadeltito.expensetracker.di.AppContainer
import com.ahmedadeltito.expensetracker.presentation.navigation.AppNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddExpenseRoute(
    appNavigator: AppNavigator, // Use AppNavigator
    snackbarHostState: SnackbarHostState
) {
    val addExpenseUseCase = AppContainer.addExpenseUseCase
    val getExpenseByIdUseCase = AppContainer.getExpenseByIdUseCase
    val updateExpenseUseCase = AppContainer.updateExpenseUseCase
    val viewModel: AddExpenseViewModel = viewModel(
        factory = AddExpenseViewModelFactory(
            addExpenseUseCase = addExpenseUseCase,
            getExpenseByIdUseCase = getExpenseByIdUseCase,
            updateExpenseUseCase = updateExpenseUseCase
        )
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is AddExpenseContract.Effect.ExpenseSavedSuccessfully -> {
                    scope.launch { snackbarHostState.showSnackbar(message = "Expense saved successfully!", duration = SnackbarDuration.Long) }
                    appNavigator.navigateBack()
                }
                is AddExpenseContract.Effect.ExpenseUpdatedSuccessfully -> {
                    scope.launch { snackbarHostState.showSnackbar(message = "Expense updated successfully!", duration = SnackbarDuration.Long) }
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