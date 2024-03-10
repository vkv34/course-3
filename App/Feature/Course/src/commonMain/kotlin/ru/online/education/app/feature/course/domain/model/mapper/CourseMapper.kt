package ru.online.education.app.feature.course.domain.model.mapper

import model.CourseDto
import ru.online.education.app.feature.course.domain.model.Course

fun CourseDto.toCourse() =
    Course(
        name = name,
        shortDescription = shortDescription,
        longDescription = longDescription,
        dateCreate = dateCreate,
        avatar = avatar,
        background = background
    )