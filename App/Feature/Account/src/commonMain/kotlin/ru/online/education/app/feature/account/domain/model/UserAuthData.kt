package ru.online.education.app.feature.account.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserAuthData (
    val token: String
)