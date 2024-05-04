package ru.online.education.domain.repository.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class PublicationOnCourseDto(
    val id: Int = 0,
    val publicationId: Int,
    val courseId: Int,
    val userId: Int,
    val visible: Boolean = true,
    val temp: Boolean = false,

    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val deadLine: LocalDateTime? = null


) : BaseModel()
