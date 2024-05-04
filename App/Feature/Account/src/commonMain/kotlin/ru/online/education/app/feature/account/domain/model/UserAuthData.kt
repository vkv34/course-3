package ru.online.education.app.feature.account.domain.model

import kotlinx.serialization.Serializable
import ru.online.education.domain.repository.model.UserRole

@Serializable
data class UserAuthData (
    val token: String,
//    val role: UserRole
)