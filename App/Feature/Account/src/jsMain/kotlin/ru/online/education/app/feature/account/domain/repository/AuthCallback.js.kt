package ru.online.education.app.feature.account.domain.repository

import kotlinx.browser.document
import model.AuthRequest
import model.AuthResponse
import ru.online.education.app.feature.account.domain.model.Auth

actual fun onAuth(authRequest: AuthRequest) {
    savePassword(with(authRequest) { Auth(email, password) })
}

actual fun onAuthScreenOpened() {
}

external val navigator: dynamic

fun savePassword(auth: Auth) {
    val credential = object {
        val password = auth.password
        val login = auth.email
    }

    navigator.credentials.store(
        credential
    )

    console.log("password saved")
}


