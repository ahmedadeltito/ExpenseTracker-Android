package com.ahmedadeltito.expensetracker.presentation.features.expensedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.ahmedadeltito.expensetracker.domain.usecase.DeleteExpenseUseCase
import com.ahmedadeltito.expensetracker.domain.usecase.GetExpenseByIdUseCase

class ExpenseDetailViewModelFactory(
    private val getExpenseByIdUseCase: GetExpenseByIdUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val savedStateHandle = extras.createSavedStateHandle()

        if (modelClass.isAssignableFrom(ExpenseDetailViewModel::class.java)) {
            return ExpenseDetailViewModel(
                savedStateHandle = savedStateHandle,
                getExpenseByIdUseCase = getExpenseByIdUseCase,
                deleteExpenseUseCase = deleteExpenseUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}