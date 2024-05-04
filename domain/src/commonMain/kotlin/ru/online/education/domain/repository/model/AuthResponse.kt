package ru.online.education.domain.repository.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String
): BaseModel()