package com.ahmedadeltito.expensetracker.data.datasource.cache

import com.ahmedadeltito.expensetracker.data.datasource.CacheDataSource
import com.ahmedadeltito.expensetracker.data.model.ExpenseCacheModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class ExpenseCacheDataSource(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CacheDataSource<String, ExpenseCacheModel> {

    // Using LinkedHashMap to maintain insertion order, useful for "getAll" if no other sort is applied.
    private val cache = LinkedHashMap<String, ExpenseCacheModel>()
    private val cacheFlow = MutableStateFlow<List<ExpenseCacheModel>>(emptyList())
    private val mutex = Mutex()

    override suspend fun add(item: ExpenseCacheModel): Result<String> = withContext(ioDispatcher) {
        mutex.withLock {
            cache[item.id] = item // Add or overwrite if already exists
            updateFlow()
        }
        Result.success(item.id)
    }

    override suspend fun addAll(items: List<ExpenseCacheModel>): Result<Unit> =
        withContext(ioDispatcher) {
            mutex.withLock {
                items.forEach { cache[it.id] = it }
                updateFlow()
            }
            Result.success(Unit)
        }

    override suspend fun getById(id: String): Result<ExpenseCacheModel?> =
        withContext(ioDispatcher) {
            val item = mutex.withLock { cache[id] }
            Result.success(item)
        }

    override fun getAll(): Flow<Result<List<ExpenseCacheModel>>> =
        cacheFlow.asStateFlow().mapNotNull { list ->
            Result.success(list)
        }

    override suspend fun update(item: ExpenseCacheModel): Result<ExpenseCacheModel> =
        withContext(ioDispatcher) {
            mutex.withLock {
                if (cache.containsKey(item.id)) {
                    cache[item.id] = item
                    updateFlow()
                } else {
                    // Or you could choose to add it if it doesn't exist, depends on desired semantics
                    return@withContext Result.failure(NoSuchElementException("Item with id ${item.id} not found for update."))
                }
            }
            Result.success(item)
        }

    override suspend fun deleteById(id: String): Result<ExpenseCacheModel> =
        withContext(ioDispatcher) {
            val deletedExpense = mutex.withLock {
                cache.remove(id)?.let { deletedExpense ->
                    updateFlow()
                    deletedExpense
                } ?: return@withContext Result.failure(
                    NoSuchElementException("Item with id $id not found for delete.")
                )
            }
            Result.success(deletedExpense)
        }

    override suspend fun clearAll(): Result<Unit> = withContext(ioDispatcher) {
        mutex.withLock {
            cache.clear()
            updateFlow()
        }
        Result.success(Unit)
    }

    private fun updateFlow() {
        // Emit a new list to ensure StateFlow detects the change
        cacheFlow.value = cache.values.toList()
    }
}