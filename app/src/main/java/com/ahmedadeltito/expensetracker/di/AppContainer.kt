package com.ahmedadeltito.expensetracker.di

import android.content.Context
import com.ahmedadeltito.expensetracker.data.datasource.CacheDataSource
import com.ahmedadeltito.expensetracker.data.datasource.cache.ExpenseCacheDataSource
import com.ahmedadeltito.expensetracker.data.model.ExpenseCacheModel
import com.ahmedadeltito.expensetracker.data.repository.ExpenseRepositoryImpl
import com.ahmedadeltito.expensetracker.domain.repository.ExpenseRepository
import com.ahmedadeltito.expensetracker.domain.usecase.AddExpenseUseCase
import com.ahmedadeltito.expensetracker.domain.usecase.GetAllExpensesUseCase
import com.ahmedadeltito.expensetracker.domain.usecase.GetExpenseByIdUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object AppContainer {
    // Dispatchers
    private val ioDispatcher: CoroutineDispatcher by lazy { Dispatchers.IO }
    val mainDispatcher: CoroutineDispatcher by lazy { Dispatchers.Main }
    val defaultDispatcher: CoroutineDispatcher by lazy { Dispatchers.Default }

    private var applicationContext: Context? = null

    // DataSources
    // Singleton instance for ExpenseCacheDataSource
    private val expenseCacheDataSource: CacheDataSource<String, ExpenseCacheModel> by lazy {
        ExpenseCacheDataSource(ioDispatcher = ioDispatcher)
    }

    // Repositories
    // Singleton instance for ExpenseRepository
    private val expenseRepository: ExpenseRepository by lazy {
        ExpenseRepositoryImpl(
            localDataSource = expenseCacheDataSource,
            ioDispatcher = ioDispatcher
        )
    }

    // Use Cases
    val addExpenseUseCase: AddExpenseUseCase by lazy {
        AddExpenseUseCase(expenseRepository = expenseRepository)
    }
    val getAllExpensesUseCase: GetAllExpensesUseCase by lazy {
        GetAllExpensesUseCase(expenseRepository = expenseRepository)
    }
    val getExpenseByIdUseCase: GetExpenseByIdUseCase by lazy {
        GetExpenseByIdUseCase(expenseRepository = expenseRepository)
    }

    fun initialize(context: Context) {
        applicationContext = context.applicationContext
    }
}