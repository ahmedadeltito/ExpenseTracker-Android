package com.ahmedadeltito.expensetracker.presentation.features.expenselist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmedadeltito.expensetracker.domain.usecase.GetAllExpensesUseCase

class ExpenseListViewModelFactory(
    private val getAllExpensesUseCase: GetAllExpensesUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseListViewModel::class.java)) {
            return ExpenseListViewModel(getAllExpensesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}