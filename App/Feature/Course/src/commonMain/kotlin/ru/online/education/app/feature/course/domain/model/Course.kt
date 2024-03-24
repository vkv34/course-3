package ru.online.education.app.feature.course.domain.model

import kotlinx.datetime.LocalDateTime
import model.Image
import onlineeducation.app.feature.course.generated.resources.Res

data class Course(
    val id: Int = 0,
    val name: String ="",
    val shortDescription: String = "",
    val longDescription: String = "",
    val dateCreate: LocalDateTime = LocalDateTime(2024, 1, 1, 1, 1),
    val avatar: Image = Image.default,
    val background: Image = Image.default,
    val courseCategory: String = "",
    val courseCreator: String = ""
    )