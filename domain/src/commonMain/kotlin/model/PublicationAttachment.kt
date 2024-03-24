package model

import kotlinx.serialization.Serializable

@Serializable
data class PublicationAttachment(
    val publicationId: Int = 0,
    val attachmentId: Int = 0,
    val fileName: String = "",
    val content: String = "",
    val contentType: PublicationAttachmentType
):BaseModel()
