package ru.online.education.app.feature.account.presentation.model

sealed class AuthState(
    open val login: String = "",
    open val displayName: String = "",
) {

    data object Undefined : AuthState()
    data object LoggedOut : AuthState(
        login = "Авторизуйтесь",
        displayName = "Необходимо войти в акаунт"
    )

    class LoggedIn(
        override val login: String,
        override val displayName: String
    ) : AuthState()
}