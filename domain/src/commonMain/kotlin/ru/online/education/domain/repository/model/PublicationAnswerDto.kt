package ru.online.education.domain.repository.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class PublicationAnswerDto(
    val id: Int = 0,
    val userDto: UserDto? = null,
    val userId: Int? = null,
    val teacher: UserDto? = null,
    val answer: String = "",
    val comment: String? = null,
    val mark: Byte? = null,
    val publicationOnCourseId: Int = 0,
    val dateCreate: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

): BaseModel()