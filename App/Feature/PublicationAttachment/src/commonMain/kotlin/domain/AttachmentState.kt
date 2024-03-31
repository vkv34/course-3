package domain

import domain.model.Attachment

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