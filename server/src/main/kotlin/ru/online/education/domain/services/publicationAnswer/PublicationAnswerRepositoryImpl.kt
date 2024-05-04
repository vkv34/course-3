package ru.online.education.domain.services.publicationAnswer

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import ru.online.education.core.exception.SelectExeption
import ru.online.education.core.util.apiCall
import ru.online.education.core.util.dbCall
import ru.online.education.data.table.PublicationAnswerTable
import ru.online.education.data.table.PublicationOnCourse
import ru.online.education.data.table.UsersTable
import ru.online.education.di.dbQuery
import ru.online.education.domain.repository.PublicationAnswerRepository
import ru.online.education.domain.repository.UserRepository
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.PublicationAnswerDto
import util.ApiResult
import util.map

class PublicationAnswerRepositoryImpl(
    private val userRepository: UserRepository
) : PublicationAnswerRepository {
    override suspend fun getAllByPublicationOnCourseId(
        publicationOnCourseId: Int,
        page: Int
    ): ApiResult<ListResponse<PublicationAnswerDto>> = dbCall {
        dbQuery {
            ListResponse(
                PublicationAnswerTable
                    .selectAll()
                    .where { PublicationAnswerTable.publicationOnCourse eq publicationOnCourseId }
                    .orderBy(PublicationAnswerTable.createdAt)
                    .orderBy(PublicationAnswerTable.user)
                    .limit(n = pageSize, offset = pageSize * page.toLong())
                    .suspendMap { it.toPublicationAnswerAttachment() }
            )
        }
    }

    override suspend fun getAllByPublicationOnCourseId(
        publicationOnCourseId: Int,
        userId: Int,
        page: Int
    ): ApiResult<ListResponse<PublicationAnswerDto>> = dbCall {
        dbQuery {
            ListResponse(
                PublicationAnswerTable
                    .selectAll()
                    .where { PublicationAnswerTable.publicationOnCourse eq publicationOnCourseId }
                    .andWhere { PublicationAnswerTable.user eq userId }
                    .orderBy(PublicationAnswerTable.createdAt)
                    .limit(n = pageSize, offset = pageSize * page.toLong())
                    .suspendMap { it.toPublicationAnswerAttachment() }
            )
        }
    }

    override suspend fun getAll(page: Int): ApiResult<ListResponse<PublicationAnswerDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): ApiResult<PublicationAnswerDto> = dbCall {
        PublicationAnswerTable
            .selectAll()
            .where { PublicationAnswerTable.id eq id }
            .singleOrNull()
            ?.toPublicationAnswerAttachment()
            ?: throw SelectExeption("Ответ с id=$id не найден")
    }

    override suspend fun deleteById(id: Int): ApiResult<PublicationAnswerDto> = apiCall {
        val deleting = getById(id)

        dbQuery {
            PublicationAnswerTable
                .deleteWhere { PublicationAnswerTable.id eq id }
        }

        deleting
    }

    override suspend fun update(data: PublicationAnswerDto): ApiResult<PublicationAnswerDto?> = apiCall {
        dbQuery {
            PublicationAnswerTable
                .update(where = { PublicationAnswerTable.id eq data.id }) {
                    data.toUpdateStatement(it)
                }
        }
        getById(data.id)
    }.map { it }

    override suspend fun add(data: PublicationAnswerDto): ApiResult<PublicationAnswerDto> = apiCall {
        val createdId = dbQuery {
            PublicationAnswerTable
                .insertAndGetId {
                    data.toInsertStatement(it)
                }.value
        }

        getById(createdId)
    }

    private suspend fun resultRowToPublicationAttachment(row: ResultRow) = row.toPublicationAnswerAttachment()

    private suspend fun ResultRow.toPublicationAnswerAttachment(): PublicationAnswerDto {
        val user = userRepository.getById(this[PublicationAnswerTable.user].value).successOrNull()!!
        return PublicationAnswerDto(
            id = this[PublicationAnswerTable.id].value,
            userDto = user,
            teacher = this[PublicationAnswerTable.reviewer]?.value?.let { userRepository.getById(it).successOrNull() },
            comment = this[PublicationAnswerTable.comment],
            mark = this[PublicationAnswerTable.mark],
            dateCreate = this[PublicationAnswerTable.createdAt],
            userId = user.id,
            publicationOnCourseId = this[PublicationAnswerTable.publicationOnCourse].value,
            answer = this[PublicationAnswerTable.answer]
        )
    }

    private fun <T : Any> PublicationAnswerDto.toInsertStatement(statement: InsertStatement<T>) {
        statement[PublicationAnswerTable.user] = EntityID(userId ?: error("id пользователя не указан"), UsersTable)
        statement[PublicationAnswerTable.comment] = comment
        statement[PublicationAnswerTable.publicationOnCourse] = EntityID(publicationOnCourseId, PublicationOnCourse)
        statement[PublicationAnswerTable.answer] = answer
    }

    private fun PublicationAnswerDto.toUpdateStatement(statement: UpdateStatement) {
//        statement[PublicationAnswerTable.user] = EntityID(userId ?: error("id пользователя не указан"), UsersTable)
        statement[PublicationAnswerTable.comment] = comment
        statement[PublicationAnswerTable.publicationOnCourse] = EntityID(publicationOnCourseId, PublicationOnCourse)
        statement[PublicationAnswerTable.answer] = answer
        statement[PublicationAnswerTable.reviewer] = teacher?.let { EntityID(it.id, UsersTable) }
        statement[PublicationAnswerTable.mark] = mark
    }

    private suspend fun <T, R> Iterable<T>.suspendMap(suspendTransform: suspend (T) -> R): List<R> =
        coroutineScope {
            map { async { suspendTransform(it) } }
                .awaitAll()
        }
}