package domain

import domain.mapper.toAttachment
import domain.model.Attachment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import repository.AttachmentRepository
import ru.online.education.app.core.util.coruotines.DispatcherProvider

class AttachmentListState(
    private val attachmentRepository: AttachmentRepository,
    private val publicationIdFlow: Flow<Int>,
    private val coroutineScope: CoroutineScope
) {
    private val _attachments = MutableStateFlow(listOf<AttachmentCardState>())
    val attachments = _attachments.asStateFlow()

    private var publicationId = 0

    init {
        syncPublicationId()
    }

    fun loadAttachments() {
        coroutineScope.launch(DispatcherProvider.IO) {
            fetchAttachments()
        }
    }

    fun syncPublicationId() {
        coroutineScope.launch {
            publicationIdFlow.collect {
                publicationId = it
                loadAttachments()
            }
        }
//        loadAttachments()
    }

    private suspend fun fetchAttachments() {
        val loadedAttachments = attachmentRepository
            .getAttachments(publicationId)

        val mappedAttachments = loadedAttachments
            .successOrNull()
            ?.values
            ?.map { attachmentDto ->
                AttachmentCardState(
                    attachmentRepository = attachmentRepository,
                    attachmentState = AttachmentState.Loaded(attachmentDto.toAttachment())
                )
            }
        _attachments.emit(mappedAttachments ?: listOf())
    }

    fun deleteAttachment(attachmentId: Int) {
        coroutineScope.launch(DispatcherProvider.IO) {
            attachmentRepository
                .deleteById(attachmentId)

            fetchAttachments()
        }
    }

    fun addAttachment(attachment: Attachment) {
        val state = AttachmentCardState(
            attachmentRepository = attachmentRepository,
            attachmentState = AttachmentState.Loading(
                progress = 0f,
                attachment = attachment
            )
        )
        coroutineScope.launch(DispatcherProvider.IO) {


            when (attachment) {
                is Attachment.Link -> {
                    state.addLink(publicationId)
                }

                else -> Unit
//                is Attachment.File -> {
//                    state.uploadFile(publicationId)
//                }
//                is Attachment.Image -> TODO()
            }
            _attachments.update {
                it + state
            }
//            fetchAttachments()
        }
    }

    fun uploadFile(
        file: Attachment.File,
        fileBytes: suspend () -> ByteArray
    ) {
        val state = AttachmentCardState(
            attachmentRepository = attachmentRepository,
            attachmentState = AttachmentState.Loading(
                progress = 0f,
                attachment = file
            )
        )
        coroutineScope.launch(DispatcherProvider.IO) {
            launch {
                state.uploadFile(
                    byteArray = fileBytes,
                    publicationId = publicationId
                )
            }
            launch {
                _attachments.update {
                    it + state
                }
            }
        }
    }

    fun removeAttachment(attachmentCardState: AttachmentCardState) {
        coroutineScope.launch(DispatcherProvider.IO) {
            attachmentRepository.deleteById(attachmentCardState.attachment.value.attachment.id)
        }

        _attachments.update {
            it - attachmentCardState
        }
    }
}