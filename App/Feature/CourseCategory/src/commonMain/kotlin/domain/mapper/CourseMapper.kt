package domain.mapper

import model.CourseCategory
import model.CourseCategoryDto
import model.CourseDto

fun CourseCategoryDto.toCourseCategory() = CourseCategory(
    id = id,
    name = name,
    description = description
)