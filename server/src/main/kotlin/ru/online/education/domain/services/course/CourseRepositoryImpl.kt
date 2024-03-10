package ru.online.education.domain.services.course

import model.CourseDto
import model.Image
import model.ListResponse
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import repository.CourseRepository
import ru.online.education.core.exception.SelectExeption
import ru.online.education.core.util.apiCall
import ru.online.education.data.table.CourseCategoryTable
import ru.online.education.data.table.CourseTable
import ru.online.education.data.table.UserOnCourse
import ru.online.education.data.table.UsersTable
import ru.online.education.di.dbQuery
import util.ApiResult

class CourseRepositoryImpl : CourseRepository {
    override suspend fun findByName(name: String): CourseDto {
        TODO("Not yet implemented")
    }

    override suspend fun filterByUser(page: Int, userId: Int): ApiResult<ListResponse<CourseDto>> = apiCall(
        call = {
            val otherCoursesIds = UserOnCourse.select(UserOnCourse.course)
                .where { UserOnCourse.role eq 0 }
                .andWhere { UserOnCourse.user eq userId }

            ListResponse(
                dbQuery {
                    CourseTable.selectAll()
                        .where { CourseTable.createdBy eq userId }
                        .orWhere { CourseTable.id inSubQuery otherCoursesIds }
                        .limit(pageSize, (page * pageSize).toLong())
                        .map(::resultRowToCourse)
                }
            )
        }
    )

    override suspend fun getAll(page: Int): ApiResult<ListResponse<CourseDto>> =
        apiCall(
            successMessage = "Курс найден",
            errorMessage = "Курс не найден",
            call = {
                ListResponse(
                    dbQuery {
                        CourseTable.selectAll()
                            .limit(pageSize, (page * pageSize).toLong())
                            .map(::resultRowToCourse)
                    }
                )
            }
        )

    override suspend fun getById(id: Int): ApiResult<CourseDto> =
        apiCall(
            successMessage = "Курс найден",
            errorMessage = "Курс не найден",
            call = {
                dbQuery {
                    CourseTable.selectAll()
                        .where { CourseTable.id eq id }
                        .singleOrNull()
                        ?.toCourse()
                } ?: throw SelectExeption("Курс с id = $id не найден")
            }
        )


    override suspend fun deleteById(id: Int): ApiResult<Unit> = ApiResult.Error.notImplemented()

    override suspend fun update(data: CourseDto): ApiResult<CourseDto?> = ApiResult.Error.notImplemented()

    override suspend fun add(data: CourseDto): ApiResult<CourseDto> = apiCall {
        val id = dbQuery {
            CourseTable.insertAndGetId {
                courseToInsertStatement(it, data)
            }.value
        }
        getById(id)
    }

    private fun resultRowToCourse(row: ResultRow) = row.toCourse()
    private fun ResultRow.toCourse() = CourseDto(
        id = this[CourseTable.id].value,
        name = this[CourseTable.name],
        creatorId = this[CourseTable.createdBy]?.value,
        shortDescription = this[CourseTable.shortDescription],
        longDescription = this[CourseTable.longDescription],
        dateCreate = this[CourseTable.dateCreate],
        avatar = Image.ImageResource(this[CourseTable.avatar]),
        background = Image.ImageResource(this[CourseTable.color]),
        courseCategoryId = this[CourseTable.courseCategory].value
    )

    private fun <T : Any> courseToInsertStatement(statement: InsertStatement<T>, course: CourseDto) {
        with(course) {
            statement[CourseTable.name] = name
            if (creatorId != null)
                statement[CourseTable.createdBy] = EntityID(creatorId!!, UsersTable)
            statement[CourseTable.courseCategory] = EntityID(courseCategoryId, CourseCategoryTable)
            statement[CourseTable.shortDescription] = shortDescription
            statement[CourseTable.longDescription] = longDescription
            statement[CourseTable.dateCreate] = dateCreate
            statement[CourseTable.avatar] = (avatar as? Image.ImageResource)?.src ?: ""
            statement[CourseTable.color] = (background as? Image.ImageResource)?.src ?: ""
        }

    }
}