package com.ahmedadeltito.expensetracker.data.datasource

import kotlinx.coroutines.flow.Flow

/**
 * A generic interface for a cache data source.
 *
 * @param K The type of the key used for identifying items.
 * @param V The type of the value (item) stored in the cache.
 */
interface CacheDataSource<K, V> {
    suspend fun add(item: V): Result<K>
    suspend fun addAll(items: List<V>): Result<Unit>
    suspend fun getById(id: K): Result<V?>
    fun getAll(): Flow<Result<List<V>>>
    suspend fun update(item: V): Result<V>
    suspend fun deleteById(id: K): Result<V>
    suspend fun clearAll(): Result<Unit>
}