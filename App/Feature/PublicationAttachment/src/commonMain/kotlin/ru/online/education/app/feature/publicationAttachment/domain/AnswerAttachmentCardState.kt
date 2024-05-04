package ru.online.education.app.feature.publicationAttachment.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.online.education.app.feature.publicationAttachment.domain.mapper.toAnswerAttachmentDto
import ru.online.education.app.feature.publicationAttachment.domain.mapper.toAttachment
import ru.online.education.domain.repository.AnswerAttachmentRepository
import ru.online.education.domain.repository.model.PublicationAttachmentType
import util.ApiResult

class AnswerAttachmentCardState(
    private val repository: AnswerAttachmentRepository,
    attachmentState: AttachmentState
) {
    private val _attachment = MutableStateFlow(attachmentState)
    val attachment = _attachment.asStateFlow()

    suspend fun uploadFile(
        byteArray: suspend () -> ByteArray,
        answerId: Int? = null,
        fileType: PublicationAttachmentType = PublicationAttachmentType.File
    ) {
        val result = repository.uploadFile(
            file = byteArray(),
            fileName = attachment.value.attachment.name,
            answerId = answerId,
            progressChanged = { sentBytes, totalBytes ->
                _attachment.update {
                    AttachmentState.Loading(
                        progress = sentBytes.toFloat() / totalBytes.toFloat(),
                        attachment = it.attachment
                    )
                }
            },
            fileType = fileType
        )

        when (result) {
            is ApiResult.Success -> {
                _attachment.update {
                    AttachmentState.Loaded(
                        attachment = result.data.toAttachment()
                    )
                }
            }

            else -> {
                _attachment.update {
                    AttachmentState.Error(
                        attachment = it.attachment,
                        message = result.message
                    )
                }
            }
        }
    }

    suspend fun addLink() {
        _attachment.update {
            AttachmentState.Loading(
                attachment = it.attachment
            )
        }
        val result = repository.add(
            attachment
                .value
                .attachment
                .toAnswerAttachmentDto(),
        )

        when (result) {
            is ApiResult.Success -> _attachment.update {
                AttachmentState.Loaded(
                    attachment = result.data.toAttachment()
                )
            }

            else -> _attachment.update {
                AttachmentState.Error(
                    attachment = it.attachment,
                    message = result.message
                )
            }
        }
    }

    suspend fun send(answerId: Int) {
        repository.sendFile(attachment.value.attachment.id, answerId)
    }
}