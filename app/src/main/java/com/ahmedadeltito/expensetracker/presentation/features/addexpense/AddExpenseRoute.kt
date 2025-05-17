package com.ahmedadeltito.expensetracker.presentation.features.addexpense

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahmedadeltito.expensetracker.di.AppContainer

// Assuming a simplified navigator for now. We will integrate a full Coordinator later.
// The Coordinator would typically be provided via CompositionLocal or passed down.
typealias ShowSnackbar = suspend (String, String?) -> Unit // message, actionLabel

@Composable
fun AddExpenseRoute(
    snackbarHostState: SnackbarHostState // For showing snackbars from the screen level
    // coordinator: AppCoordinator // Later, we'd pass the coordinator
) {
    // Obtain AddExpenseUseCase from the AppContainer
    val addExpenseUseCase = AppContainer.addExpenseUseCase

    // Create the ViewModel using its factory
    val viewModel: AddExpenseViewModel = viewModel(
        factory = AddExpenseViewModelFactory(addExpenseUseCase)
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Effect for showing snackbar from this level (route)
    val showSnackbar: ShowSnackbar = { message, actionLabel ->
        snackbarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel
        )
    }

    // Handle side effects from the ViewModel
    LaunchedEffect(Unit) { // Use a key that changes if you need to re-launch for specific reasons
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is AddExpenseContract.Effect.ShowSnackbar -> {
                    showSnackbar(effect.message, "Dismiss")
                }
            }
        }
    }

    AddExpenseScreen(
        state = uiState,
        onEvent = viewModel::onEvent
    )
}