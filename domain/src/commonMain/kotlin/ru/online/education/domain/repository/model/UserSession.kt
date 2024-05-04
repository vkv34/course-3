package ru.online.education.domain.repository.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val id: String = "",
    val userId: Int = 0,
    val authDate: LocalDateTime = LocalDateTime(2000, 1, 1, 1, 1),
    val lastOnline: LocalDateTime = LocalDateTime(2000, 1, 1, 1, 1),
    val state: SessionState = SessionState.Ended,
    val host: String = ""
) : BaseModel()
