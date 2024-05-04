package ru.online.education.app.feature.account.domain.repository

import ru.online.education.domain.repository.model.AuthRequest
import ru.online.education.domain.repository.model.AuthResponse

interface AuthCallback {
    suspend fun onAuth(authResponse: AuthResponse)
    suspend fun onLogOut()
}

expect fun onAuth(authRequest: AuthRequest)

expect fun onAuthScreenOpened()