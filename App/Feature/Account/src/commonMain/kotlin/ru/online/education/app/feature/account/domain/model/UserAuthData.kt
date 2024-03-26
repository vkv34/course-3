package ru.online.education.app.feature.account.domain.model

import kotlinx.serialization.Serializable
import model.UserRole

@Serializable
data class UserAuthData (
    val token: String,
//    val role: UserRole
)