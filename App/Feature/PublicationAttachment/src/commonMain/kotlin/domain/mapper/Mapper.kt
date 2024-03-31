package domain.mapper

import domain.model.Attachment
import model.PublicationAttachmentDto
import model.PublicationAttachmentType

fun PublicationAttachmentDto.toAttachment(): Attachment = when (this.contentType) {
    PublicationAttachmentType.File -> Attachment.File(
        id = attachmentId,
        name = name,
        url = content
    )

    PublicationAttachmentType.Image -> Attachment.Image(
        id = attachmentId,
        name = name,
        url = content
    )

    PublicationAttachmentType.Link -> Attachment.Link(
        id = attachmentId,
        name = name,
        url = content
    )

    PublicationAttachmentType.Text -> TODO()
    PublicationAttachmentType.Html -> TODO()
    PublicationAttachmentType.MarkDown -> TODO()
}

fun Attachment.toPublicationAttachmentDto(): PublicationAttachmentDto = when (this) {
    is Attachment.File -> PublicationAttachmentDto(
        attachmentId = id,
        name = name,
        content = url,
        contentType = PublicationAttachmentType.File
    )

    is Attachment.Image -> PublicationAttachmentDto(
        attachmentId = id,
        name = name,
        content = url,
        contentType = PublicationAttachmentType.Image
    )

    is Attachment.Link -> PublicationAttachmentDto(
        attachmentId = id,
        name = name,
        content = url,
        contentType = PublicationAttachmentType.Link
    )
}