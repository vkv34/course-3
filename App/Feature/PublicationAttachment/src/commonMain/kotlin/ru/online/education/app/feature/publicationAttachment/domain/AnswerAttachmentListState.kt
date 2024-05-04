package ru.online.education.app.feature.publicationAttachment.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.feature.publicationAttachment.domain.model.Attachment
import ru.online.education.domain.repository.AnswerAttachmentRepository
import ru.online.education.domain.repository.model.PublicationAttachmentType

class AnswerAttachmentListState(
    private val repository: AnswerAttachmentRepository,
//    private val answerId: Int?,
    private val coroutineScope: CoroutineScope
) {
    private val _attachments = MutableStateFlow(listOf<AnswerAttachmentCardState>())
    val attachments = _attachments.asStateFlow()

    init {
//        loadAttachments()
    }

//    fun loadAttachments() {
//        coroutineScope.launch(DispatcherProvider.IO) {
//            fetchAttachments()
//        }
//    }

//    fun syncPublicationId() {
//        coroutineScope.launch {
//            publicationIdFlow.collect {
//                publicationId = it
//                loadAttachments()
//            }
//        }
////        loadAttachments()
//    }

//    private suspend fun fetchAttachments() {
//        val loadedAttachments = repository
//            .getAttachments(publicationId)
//
//        val mappedAttachments = loadedAttachments
//            .successOrNull()
//            ?.values
//            ?.map { attachmentDto ->
//                AttachmentCardState(
//                    attachmentRepository = attachmentRepository,
//                    attachmentState = AttachmentState.Loaded(attachmentDto.toAttachment())
//                )
//            }
//        _attachments.emit(mappedAttachments ?: listOf())
//    }

    fun deleteAttachment(attachmentId: Int) {
        coroutineScope.launch(DispatcherProvider.IO) {
            repository
                .deleteById(attachmentId)

//            fetchAttachments()
        }
    }

    fun addAttachment(attachment: Attachment) {
        val state = AnswerAttachmentCardState(
            repository = repository,
            attachmentState = AttachmentState.Loading(
                progress = 0f,
                attachment = attachment
            )
        )
        coroutineScope.launch(DispatcherProvider.IO) {


            when (attachment) {
                is Attachment.Link -> {
                    state.addLink()
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
        file: Attachment,
        fileBytes: suspend () -> ByteArray,
        fileType: PublicationAttachmentType
    ) {
        val state = AnswerAttachmentCardState(
            repository = repository,
            attachmentState = AttachmentState.Loading(
                progress = 0f,
                attachment = file
            )
        )
        coroutineScope.launch(DispatcherProvider.IO) {
            launch {
                state.uploadFile(
                    byteArray = fileBytes,
                    fileType = fileType
                )
            }
            launch {
                _attachments.update {
                    it + state
                }
            }
        }
    }

    fun removeAttachment(attachmentCardState: AnswerAttachmentCardState) {
        coroutineScope.launch(DispatcherProvider.IO) {
            repository.deleteById(attachmentCardState.attachment.value.attachment.id)
        }

        _attachments.update {
            it - attachmentCardState
        }
    }

    fun sendAll(answerId: Int) {
        coroutineScope.launch(DispatcherProvider.IO) {
            val copy = attachments.value
            copy.map {
                async {
                    it.send(answerId)
                    _attachments.update { attach -> attach - it }
                }
            }.awaitAll()


        }
    }
}