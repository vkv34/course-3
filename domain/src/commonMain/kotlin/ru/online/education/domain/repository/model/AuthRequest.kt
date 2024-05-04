package ru.online.education.domain.repository.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val email: String,
    val password: String,
    val hostName: String = "",
//    val ipAddress: String
): BaseModel()
