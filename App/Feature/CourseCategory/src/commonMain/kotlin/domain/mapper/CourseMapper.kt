package domain.mapper

import model.CourseCategory
import ru.online.education.domain.repository.model.CourseCategoryDto
import ru.online.education.domain.repository.model.CourseDto

fun CourseCategoryDto.toCourseCategory() = CourseCategory(
    id = id,
    name = name,
    description = description
)