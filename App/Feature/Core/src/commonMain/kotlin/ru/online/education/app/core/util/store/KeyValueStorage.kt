package ru.online.education.app.core.util.store

import kotlinx.coroutines.flow.Flow

interface KeyValueStorage<K, V> {
    suspend fun store(key: K, value: V)
    suspend fun read(key: K): V?
    suspend fun remove(key: K)
    fun readAsFlow(key: K): Flow<V?>
}