package com.ahmedadeltito.expensetracker.presentation.features.addexpense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.ahmedadeltito.expensetracker.domain.usecase.AddExpenseUseCase
import com.ahmedadeltito.expensetracker.domain.usecase.GetExpenseByIdUseCase
import com.ahmedadeltito.expensetracker.domain.usecase.UpdateExpenseUseCase

class AddExpenseViewModelFactory(
    private val addExpenseUseCase: AddExpenseUseCase,
    private val getExpenseByIdUseCase: GetExpenseByIdUseCase,
    private val updateExpenseUseCase: UpdateExpenseUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val savedStateHandle = extras.createSavedStateHandle()
        if (modelClass.isAssignableFrom(AddExpenseViewModel::class.java)) {
            return AddExpenseViewModel(
                savedStateHandle = savedStateHandle,
                addExpenseUseCase = addExpenseUseCase,
                getExpenseByIdUseCase = getExpenseByIdUseCase,
                updateExpenseUseCase = updateExpenseUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}