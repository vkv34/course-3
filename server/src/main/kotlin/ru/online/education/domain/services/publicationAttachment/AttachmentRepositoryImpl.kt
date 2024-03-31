package ru.online.education.domain.services.publicationAttachment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.ListResponse
import model.PublicationAttachmentDto
import model.PublicationAttachmentType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import repository.AttachmentRepository
import ru.online.education.core.exception.SelectExeption
import ru.online.education.core.util.apiCall
import ru.online.education.core.util.dbCall
import ru.online.education.data.table.PublicationAttachmentTable
import ru.online.education.di.dbQuery
import util.ApiResult
import java.io.File
import java.util.*

class AttachmentRepositoryImpl : AttachmentRepository {

    val defaultFilePath = System.getenv("ATTACHMENT_DIR")
    override suspend fun uploadFile(
        file: ByteArray,
        fileName: String,
        publicationId: Int,
        progressChanged: (sentBytes: Long, totalBytes: Long) -> Unit
    ): ApiResult<PublicationAttachmentDto> = try {
        val path = "$defaultFilePath/${UUID.randomUUID()}.${fileName.split(".").last()}"
        withContext(Dispatchers.IO) {
            File(path).writeBytes(file)
        }

        add(
            PublicationAttachmentDto(
                publicationId = publicationId,
                name = fileName,
                content = path,
                contentType = PublicationAttachmentType.File
            )
        )
    } catch (e: Exception) {
        ApiResult.Error(e.localizedMessage, e)
    }

    override suspend fun getAttachments(publicationId: Int): ApiResult<ListResponse<PublicationAttachmentDto>> =
        dbCall {
            ListResponse(
                dbQuery {
                    PublicationAttachmentTable
                        .selectAll()
                        .where { PublicationAttachmentTable.publication eq publicationId }
                        .map(::resultRowToPublicationAttachment)
                }
            )
        }

    override suspend fun getAll(page: Int): ApiResult<ListResponse<PublicationAttachmentDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): ApiResult<PublicationAttachmentDto> =
        dbCall {
            dbQuery {
                PublicationAttachmentTable.selectAll()
                    .where { PublicationAttachmentTable.id eq id }
                    .firstOrNull()
                    ?.toPublicationAttachment()
                    ?: throw SelectExeption("Вложение с id = $id не найдена")
            }
        }

    override suspend fun deleteById(id: Int): ApiResult<PublicationAttachmentDto> {
        TODO("Not yet implemented")
    }

    override suspend fun update(data: PublicationAttachmentDto): ApiResult<PublicationAttachmentDto?> {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: PublicationAttachmentDto): ApiResult<PublicationAttachmentDto> = apiCall {
        val id = dbQuery {
            PublicationAttachmentTable
                .insertAndGetId {
                    data.toInsertStatement(it)
                }.value
        }
        getById(id)
    }

    private fun resultRowToPublicationAttachment(row: ResultRow) = row.toPublicationAttachment()
    private fun ResultRow.toPublicationAttachment() = PublicationAttachmentDto(
        publicationId = this[PublicationAttachmentTable.publication].value,
        attachmentId = this[PublicationAttachmentTable.id].value,
        name = this[PublicationAttachmentTable.name],
        contentType = this[PublicationAttachmentTable.type],
        content = this[PublicationAttachmentTable.content],
        dateCreate = this[PublicationAttachmentTable.createdAt]
    )

    private fun <T : Any> PublicationAttachmentDto.toInsertStatement(
        statement: InsertStatement<T>
    ) {
        statement[PublicationAttachmentTable.publication] = publicationId
        statement[PublicationAttachmentTable.id] = attachmentId
        statement[PublicationAttachmentTable.name] = name
        statement[PublicationAttachmentTable.type] = contentType
        statement[PublicationAttachmentTable.content] = content
        statement[PublicationAttachmentTable.createdAt] = dateCreate
    }
}