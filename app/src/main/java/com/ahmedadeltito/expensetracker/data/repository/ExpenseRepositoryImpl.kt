package com.ahmedadeltito.expensetracker.data.repository

import com.ahmedadeltito.expensetracker.data.datasource.CacheDataSource
import com.ahmedadeltito.expensetracker.data.mapper.toCacheModel
import com.ahmedadeltito.expensetracker.data.mapper.toDomain
import com.ahmedadeltito.expensetracker.data.model.ExpenseCacheModel
import com.ahmedadeltito.expensetracker.domain.model.Expense
import com.ahmedadeltito.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ExpenseRepositoryImpl(
    private val localDataSource: CacheDataSource<String, ExpenseCacheModel>, // Injected
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExpenseRepository {

    override suspend fun addExpense(expense: Expense): Result<String> = withContext(ioDispatcher) {
        val cacheModel = expense.toCacheModel()
        localDataSource.add(cacheModel)
    }

    override suspend fun getExpenseById(expenseId: String): Result<Expense?> =
        withContext(ioDispatcher) {
            val result = localDataSource.getById(expenseId)
            result.map { cacheModel -> cacheModel?.toDomain() }
        }

    override fun getAllExpenses(): Flow<Result<List<Expense>>> {
        return localDataSource.getAll().map { result ->
            result.map { cacheModels ->
                cacheModels.toDomain()
            }
        }
    }

    override suspend fun updateExpense(expense: Expense): Result<Expense> =
        withContext(ioDispatcher) {
            val cacheModel = expense.toCacheModel()
            val updatedCacheModel = cacheModel.copy(
                lastUpdatedTimestamp = System.currentTimeMillis()
            )
            localDataSource.update(updatedCacheModel).map { it.toDomain() }
        }

    override suspend fun deleteExpense(expenseId: String): Result<Expense> =
        withContext(ioDispatcher) {
            localDataSource.deleteById(expenseId).map { it.toDomain() }
        }

    override suspend fun clearAllExpenses(): Result<Unit> = withContext(ioDispatcher) {
        localDataSource.clearAll()
    }
}