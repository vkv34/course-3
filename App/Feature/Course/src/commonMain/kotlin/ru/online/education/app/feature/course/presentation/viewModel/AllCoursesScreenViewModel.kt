package ru.online.education.app.feature.course.presentation.viewModel

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import model.CourseDto
import repository.CourseRepository
import ru.online.education.app.core.util.model.ApiState

@Stable
class AllCoursesScreenViewModel(
    private val courseRepository: CourseRepository
) {
    
    val _courseResult = MutableStateFlow(ApiState.Default<List<CourseDto>>())
    
}