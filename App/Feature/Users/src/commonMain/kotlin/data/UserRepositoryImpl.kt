package data

import domain.NotificationManager
import io.ktor.client.*
import ru.online.education.domain.repository.model.ListResponse
import ru.online.education.domain.repository.model.UserDto
import ru.online.education.domain.repository.UserRepository
import ru.online.education.app.core.util.ktorUtil.safeGet
import util.ApiResult

class UserRepositoryImpl(
    private val client: HttpClient,
    private val notificationManager: NotificationManager
) : UserRepository {
    override suspend fun findUserByEmail(user: UserDto): UserDto? {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(page: Int): ApiResult<ListResponse<UserDto>> = client.safeGet(
        path = "/user/all/$page",
        notificationManager
    )

    override suspend fun getById(id: Int): ApiResult<UserDto> =
        client.safeGet<UserDto>("/user/$id", notificationManager = notificationManager)

    override suspend fun deleteById(id: Int): ApiResult<UserDto> {
        TODO("Not yet implemented")
    }

    override suspend fun update(data: UserDto): ApiResult<UserDto?> {
        TODO("Not yet implemented")
    }

    override suspend fun add(data: UserDto): ApiResult<UserDto> {
        TODO("Not yet implemented")
    }
}