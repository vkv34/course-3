package ru.online.education.domain.services.coursePublication

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.slf4j.LoggerFactory
import ru.online.education.core.exception.SelectExeption
import ru.online.education.core.util.apiCall
import ru.online.education.core.util.dbCall
import ru.online.education.data.table.PublicationOnCourse
import ru.online.education.data.table.PublicationTable
import ru.online.education.di.dbQuery
import ru.online.education.domain.repository.PublicationRepository
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.PublicationDto
import ru.online.education.domain.repository.model.UserRole
import util.ApiResult

class PublicationRepositoryImpl(
    private val userRole: UserRole,
//    private val courseCategoryRepository: CourseCategoryRepository,
//    private val publicationOnCourseRepository: PublicationOnCourseRepository
) : PublicationRepository {
    val logger = LoggerFactory.getLogger(this::class.java)

    override suspend fun getByCourseId(
        courseId: Int,
        page: Int,
    ): ApiResult<ListResponse<PublicationDto>> =
        dbCall(
            errorMessage = "Ошибка при выборке публикации курса с id = $courseId",
            call = {
                ListResponse(
                    dbQuery {
                        (PublicationOnCourse innerJoin PublicationTable)
                            .selectAll()
                            .where { PublicationOnCourse.courseTable eq courseId }
                            .apply {
                                if (userRole == UserRole.Student) {
                                    andWhere { PublicationOnCourse.visible eq true }
                                        .andWhere { PublicationOnCourse.temp eq false }
                                }
                            }
                            .orderBy(PublicationOnCourse.visible to SortOrder.ASC_NULLS_FIRST)
                            .orderBy(PublicationOnCourse.temp to SortOrder.DESC_NULLS_FIRST)
                            .orderBy(PublicationOnCourse.createdAt to SortOrder.DESC_NULLS_LAST)
                            .limit(pageSize, page.toLong() * pageSize)
                            .map(::resultRowToCoursePublication)
                    },
                )
            },
        )

    override suspend fun getByPublicationOnCourseId(publicationOnCourseId: Int): ApiResult<PublicationDto> =
        dbCall(
            call = {
                dbQuery {
                    (PublicationOnCourse innerJoin PublicationTable)
                        .selectAll()
                        .where { PublicationOnCourse.id eq publicationOnCourseId }
                        .singleOrNull()
                        ?.toCoursePublication()
                } ?: throw SelectExeption("Публикация в курсе с id = $publicationOnCourseId не найдена")
            },
        )

    override suspend fun getAll(page: Int): ApiResult<ListResponse<PublicationDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): ApiResult<PublicationDto> =
        dbCall(
            call = {
                dbQuery {
                    (PublicationOnCourse rightJoin PublicationTable)
                        .selectAll()
                        .where { PublicationTable.id eq id }
                        .singleOrNull()
                        ?.toCoursePublication()
                } ?: throw SelectExeption("Публикация с id = $id не найден")
            },
        )

    override suspend fun deleteById(id: Int): ApiResult<PublicationDto> {
        TODO("Not yet implemented")
    }

    override suspend fun update(data: PublicationDto): ApiResult<PublicationDto?> {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: PublicationDto): ApiResult<PublicationDto> =
        apiCall {
            val id =
                dbQuery {
                    if (data.publicationId == 0) {
                        val result =
                            PublicationTable
                                .insertAndGetId {
                                    coursePublicationDtoToInsertStatement(it, data)
                                }.value
                        commit()
                        result
                    } else {
                        PublicationTable
                            .update(where = { PublicationTable.id eq data.publicationId }) {
                                coursePublicationDtoToUpdateStatement(it, data)
                            }
                        PublicationOnCourse
                            .update(where = { PublicationOnCourse.id eq data.publicationInCourseId }) {
                                it[deadLine] = data.deadLine
                                    ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                            }
                        commit()
                        data.publicationId
                    }
                }
            getById(id)
        }

    private fun resultRowToCoursePublication(row: ResultRow) = row.toCoursePublication()

    private fun ResultRow.toCoursePublication() =
        try {
            logger.debug("this.hasValue(PublicationTable.id)")
            PublicationDto(
                publicationId = this[PublicationTable.id].value,
                courseId = this[PublicationOnCourse.courseTable].value,
                publicationInCourseId = this[PublicationOnCourse.id].value,
                visible = this[PublicationOnCourse.visible],
                temp = this[PublicationOnCourse.temp],
                createdAt = this[PublicationOnCourse.createdAt],
                deadLine = this[PublicationOnCourse.deadLine],
                title = this[PublicationTable.title],
                subTitle = this[PublicationTable.subTitle],
                content = this[PublicationTable.content],
                authorId = this[PublicationTable.author].value,
                type = this[PublicationTable.type],
            )
        } catch (_: Exception) {
            logger.debug("this not hasValue(PublicationTable.id)")

            PublicationDto(
                publicationId = this[PublicationTable.id].value,
//            courseId = this[PublicationOnCourse.courseTable].value,
//            publicationInCourseId = this[PublicationOnCourse.id].value,
                title = this[PublicationTable.title],
                subTitle = this[PublicationTable.subTitle],
                content = this[PublicationTable.content],
                authorId = this[PublicationTable.author].value,
                type = this[PublicationTable.type],
            )
        }

    private fun <T : Any> coursePublicationDtoToInsertStatement(
        statement: InsertStatement<T>,
        publicationDto: PublicationDto,
    ) {
        with(publicationDto) {
            statement[PublicationTable.id] = publicationId
            statement[PublicationTable.title] = title
            statement[PublicationTable.subTitle] = subTitle
            statement[PublicationTable.content] = content
            statement[PublicationTable.author] = authorId
            statement[PublicationTable.type] = type
//            statement[PublicationTable.visible] = visible
        }
    }

    private fun coursePublicationDtoToUpdateStatement(
        statement: UpdateStatement,
        publicationDto: PublicationDto,
    ) {
        with(publicationDto) {
            statement[PublicationTable.id] = publicationId
            statement[PublicationTable.title] = title
            statement[PublicationTable.subTitle] = subTitle
            statement[PublicationTable.content] = content
            statement[PublicationTable.type] = type
        }
    }


}
