package ru.online.education.domain.services.publicationAnswer.atachment

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.online.education.core.util.respond
import ru.online.education.core.util.respondCreated
import ru.online.education.domain.repository.model.PublicationAnswerAttachmentDto
import ru.online.education.domain.repository.model.PublicationAttachmentType
import ru.online.education.domain.services.publicationAttachment.json
import util.map
import java.io.File
import java.net.URLEncoder

fun Route.answerAttachmentRoute() {
    val repository = AnswerAttachmentRepositoryImpl()

    get("publicationAnswer/attachment/{id}") {
        val id =
            call.parameters["id"]?.toIntOrNull()
                ?: call.request.queryParameters["id"]
                    ?.toIntOrNull()
                ?: 0

        val attachmentDto =
            repository.getById(id).map {
                if (it.contentType == PublicationAttachmentType.File || it.contentType == PublicationAttachmentType.Image) {
                    it.copy(
                        content = "/file/${it.content}",
                    )
                } else {
                    it
                }
            }

        respond(attachmentDto)
    }
    route("publicationAnswer/file") {

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: 0
            respond(repository.deleteById(id))
        }
        get("all/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: 0

            respond(repository.getAttachments(id))
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: 0
            val attachmentDto = repository.getById(id).successOrNull()
            if (attachmentDto == null) {
                call.respond(HttpStatusCode.NotFound)
                finish()
            }
            checkNotNull(attachmentDto)
            val file = File(System.getenv("ATTACHMENT_DIR"), attachmentDto.content)
            val encodedFileName = URLEncoder.encode(attachmentDto.name, "UTF-8")

            val fileName = attachmentDto.name

            if (attachmentDto.contentType == PublicationAttachmentType.Image) {
                call.response.headers.append(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Inline.withParameter(ContentDisposition.Parameters.FileName, encodedFileName)
                        .toString(),
                )
            } else {
                call.response.headers.append(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, encodedFileName)
                        .toString(),
                )
            }

            call.respondFile(file)
        }

        post {
            val multipart = call.receiveMultipart()
            val parts = multipart.readAllParts()
            val jsonPart =
                parts
                    .filterIsInstance<PartData.FormItem>()
                    .firstOrNull { it.name == "json" }
            val attachmentDto =
                json.decodeFromString<PublicationAnswerAttachmentDto>(
                    jsonPart?.value ?: "",
                )
            jsonPart?.dispose?.invoke()
            if (attachmentDto.contentType == PublicationAttachmentType.File || attachmentDto.contentType == PublicationAttachmentType.Image) {
                val filePart = parts.filterIsInstance<PartData.FileItem>()
                if (filePart.isNotEmpty()) {
                    val fileBytes = filePart.first().streamProvider().readBytes()
                    val fileName = filePart.first().originalFileName

                    val attachment =
                        repository.uploadFile(
                            fileName = attachmentDto.name.ifEmpty { fileName ?: "Файл без имени" },
                            file = fileBytes,
                            answerId = attachmentDto.publicationAnswerId,
                            progressChanged = { _, _ -> },
                            fileType = attachmentDto.contentType,
                        )
                    respondCreated(attachment)
                    finish()
                }
            } else {
                respondCreated(repository.add(attachmentDto))
            }
        }
    }
}
