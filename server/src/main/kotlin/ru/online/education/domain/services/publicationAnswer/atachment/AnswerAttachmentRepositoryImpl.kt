package ru.online.education.domain.services.publicationAnswer.atachment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.update
import ru.online.education.core.util.apiCall
import ru.online.education.core.util.dbCall
import ru.online.education.data.table.PublicationAnswerAttachmentTable
import ru.online.education.data.table.PublicationAnswerTable
import ru.online.education.di.dbQuery
import ru.online.education.domain.repository.AnswerAttachmentRepository
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.PublicationAnswerAttachmentDto
import ru.online.education.domain.repository.model.PublicationAttachmentType
import util.ApiResult
import util.map
import java.io.File
import java.io.IOException
import java.util.*

class AnswerAttachmentRepositoryImpl : AnswerAttachmentRepository {
    private val defaultFilePath: String = System.getenv("ATTACHMENT_DIR")


    override suspend fun uploadFile(
        file: ByteArray,
        fileName: String,
        answerId: Int?,
        progressChanged: (sentBytes: Long, totalBytes: Long) -> Unit,
        fileType: PublicationAttachmentType
    ): ApiResult<PublicationAnswerAttachmentDto> = try {
        val resultFileName = "${UUID.randomUUID()}.${fileName.split(".").last()}"
        val path = "$defaultFilePath/$resultFileName"
        withContext(Dispatchers.IO) {
            File(path).writeBytes(file)
        }

        add(
            PublicationAnswerAttachmentDto(
                publicationAnswerId = answerId,
                name = fileName,
                content = resultFileName,
                contentType = fileType
            )
        )
    } catch (e: Exception) {
        ApiResult.Error(e.localizedMessage, e)
    }

    override suspend fun getAttachments(
        answerId: Int
    ): ApiResult<ListResponse<PublicationAnswerAttachmentDto>> = dbCall {
        ListResponse(
            dbQuery {
                PublicationAnswerAttachmentTable
                    .selectAll()
                    .where { PublicationAnswerAttachmentTable.publicationAnswer eq answerId }
                    .map(::resultRowToAnswerAttachment)
            },
        )
    }

    override suspend fun sendFile(
        publicationAttachmentId: Int, answerId: Int
    ): ApiResult<PublicationAnswerAttachmentDto> = apiCall {
        dbQuery {
            PublicationAnswerAttachmentTable
                .update(
                    where = {
                        PublicationAnswerAttachmentTable.id eq publicationAttachmentId
                    }
                ) { updateStatement ->
                    updateStatement[publicationAnswer] = EntityID(answerId, PublicationAnswerTable)
                }
        }
        getById(publicationAttachmentId)
    }.map { it }

    override suspend fun getAll(page: Int): ApiResult<ListResponse<PublicationAnswerAttachmentDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): ApiResult<PublicationAnswerAttachmentDto> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteById(id: Int): ApiResult<PublicationAnswerAttachmentDto> {
        val attachment = getById(id)
        if (attachment !is ApiResult.Success) {
            return attachment
        }
        val path = "$defaultFilePath/${attachment.data.content}"

        try {
            withContext(Dispatchers.IO) {
                File(path).delete()
            }
        } catch (e: IOException) {
            return ApiResult.Error("Вложение не удалось удалить", e)
        }
        return dbQuery {
            PublicationAnswerAttachmentTable
                .deleteWhere { PublicationAnswerAttachmentTable.id eq id }
            ApiResult.Success(attachment.data)
        }
    }

    override suspend fun update(
        data: PublicationAnswerAttachmentDto
    ): ApiResult<PublicationAnswerAttachmentDto?> = TODO()

    override suspend fun add(data: PublicationAnswerAttachmentDto): ApiResult<PublicationAnswerAttachmentDto> {
        TODO("Not yet implemented")
    }

    private fun resultRowToAnswerAttachment(row: ResultRow) = row.toAnswerAttachment()

    private fun ResultRow.toAnswerAttachment() =
        PublicationAnswerAttachmentDto(
            id = this[PublicationAnswerAttachmentTable.id].value,
            publicationAnswerId = this[PublicationAnswerAttachmentTable.publicationAnswer]?.value,
            name = this[PublicationAnswerAttachmentTable.name],
            contentType = this[PublicationAnswerAttachmentTable.type],
            content = this[PublicationAnswerAttachmentTable.content],
            dateCreate = this[PublicationAnswerAttachmentTable.createdAt],
        )

    private fun <T : Any> PublicationAnswerAttachmentDto.toInsertStatement(
        statement: InsertStatement<T>
    ) {
        statement[PublicationAnswerAttachmentTable.publicationAnswer] =
            publicationAnswerId?.let { EntityID(it, PublicationAnswerTable) }
        statement[PublicationAnswerAttachmentTable.name] = name
        statement[PublicationAnswerAttachmentTable.type] = contentType
        statement[PublicationAnswerAttachmentTable.content] = content
        statement[PublicationAnswerAttachmentTable.createdAt] = dateCreate
    }
//    private fun <T : Any> PublicationAnswerAttachmentDto.toUpdateStatement(
//        statement: InsertStatement<T>
//    ) {
//        statement[PublicationAnswerAttachmentTable.publicationAnswer] =
//            publicationAnswerId?.let { EntityID(it, PublicationAnswerTable) }
//        statement[PublicationAnswerAttachmentTable.name] = name
//        statement[PublicationAnswerAttachmentTable.type] = contentType
//        statement[PublicationAnswerAttachmentTable.content] = content
//        statement[PublicationAnswerAttachmentTable.createdAt] = dateCreate
//    }

}