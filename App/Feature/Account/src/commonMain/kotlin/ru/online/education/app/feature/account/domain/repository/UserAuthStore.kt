package ru.online.education.app.feature.account.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.online.education.app.core.util.store.KeyValueStorage
import ru.online.education.app.feature.account.domain.model.UserAuthData

interface UserAuthStore {
    suspend fun store(userAuthData: UserAuthData)
    suspend fun logOut()
    suspend fun read(): UserAuthData?
    fun readAsFlow(): Flow<UserAuthData?>
}