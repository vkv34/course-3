package model

import ru.online.education.app.feature.account.presentation.model.AuthState

data class AppState(
    val authState: AuthState = AuthState.Undefined
)