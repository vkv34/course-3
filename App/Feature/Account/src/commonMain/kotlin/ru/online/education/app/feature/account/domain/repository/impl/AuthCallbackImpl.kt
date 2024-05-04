package ru.online.education.app.feature.account.domain.repository

import io.github.aakira.napier.Napier
import ru.online.education.domain.repository.model.AuthResponse
import ru.online.education.app.feature.account.domain.model.UserAuthData

class AuthCallbackImpl(
    private val authstore: UserAuthStore
) : AuthCallback {
    override suspend fun onAuth(authResponse: AuthResponse) {
        authstore.store(
            UserAuthData(
                token = authResponse.token
            )
        )
        Napier.d { authResponse.token }
    }

    override suspend fun onLogOut() {
        authstore.logOut()
    }
}