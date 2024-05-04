package ru.online.education.app.core.util.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface KeyValueStorage<K, V> {
    suspend fun store(key: K, value: V)
    suspend fun read(key: K): V?
    suspend fun remove(key: K)
    fun readAsFlow(key: K): SharedFlow<V?>
}