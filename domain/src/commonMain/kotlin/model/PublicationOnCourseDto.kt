package model

import kotlinx.serialization.Serializable

@Serializable
data class PublicationOnCourseDto(
    val id: Int = 0,
    val publicationId: Int,
    val courseId: Int,
    val userId: Int,
    val visible: Boolean = true,
    val temp: Boolean = false
) : BaseModel()
