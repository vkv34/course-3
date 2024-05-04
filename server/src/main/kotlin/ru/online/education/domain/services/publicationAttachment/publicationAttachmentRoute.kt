package ru.online.education.domain.services.publicationAttachment

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import ru.online.education.domain.repository.model.BaseModel
import ru.online.education.domain.repository.model.PublicationAttachmentDto
import ru.online.education.domain.repository.model.PublicationAttachmentType
import ru.online.education.domain.repository.model.UserRole
import org.koin.ktor.ext.inject
import ru.online.education.domain.repository.AttachmentRepository
import ru.online.education.core.util.respond
import ru.online.education.core.util.respondCreated
import ru.online.education.core.util.role
import ru.online.education.domain.services.account.auth.jwtAuthenticate
import util.map
import java.io.File
import java.net.URLEncoder
val json =
    Json {
        serializersModule = api.defaultSerializersModule
    }

fun Route.publicationAttachmentRoute() {
    val attachmentRepository by inject<AttachmentRepository>()
    jwtAuthenticate {
        role<BaseModel>(UserRole.all)
        get("publication/{id}/attachment") {
            val publicationId = call.parameters["id"]?.toIntOrNull() ?: 0

            respond(attachmentRepository.getAttachments(publicationId))
        }
    }
    route("attachment") {
        jwtAuthenticate {
            role<BaseModel>(UserRole.all)
            post {
                val attachmentDto = call.receive<PublicationAttachmentDto>()
                respondCreated(attachmentRepository.add(attachmentDto))
            }
            post("/file") {
                val multipart = call.receiveMultipart()
                val parts = multipart.readAllParts()
                val jsonPart =
                    parts
                        .filterIsInstance<PartData.FormItem>()
                        .firstOrNull { it.name == "json" }
                val attachmentDto =
                    json.decodeFromString<PublicationAttachmentDto>(
                        jsonPart?.value ?: "",
                    )
                jsonPart?.dispose?.invoke()
                if (attachmentDto.contentType == PublicationAttachmentType.File || attachmentDto.contentType == PublicationAttachmentType.Image) {
                    val filePart = parts.filterIsInstance<PartData.FileItem>()
                    if (filePart.isNotEmpty()) {
                        val fileBytes = filePart.first().streamProvider().readBytes()
                        val fileName = filePart.first().originalFileName

                        val attachment =
                            attachmentRepository.uploadFile(
                                fileName = attachmentDto.name.ifEmpty { fileName ?: "Файл без имени" },
                                file = fileBytes,
                                publicationId = attachmentDto.publicationId,
                                progressChanged = { _, _ -> },
                                fileType = attachmentDto.contentType,
                            )
                        respondCreated(attachment)
                        finish()
                    }
                } else {
                    respondCreated(attachmentRepository.add(attachmentDto))
                }
            }
        }

        get("/{id}") {
            val id =
                call.parameters["id"]?.toIntOrNull()
                    ?: call.request.queryParameters["id"]
                        ?.toIntOrNull()
                    ?: 0

            val attachmentDto =
                attachmentRepository.getById(id).map {
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

//        staticFiles(
//            "/file",
//            dir = File(System.getenv("ATTACHMENT_DIR")),
//
//            )

        get("file/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: 0
            val attachmentDto = attachmentRepository.getById(id).successOrNull()
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
                    ContentDisposition.Inline.withParameter(ContentDisposition.Parameters.FileName, encodedFileName).toString(),
                )
            }else{
                call.response.headers.append(
                    HttpHeaders.ContentDisposition,
                    ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, encodedFileName).toString(),
                )
            }

            call.respondFile(file)
        }
        get("image/{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: 0
            val attachmentDto = attachmentRepository.getById(id).successOrNull()
            if (attachmentDto == null) {
                call.respond(HttpStatusCode.NotFound)
                finish()
            }
            checkNotNull(attachmentDto)
            val file = File(System.getenv("ATTACHMENT_DIR"), attachmentDto.content)
            val encodedFileName = URLEncoder.encode(attachmentDto.name, "UTF-8")

            val fileName = attachmentDto.name

            call.response.headers.append(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, encodedFileName).toString(),
            )

            call.respond(file)
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: 0
            respond(attachmentRepository.deleteById(id))
        }
    }
}
