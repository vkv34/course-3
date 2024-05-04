package ru.online.education.domain.services.userOnCourse

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.update
import ru.online.education.core.exception.SelectExeption
import ru.online.education.core.util.apiCall
import ru.online.education.core.util.dbCall
import ru.online.education.data.table.CourseTable
import ru.online.education.data.table.PublicationAttachmentTable
import ru.online.education.data.table.UserOnCourse
import ru.online.education.data.table.UsersTable
import ru.online.education.di.dbQuery
import ru.online.education.domain.repository.UserOnCourseRepository
import ru.online.education.domain.repository.UserRepository
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.UserOnCourseDto
import ru.online.education.domain.repository.model.UserRole
import util.ApiResult
import util.map

class UserOnCourseRepositoryImpl(
    private val userRepository: UserRepository
) : UserOnCourseRepository {
    override suspend fun getUsersOnCourseByCourseId(
        courseId: Int,
        page: Int
    ): ApiResult<ListResponse<UserOnCourseDto>> = dbCall {
        ListResponse(
            dbQuery {
                UserOnCourse
                    .selectAll()
                    .where { UserOnCourse.course eq courseId }
//                    .sortedBy { UserOnCourse.role }
                    .orderBy(UserOnCourse.role)
                    .limit(n = pageSize, offset = pageSize * page.toLong())
//                    .groupBy(UserOnCourse.role)
                    .suspendMap { it.toUserOnCourse() }

            },
        )
    }

    override suspend fun getAll(page: Int): ApiResult<ListResponse<UserOnCourseDto>> = dbCall {
        ListResponse(
            dbQuery {
                UsersTable
                    .selectAll()
//                    .where { UserOnCourse.course eq courseId }
//                    .sortedBy { UserOnCourse.role }
                    .orderBy(UserOnCourse.role)
                    .limit(n = pageSize, offset = pageSize * page.toLong())
//                    .groupBy(UserOnCourse.role)
                    .suspendMap { it.toUserOnCourse() }

            },
        )
    }

    override suspend fun getById(id: Int): ApiResult<UserOnCourseDto> = dbCall {
        dbQuery {
            UserOnCourse
                .selectAll()
                .where { UserOnCourse.id eq id }
                .singleOrNull()
                ?.toUserOnCourse()
                ?: throw SelectExeption("пользователя на курсе с id $id не найдено")
        }
    }

    override suspend fun deleteById(id: Int): ApiResult<UserOnCourseDto> {
        val userOnCourseDtoApiResult = getById(id)
        if (userOnCourseDtoApiResult !is ApiResult.Success) {
            return userOnCourseDtoApiResult
        }
        dbQuery {
            UserOnCourse
                .deleteWhere { UserOnCourse.id eq id }
        }

        return ApiResult.Success(userOnCourseDtoApiResult.data)
    }

    override suspend fun update(data: UserOnCourseDto): ApiResult<UserOnCourseDto?> = apiCall {
        dbQuery {
            UserOnCourse.update(where = { UserOnCourse.id eq data.id }) { updateStatement ->
                data.toUpdateStatement(statement = updateStatement)
            }
        }
        getById(data.id)
    }.map { it }

    override suspend fun add(data: UserOnCourseDto): ApiResult<UserOnCourseDto> {
        TODO("Not yet implemented")
    }

    private suspend fun resultRowToUserOnCourse(row: ResultRow) =
        row.toUserOnCourse()

    private suspend fun ResultRow.toUserOnCourse() =
        UserOnCourseDto(
            id = this[UserOnCourse.id].value,
            courseId = this[UserOnCourse.course].value,
            userDto = userRepository.getById(this[UserOnCourse.user].value).successOrNull()!!,
            role = UserRole.all[this[UserOnCourse.role].value.dec()]
        )

    private fun <T : Any> UserOnCourseDto.toInsertStatement(
        statement: InsertStatement<T>,
    ) {
        val userId = EntityID(userDto.id, UsersTable)
        val courseId = EntityID(courseId, CourseTable)
        statement[UserOnCourse.user] = userId
        statement[UserOnCourse.course] = courseId
        statement[UserOnCourse.role] = UserRole.all.indexOf(role) + 1
    }

    private fun UserOnCourseDto.toUpdateStatement(
        statement: UpdateStatement,
    ) {
        val userId = EntityID(userDto.id, UsersTable)
        val courseId = EntityID(courseId, CourseTable)
        statement[UserOnCourse.user] = userId
        statement[UserOnCourse.course] = courseId
        statement[UserOnCourse.role] = UserRole.all.indexOf(role) + 1
    }

    private suspend fun <T, R> Iterable<T>.suspendMap(suspendTransform: suspend (T) -> R): List<R> =
        coroutineScope {
            map { async { suspendTransform(it) } }
                .awaitAll()
        }
}