package domain.mapper

import model.Publication
import model.PublicationDto

fun PublicationDto.toPublication(
    author: String,
    category: String = ""
) = Publication(
    id = publicationId,
    publicationInCourseId = publicationInCourseId,
    title = title,
    subTitle = subTitle,
    content = content,
    type = type,
    visible = visible,
    temp = temp,
    author = author,
    createdAt = createdAt,
    deadLine = deadLine
)

fun Publication.toPublicationDto() = PublicationDto(
    publicationId = id,
    publicationInCourseId = publicationInCourseId,
    title = title,
    subTitle = subTitle,
    content = content,
    type = type,
    visible = visible,
    temp = temp,
    createdAt = createdAt,
    deadLine = deadLine
)