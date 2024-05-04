package ru.online.education.app.feature.publicationAttachment.domain

import ru.online.education.app.feature.publicationAttachment.domain.model.Attachment

sealed class AttachmentState {

    abstract val attachment: Attachment

    data class Loading(
        val progress: Float = 0f,
        override val attachment: Attachment
    ) : AttachmentState()

    data class Loaded(
        override val attachment: Attachment
    ) : AttachmentState()

    data class Error(
        val message: String,
        override val attachment: Attachment
    ) : AttachmentState()

}