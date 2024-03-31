package ru.online.education.app.feature.course.domain.model.mapper

import model.CourseDto
import ru.online.education.app.feature.course.domain.model.Course

fun CourseDto.toCourse() =
    Course(
        id = id,
        name = name,
        shortDescription = shortDescription,
        longDescription = longDescription,
        dateCreate = dateCreate,
        avatar = avatar,
        background = background,

    )

fun Course.toCourseDto() =
    CourseDto(
        id = id,
        name = name,
        shortDescription = shortDescription,
        longDescription = longDescription,
        dateCreate = dateCreate,
        avatar = avatar,
        background = background
    )