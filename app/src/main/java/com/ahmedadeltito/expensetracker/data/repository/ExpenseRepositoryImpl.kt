package com.ahmedadeltito.expensetracker.data.repository

import com.ahmedadeltito.expensetracker.data.datasource.CacheDataSource
import com.ahmedadeltito.expensetracker.data.mapper.toCacheModel
import com.ahmedadeltito.expensetracker.data.mapper.toDomain
import com.ahmedadeltito.expensetracker.data.model.ExpenseCacheModel
import com.ahmedadeltito.expensetracker.domain.model.Expense
import com.ahmedadeltito.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

class ExpenseRepositoryImpl(
    private val localDataSource: CacheDataSource<String, ExpenseCacheModel>, // Injected
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExpenseRepository {

    override suspend fun addExpense(expense: Expense): Result<String> = withContext(ioDispatcher) {
        delay(timeMillis = 2000)
        val cacheModel = expense.toCacheModel()
        localDataSource.add(cacheModel)
    }

    override fun getExpenseById(expenseId: String): Flow<Result<Expense?>> =
        localDataSource.getAll().map { result ->
            result.map { cacheModels ->
                cacheModels.find { it.id == expenseId }?.toDomain()
            }
        }.onStart { delay(timeMillis = 2000) }

    override fun getAllExpenses(): Flow<Result<List<Expense>>> = localDataSource.getAll().map { result ->
        result.map { cacheModels ->
            cacheModels.toDomain()
        }
    }.onStart { delay(timeMillis = 2000) }

    override suspend fun updateExpense(expense: Expense): Result<Expense> = withContext(ioDispatcher) {
        delay(timeMillis = 2000)
        val cacheModel = expense.toCacheModel()
        val updatedCacheModel = cacheModel.copy(
            lastUpdatedTimestamp = System.currentTimeMillis()
        )
        localDataSource.update(updatedCacheModel).map { it.toDomain() }
    }

    override suspend fun deleteExpense(expenseId: String): Result<Expense> = withContext(ioDispatcher) {
        delay(timeMillis = 2000)
        localDataSource.deleteById(expenseId).map { it.toDomain() }
    }

    override suspend fun clearAllExpenses(): Result<Unit> = withContext(ioDispatcher) {
        delay(timeMillis = 2000)
        localDataSource.clearAll()
    }
}