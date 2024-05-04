package ru.online.education.domain.repository.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class PublicationAnswerAttachmentDto(
    val id: Int = 0,
    val publicationAnswerId: Int? = null,
    val name: String = "",
    val content: String = "",
    val contentType: PublicationAttachmentType,
    val dateCreate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
): BaseModel()
