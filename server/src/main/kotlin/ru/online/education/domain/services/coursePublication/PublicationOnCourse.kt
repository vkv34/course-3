package ru.online.education.domain.services.coursePublication

import model.ListResponse
import model.PublicationOnCourseDto
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import repository.PublicationOnCourseRepository
import ru.online.education.core.exception.SelectExeption
import ru.online.education.core.util.apiCall
import ru.online.education.data.table.PublicationOnCourse
import ru.online.education.di.dbQuery
import util.ApiResult

class PublicationOnCourseRepositoryImpl : PublicationOnCourseRepository {
    override suspend fun getAll(page: Int): ApiResult<ListResponse<PublicationOnCourseDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): ApiResult<PublicationOnCourseDto> = apiCall(
        errorMessage = "Ошибка при выборке данных",
        call = {
            dbQuery {
                PublicationOnCourse
                    .selectAll()
                    .where { PublicationOnCourse.id eq id }
                    .firstOrNull()
                    ?.toPublicationOnCourse()
                    ?: throw SelectExeption("Публикация с id = $id не найдена")
            }
        }
    )

    override suspend fun deleteById(id: Int): ApiResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun update(data: PublicationOnCourseDto): ApiResult<PublicationOnCourseDto?> {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: PublicationOnCourseDto): ApiResult<PublicationOnCourseDto> = apiCall {
        dbQuery {
            val id = PublicationOnCourse.insertAndGetId {
                data.toInsertStatement(it)
            }.value
            commit()

            getById(id)
        }
    }

    private fun resultRowToPublicationOnCourse(row: ResultRow) = row.toPublicationOnCourse()
    private fun ResultRow.toPublicationOnCourse() = PublicationOnCourseDto(
        id = this[PublicationOnCourse.id].value,
        publicationId = this[PublicationOnCourse.publication].value,
        courseId = this[PublicationOnCourse.courseTable].value,
        userId = this[PublicationOnCourse.user].value
    )

    private fun <T : Any> PublicationOnCourseDto.toInsertStatement(
        statement: InsertStatement<T>
    ) {
        statement[PublicationOnCourse.publication] = publicationId
        statement[PublicationOnCourse.courseTable] = courseId
        statement[PublicationOnCourse.user] = userId
    }
}