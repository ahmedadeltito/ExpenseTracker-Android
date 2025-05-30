package com.ahmedadeltito.expensetracker.presentation.features.expensedetail

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahmedadeltito.expensetracker.di.AppContainer
import com.ahmedadeltito.expensetracker.presentation.navigation.AppDestination
import com.ahmedadeltito.expensetracker.presentation.navigation.AppNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun ExpenseDetailRoute(
    appNavigator: AppNavigator,
    snackbarHostState: SnackbarHostState
) {
    val getExpenseByIdUseCase = AppContainer.getExpenseByIdUseCase
    val deleteExpenseUseCase = AppContainer.deleteExpenseUseCase

    val factory = ExpenseDetailViewModelFactory(getExpenseByIdUseCase, deleteExpenseUseCase)
    val viewModel: ExpenseDetailViewModel = viewModel(factory = factory)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    // State for controlling the delete confirmation dialog
    var showDeleteDialog by remember { mutableStateOf(false) }
    var expenseToDeleteInfo by remember { mutableStateOf<Pair<String, String>?>(null) }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is ExpenseDetailContract.Effect.NavigateToEditExpense -> {
                    appNavigator.navigate(command = AppDestination.EditExpense(expenseId = effect.expenseId))
                }

                is ExpenseDetailContract.Effect.ShowDeleteConfirmation -> {
                    expenseToDeleteInfo = Pair(effect.expenseId, effect.expenseDetails)
                    showDeleteDialog = true
                }

                is ExpenseDetailContract.Effect.ExpenseDeletedSuccessfully -> {
                    showDeleteDialog = false
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = effect.message,
                            duration = SnackbarDuration.Long
                        )
                    }
                    appNavigator.navigateBack()
                }

                ExpenseDetailContract.Effect.NavigateBack -> {
                    appNavigator.navigateBack()
                }
            }
        }
    }

    if (showDeleteDialog && expenseToDeleteInfo != null) {
        val (id, details) = expenseToDeleteInfo!! // Safe due to check
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this expense?\nDetails: $details") },
            properties = DialogProperties(dismissOnClickOutside = false),
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(ExpenseDetailContract.Event.OnConfirmDelete(id)) // Send confirm event
                    },
                    enabled = !uiState.isDeleting
                ) {
                    if (uiState.isDeleting) {
                        Text("Deleting...")
                    } else {
                        Text("Delete")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false },
                    enabled = !uiState.isDeleting
                ) { Text("Cancel") }
            }
        )
    }

    ExpenseDetailScreen(
        state = uiState,
        onEvent = viewModel::onEvent
    )
}