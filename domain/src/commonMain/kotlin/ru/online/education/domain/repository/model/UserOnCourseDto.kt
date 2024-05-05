package ru.online.education.domain.repository.model

import kotlinx.serialization.Serializable

@Serializable
data class UserOnCourseDto(
    val id: Int = 0,
    val userDto: UserDto = UserDto(),
    val courseId: Int = 0,
    val role: UserRole = UserRole.Student
) : BaseModel()
