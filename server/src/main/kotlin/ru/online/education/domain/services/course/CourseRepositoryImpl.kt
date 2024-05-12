package ru.online.education.domain.services.course

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement
import ru.online.education.core.exception.SelectExeption
import ru.online.education.core.util.apiCall
import ru.online.education.core.util.dbCall
import ru.online.education.data.table.*
import ru.online.education.di.dbQuery
import ru.online.education.domain.repository.CourseRepository
import ru.online.education.domain.repository.model.*
import util.ApiResult
import util.map

class CourseRepositoryImpl : CourseRepository {
    override suspend fun findByName(name: String): CourseDto {
        TODO("Not yet implemented")
    }

    override suspend fun filterByUser(
        page: Int,
        userId: Int,
        archived: Boolean
    ): ApiResult<ListResponse<CourseDto>> =
        dbCall(
            call = {
                val otherCoursesIds =
                    UserOnCourse.select(UserOnCourse.course)
//                        .where { UserOnCourse.role eq 0 }
                        .andWhere { UserOnCourse.user eq userId }
                        .orderBy(UserOnCourse.role)

                ListResponse(
                    dbQuery {
                        CourseTable.selectAll()
                            .where { CourseTable.createdBy eq userId }
                            .orWhere { CourseTable.id inSubQuery otherCoursesIds }
                            .also {
                                if (archived) {
                                    it.andWhere { CourseTable.courseState eq CourseState.Archived }
                                }else{
                                    it.andWhere { CourseTable.courseState neq CourseState.Archived }
                                }
                            }
                            .limit(pageSize, (page * pageSize).toLong())
                            .map(::resultRowToCourse)
                    },
                )
            },
        )

    override suspend fun archiveCourse(courseId: Int, archived: Boolean): ApiResult<CourseDto> = apiCall {
        dbQuery {
            CourseTable
                .update(where = { CourseTable.id eq courseId }) {
                    it[courseState] = if (archived) CourseState.Archived else CourseState.InUse
                }
        }

        getById(courseId)
    }

    override suspend fun getAll(page: Int): ApiResult<ListResponse<CourseDto>> =
        dbCall(
            successMessage = "Курс найден",
            errorMessage = "Курс не найден",
            call = {
                ListResponse(
                    dbQuery {
                        CourseTable.selectAll()
                            .limit(pageSize, (page * pageSize).toLong())
                            .map(::resultRowToCourse)
                    },
                )
            },
        )

    override suspend fun getById(id: Int): ApiResult<CourseDto> =
        dbCall(
            successMessage = "Курс найден",
            errorMessage = "Курс не найден",
            call = {
                dbQuery {
                    CourseTable.selectAll()
                        .where { CourseTable.id eq id }
                        .singleOrNull()
                        ?.toCourse()
                } ?: throw SelectExeption("Курс с id = $id не найден")
            },
        )

    override suspend fun deleteById(id: Int): ApiResult<CourseDto> = ApiResult.Error.notImplemented()

    override suspend fun update(data: CourseDto): ApiResult<CourseDto?> = apiCall {
        dbQuery {
            CourseTable
                .update(where = { CourseTable.id eq data.id }){
                    it[name] = data.name
                }
        }
        getById(data.id)
    }.map { it }

    override suspend fun add(data: CourseDto): ApiResult<CourseDto> =
        apiCall {
            val id =
                dbQuery {

                    val createdId = CourseTable.insertAndGetId {
                        courseToInsertStatement(it, data)
                    }.value

                    commit()

                    UserOnCourse.insert { statement ->
                        statement[user] = EntityID(data.creatorId ?: 0, UsersTable)
                        statement[role] = EntityID(UserRole.all.indexOf(UserRole.Admin) + 1, UserRoleTable)
                        statement[this.course] = EntityID(createdId, CourseTable)
                    }
                    createdId
                }
            getById(id)
        }

    private fun resultRowToCourse(row: ResultRow) = row.toCourse()

    private fun ResultRow.toCourse() =
        CourseDto(
            id = this[CourseTable.id].value,
            name = this[CourseTable.name],
            creatorId = this[CourseTable.createdBy]?.value,
            shortDescription = this[CourseTable.shortDescription],
            longDescription = this[CourseTable.longDescription],
            dateCreate = this[CourseTable.dateCreate],
            avatar = Image.ImageResource(this[CourseTable.avatar]),
            background = Image.ImageResource(this[CourseTable.color]),
            courseCategoryId = this[CourseTable.courseCategory].value,
        )

    private fun <T : Any> courseToInsertStatement(
        statement: InsertStatement<T>,
        course: CourseDto,
    ) {
        with(course) {
            statement[CourseTable.name] = name
            if (creatorId != null) {
                statement[CourseTable.createdBy] = EntityID(creatorId!!, UsersTable)
            }
            statement[CourseTable.courseCategory] = EntityID(courseCategoryId, CourseCategoryTable)
            statement[CourseTable.shortDescription] = shortDescription
            statement[CourseTable.longDescription] = longDescription
            statement[CourseTable.dateCreate] = dateCreate
            statement[CourseTable.avatar] = (avatar as? Image.ImageResource)?.src ?: ""
            statement[CourseTable.color] = (background as? Image.ImageResource)?.src ?: ""
        }
    }
}
