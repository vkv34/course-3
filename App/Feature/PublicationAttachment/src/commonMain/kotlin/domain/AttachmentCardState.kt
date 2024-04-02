package domain

import domain.mapper.toAttachment
import domain.mapper.toPublicationAttachmentDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import repository.AttachmentRepository
import util.ApiResult

class AttachmentCardState(
    private val attachmentRepository: AttachmentRepository,
    attachmentState: AttachmentState
) {
    private val _attachment = MutableStateFlow(attachmentState)
    val attachment = _attachment.asStateFlow()

    suspend fun uploadFile(byteArray: suspend () -> ByteArray, publicationId: Int) {
        val result = attachmentRepository.uploadFile(
            file = byteArray(),
            fileName = attachment.value.attachment.name,
            publicationId = publicationId,
            progressChanged = { sentBytes, totalBytes ->
                _attachment.update {
                    AttachmentState.Loading(
                        progress = sentBytes.toFloat() / totalBytes.toFloat(),
                        attachment = it.attachment
                    )
                }
            },
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

    suspend fun addLink(publicationId: Int) {
        _attachment.update {
            AttachmentState.Loading(
                attachment = it.attachment
            )
        }
        val result = attachmentRepository.add(
            attachment
                .value
                .attachment
                .toPublicationAttachmentDto()
                .copy(publicationId = publicationId),
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
}