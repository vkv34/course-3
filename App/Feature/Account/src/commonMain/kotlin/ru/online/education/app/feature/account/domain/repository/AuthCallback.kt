package ru.online.education.app.feature.account.domain.repository

import model.AuthRequest
import model.AuthResponse

interface AuthCallback {
    suspend fun onAuth(authResponse: AuthResponse)
    suspend fun onLogOut()
}

expect fun onAuth(authRequest: AuthRequest)

expect fun onAuthScreenOpened()