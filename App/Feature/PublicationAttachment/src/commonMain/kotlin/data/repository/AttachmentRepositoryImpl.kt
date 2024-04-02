package data.repository

import api.defaultJson
import domain.NotificationManager
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import model.ListResponse
import model.PublicationAttachmentDto
import model.PublicationAttachmentType
import repository.AttachmentRepository
import ru.online.education.app.core.util.ktorUtil.safeDelete
import ru.online.education.app.core.util.ktorUtil.safeGet
import ru.online.education.app.core.util.ktorUtil.safePostAsJson
import util.ApiResult

class AttachmentRepositoryImpl(
    private val client: HttpClient,
    private val notificationManager: NotificationManager
) : AttachmentRepository {
    override suspend fun uploadFile(
        file: ByteArray,
        fileName: String,
        publicationId: Int,
        progressChanged: (sentBytes: Long, totalBytes: Long) -> Unit
    ): ApiResult<PublicationAttachmentDto> {
        val attachmentDto = PublicationAttachmentDto(
            publicationId = publicationId,
            name = fileName,
            contentType = PublicationAttachmentType.File,
        )
        /*val attachmentJsonPart = PartData.FormItem(
            defaultJson.encodeToString(PublicationAttachmentDto.serializer(), attachmentDto),
            {},
            Headers.build {
                append(HttpHeaders.ContentType, "application/json")
            }
        )
        val buffer = withContext(DispatcherProvider.IO) {
            buildPacket {
                writeFully(file)
            }
        }
        val fileFormPart = PartData.FileItem(
            provider = { buffer },
            dispose = { buffer.close() },
            partHeaders = Headers.build {
                append(HttpHeaders.ContentType, "application/octet-stream")
            }
        )*/

        val response = client.submitFormWithBinaryData("/attachment/file", formData = formData {
            append(
                key = "file",
                value = file,
                headers = Headers.build {
                    append(HttpHeaders.ContentType, "application/octet-stream")
                    append(HttpHeaders.ContentDisposition, "filename=${fileName}$")
                },

                )
            append("json", defaultJson.encodeToString(PublicationAttachmentDto.serializer(), attachmentDto))
        }

        ) {
            onUpload { bytesSentTotal, contentLength -> progressChanged(bytesSentTotal, contentLength) }
        }

        return response.body()
    }

    override suspend fun getAttachments(publicationId: Int): ApiResult<ListResponse<PublicationAttachmentDto>> =
        client.safeGet(
            path = "/publication/$publicationId/attachment",
        )

    override suspend fun getAll(page: Int): ApiResult<ListResponse<PublicationAttachmentDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): ApiResult<PublicationAttachmentDto> = client.safeGet(
        path = "/attachment/$id",
        notificationManager = notificationManager
    )

    override suspend fun deleteById(id: Int): ApiResult<PublicationAttachmentDto> = client.safeDelete(
        path = "/attachment/$id",
        notificationManager = notificationManager
    )

    override suspend fun update(data: PublicationAttachmentDto): ApiResult<PublicationAttachmentDto?> {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: PublicationAttachmentDto): ApiResult<PublicationAttachmentDto> =
        client.safePostAsJson(
            path = "/attachment",
            body = data,
            notificationManager = notificationManager
        )
}