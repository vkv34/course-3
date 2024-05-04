package ru.online.education.app.feature.course.domain.model

import kotlinx.datetime.LocalDateTime
import ru.online.education.domain.repository.model.Image

data class Course(
    val id: Int = 0,
    val name: String = "",
    val shortDescription: String = "",
    val longDescription: String = "",
    val dateCreate: LocalDateTime = LocalDateTime(2024, 1, 1, 1, 1),
    val avatar: Image = Image.default,
    val background: Image = Image.default,
    val courseCategory: String = "",
    val courseCreator: String = "",
)