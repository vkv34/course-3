package model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class PublicationAttachmentDto(
    val publicationId: Int = 0,
    val attachmentId: Int = 0,
    val name: String = "",
    val content: String = "",
    val contentType: PublicationAttachmentType,
    val dateCreate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
):BaseModel()
