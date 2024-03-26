package domain.mapper

import model.Publication
import model.PublicationDto

fun PublicationDto.toPublication(
    author: String,
    category: String = ""
) = Publication(
    id = publicationId,
    title = title,
    subTitle = subTitle,
    content = content,
    type = type,
    visible = visible,
    temp = temp,
    author = author
)