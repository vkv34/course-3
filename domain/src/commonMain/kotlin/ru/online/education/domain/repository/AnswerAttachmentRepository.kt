package ru.online.education.domain.repository

import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.PublicationAttachmentDto
import ru.online.education.domain.repository.defaults.Repository
import ru.online.education.domain.repository.model.PublicationAnswerAttachmentDto
import ru.online.education.domain.repository.model.PublicationAttachmentType
import util.ApiResult

interface AnswerAttachmentRepository: Repository<PublicationAnswerAttachmentDto, Int> {

    suspend fun uploadFile(
        file: ByteArray,
        fileName: String,
        answerId: Int? = null,
        progressChanged: (sentBytes: Long, totalBytes: Long) -> Unit,
        fileType: PublicationAttachmentType = PublicationAttachmentType.File
    ): ApiResult<PublicationAnswerAttachmentDto>

    suspend fun getAttachments(answerId: Int): ApiResult<ListResponse<PublicationAnswerAttachmentDto>>

    suspend fun sendFile(
        publicationAttachmentId: Int,
        answerId: Int
    ): ApiResult<PublicationAnswerAttachmentDto>


}