package ru.online.education.app.feature.publicationAttachment.domain.mapper

import ru.online.education.app.feature.publicationAttachment.domain.model.Attachment
import ru.online.education.domain.repository.model.PublicationAnswerAttachmentDto
import ru.online.education.domain.repository.model.PublicationAttachmentDto
import ru.online.education.domain.repository.model.PublicationAttachmentType

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
fun PublicationAnswerAttachmentDto.toAttachment(): Attachment = when (this.contentType) {
    PublicationAttachmentType.File -> Attachment.File(
        id = id,
        name = name,
        url = content
    )

    PublicationAttachmentType.Image -> Attachment.Image(
        id = id,
        name = name,
        url = content
    )

    PublicationAttachmentType.Link -> Attachment.Link(
        id = id,
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
}fun Attachment.toAnswerAttachmentDto(): PublicationAnswerAttachmentDto = when (this) {
    is Attachment.File -> PublicationAnswerAttachmentDto(
        id = id,
        name = name,
        content = url,
        contentType = PublicationAttachmentType.File
    )

    is Attachment.Image -> PublicationAnswerAttachmentDto(
        id = id,
        name = name,
        content = url,
        contentType = PublicationAttachmentType.Image
    )

    is Attachment.Link -> PublicationAnswerAttachmentDto(
        id = id,
        name = name,
        content = url,
        contentType = PublicationAttachmentType.Link
    )
}