package ru.online.education.domain.services.coursePublication

import model.ListResponse
import model.PublicationDto
import model.UserRole
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.slf4j.LoggerFactory
import repository.CoursePublicationRepository
import ru.online.education.core.exception.SelectExeption
import ru.online.education.core.util.apiCall
import ru.online.education.data.table.PublicationOnCourse
import ru.online.education.data.table.PublicationTable
import ru.online.education.di.dbQuery
import util.ApiResult

class PublicationRepositoryImpl(
    private val userRole: UserRole,
//    private val courseCategoryRepository: CourseCategoryRepository,
//    private val publicationOnCourseRepository: PublicationOnCourseRepository
) : CoursePublicationRepository {

    val logger = LoggerFactory.getLogger(this::class.java)
    override suspend fun getByCourseId(courseId: Int, page: Int): ApiResult<ListResponse<PublicationDto>> =
        apiCall(
            errorMessage = "Ошибка при выборке публикации курса с id = $courseId",
            call = {
                ListResponse(
                    dbQuery {
                        (PublicationOnCourse innerJoin PublicationTable)
                            .selectAll()
                            .where { PublicationOnCourse.courseTable eq courseId }
                            .apply {
                                if (userRole != UserRole.Student) {
                                    andWhere { PublicationOnCourse.visible eq true }
                                        .andWhere { PublicationOnCourse.temp eq false }
                                }
                            }
                            .limit(pageSize, page.toLong() * pageSize)
                            .map(::resultRowToCoursePublication)
                    }
                )
            }
        )


    override suspend fun getAll(page: Int): ApiResult<ListResponse<PublicationDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(id: Int): ApiResult<PublicationDto> = apiCall(
        call = {
            dbQuery {
                PublicationTable
                    .selectAll()
                    .where { PublicationTable.id eq id }
                    .singleOrNull()
                    ?.toCoursePublication()
            } ?: throw SelectExeption("Публикация с id = $id не найден")
        }
    )

    override suspend fun deleteById(id: Int): ApiResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun update(data: PublicationDto): ApiResult<PublicationDto?> {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: PublicationDto): ApiResult<PublicationDto> = apiCall {
        val id = dbQuery {
            PublicationTable
                .insertAndGetId {
                    coursePublicationDtoToInsertStatement(it, data)
                }.value
        }
        getById(id)
    }

    private fun resultRowToCoursePublication(row: ResultRow) = row.toCoursePublication()
    private fun ResultRow.toCoursePublication() = try{
        logger.debug("this.hasValue(PublicationTable.id)")
        PublicationDto(
            publicationId = this[PublicationTable.id].value,
            courseId = this[PublicationOnCourse.courseTable].value,
            publicationInCourseId = this[PublicationOnCourse.id].value,
            visible = this[PublicationOnCourse.visible],
            temp = this[PublicationOnCourse.temp],
            title = this[PublicationTable.title],
            subTitle = this[PublicationTable.subTitle],
            content = this[PublicationTable.content],
            authorId = this[PublicationTable.author].value,
            type = this[PublicationTable.type]
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
            type = this[PublicationTable.type]
        )
    }

    private fun <T : Any> coursePublicationDtoToInsertStatement(
        statement: InsertStatement<T>,
        publicationDto: PublicationDto
    ) {
        with(publicationDto) {
            statement[PublicationTable.id] = publicationId
            statement[PublicationTable.title] = title
            statement[PublicationTable.subTitle] = subTitle
            statement[PublicationTable.content] = content
            statement[PublicationTable.author] = authorId
            statement[PublicationTable.type] = type

        }

    }

}