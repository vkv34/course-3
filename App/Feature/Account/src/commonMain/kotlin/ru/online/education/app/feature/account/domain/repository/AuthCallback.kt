package ru.online.education.app.feature.account.domain.repository

import model.AuthResponse

interface AuthCallback {
    suspend fun onAuth(authResponse: AuthResponse)
    suspend fun onUnAuth()
}