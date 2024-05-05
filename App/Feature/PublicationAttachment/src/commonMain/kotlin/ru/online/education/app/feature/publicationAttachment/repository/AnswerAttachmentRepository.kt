package ru.online.education.app.feature.publicationAttachment.repository

import api.defaultJson
import domain.NotificationManager
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import ru.online.education.app.core.util.ktorUtil.safeDelete
import ru.online.education.app.core.util.ktorUtil.safeGet
import ru.online.education.app.core.util.ktorUtil.safePostAsJson
import ru.online.education.domain.repository.AnswerAttachmentRepository
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.PublicationAnswerAttachmentDto
import ru.online.education.domain.repository.model.PublicationAttachmentType
import util.ApiResult

class AnswerAttachmentRepositoryImpl(
    private val httpClient: HttpClient,
    private val notificationManager: NotificationManager
) : AnswerAttachmentRepository {
    override suspend fun uploadFile(
        file: ByteArray,
        fileName: String,
        answerId: Int?,
        progressChanged: (sentBytes: Long, totalBytes: Long) -> Unit,
        fileType: PublicationAttachmentType
    ): ApiResult<PublicationAnswerAttachmentDto> {
        val attachmentDto = PublicationAnswerAttachmentDto(
            publicationAnswerId = answerId,
            name = fileName,
            contentType = PublicationAttachmentType.File,
        )

        val response = httpClient.submitFormWithBinaryData("/publicationAnswer/file", formData = formData {
            append(
                key = "file",
                value = file,
                headers = Headers.build {
                    append(HttpHeaders.ContentType, "application/octet-stream")
                    append(HttpHeaders.ContentDisposition, "filename=${fileName}$")
                },

                )
            append("json", defaultJson.encodeToString(PublicationAnswerAttachmentDto.serializer(), attachmentDto))
        }

        ) {
            onUpload { bytesSentTotal, contentLength -> progressChanged(bytesSentTotal, contentLength) }
        }

        return response.body()
    }

    override suspend fun getAttachments(
        answerId: Int
    ): ApiResult<ListResponse<PublicationAnswerAttachmentDto>> =
        httpClient.safeGet(
            path = "publicationAnswer/file/all/$answerId",
        )

    override suspend fun sendFile(
        publicationAttachmentId: Int,
        answerId: Int
    ): ApiResult<PublicationAnswerAttachmentDto> =
        httpClient.safePostAsJson(
            path = "publicationAnswer/file/send" +
                    "?publicationAttachmentId=$publicationAttachmentId" +
                    "&answerId=$answerId",
            body = PublicationAnswerAttachmentDto(contentType = PublicationAttachmentType.File),
            notificationManager = notificationManager
        )

    override suspend fun getAll(page: Int): ApiResult<ListResponse<PublicationAnswerAttachmentDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): ApiResult<PublicationAnswerAttachmentDto> =
        httpClient.safeGet(
            path = "/publicationAnswer/attachment/$id",
            notificationManager = notificationManager
        )

    override suspend fun deleteById(id: Int): ApiResult<PublicationAnswerAttachmentDto> =
        httpClient.safeDelete(
            path = "/publicationAnswer/file/$id",
            notificationManager = notificationManager
        )

    override suspend fun update(data: PublicationAnswerAttachmentDto): ApiResult<PublicationAnswerAttachmentDto?> {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: PublicationAnswerAttachmentDto): ApiResult<PublicationAnswerAttachmentDto> =
        httpClient.safePostAsJson(
            path = "/publicationAnswer/attachment",
            body = data,
            notificationManager = notificationManager
        )
}
