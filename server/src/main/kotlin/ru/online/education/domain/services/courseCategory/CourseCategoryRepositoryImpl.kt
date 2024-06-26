package ru.online.education.domain.services.courseCategory

import ru.online.education.domain.repository.model.CourseCategoryDto
import ru.online.education.domain.repository.model.ListResponse
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import ru.online.education.domain.repository.CourseCategoryRepository
import ru.online.education.core.exception.SelectExeption
import ru.online.education.core.util.apiCall
import ru.online.education.core.util.dbCall
import ru.online.education.data.table.CourseCategoryTable
import ru.online.education.di.dbQuery
import util.ApiResult

class CourseCategoryRepositoryImpl : CourseCategoryRepository {
    override suspend fun findByName(
        name: String,
        page: Int,
    ): ApiResult<ListResponse<CourseCategoryDto>> =
        apiCall {
            val result =
                dbQuery {
                    CourseCategoryTable
                        .selectAll()
                        .where { CourseCategoryTable.name like "%$name%" }
                        .limit(pageSize, (page * pageSize).toLong())
                        .map(::resultRowToCourse)
                }
            if (result.isNotEmpty()) {
                ApiResult.Success(ListResponse(result))
            } else {
                ApiResult.Success(ListResponse(listOf()))
            }
        }

    override suspend fun getAll(page: Int): ApiResult<ListResponse<CourseCategoryDto>> =
        dbCall(
            successMessage = "",
            errorMessage = "Ошибка при выборке курсов",
            call = {
                ListResponse(
                    dbQuery {
                        CourseCategoryTable
                            .selectAll()
                            .limit(pageSize, (page * pageSize).toLong())
                            .map(::resultRowToCourse)
                    },
                )
            },
        )

    override suspend fun getById(id: Int): ApiResult<CourseCategoryDto> =
        dbCall(
            errorMessage = "Course category not added",
            call = {
                dbQuery {
                    CourseCategoryTable
                        .selectAll()
                        .where { CourseCategoryTable.id eq id }
                        .singleOrNull()
                        ?.toCourseCategory()
                } ?: throw SelectExeption("Курс с id = $id не найден")
            },
        )

    override suspend fun deleteById(id: Int): ApiResult<CourseCategoryDto> = ApiResult.Error("Not Implemented")

    override suspend fun update(data: CourseCategoryDto): ApiResult<CourseCategoryDto?> {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: CourseCategoryDto): ApiResult<CourseCategoryDto> =

        apiCall {
            val id =
                dbQuery {
                    CourseCategoryTable
                        .insertAndGetId {
                            courseCategoryToInsertStatement(it, data)
                        }.value
                }
            getById(id)
        }

    private fun resultRowToCourse(row: ResultRow) = row.toCourseCategory()

    private fun ResultRow.toCourseCategory() =
        CourseCategoryDto(
            id = this[CourseCategoryTable.id].value,
            name = this[CourseCategoryTable.name],
            description = this[CourseCategoryTable.description],
        )

    private fun <T : Any> courseCategoryToInsertStatement(
        statement: InsertStatement<T>,
        courseCategory: CourseCategoryDto,
    ) {
        with(courseCategory) {
            statement[CourseCategoryTable.name] = name
            statement[CourseCategoryTable.description] = description
        }
    }
}
