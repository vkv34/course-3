package ru.online.education.app.feature.publication.domain

import kotlinx.coroutines.flow.MutableStateFlow
import ru.online.education.app.feature.publicationAttachment.domain.AttachmentCardState
import ru.online.education.app.feature.publicationAttachment.domain.AttachmentState

class PublicationAnswerState(
    private val publicationOnCourseId: Int
) {
    val attachmentStates = MutableStateFlow(listOf<AttachmentCardState>())
}