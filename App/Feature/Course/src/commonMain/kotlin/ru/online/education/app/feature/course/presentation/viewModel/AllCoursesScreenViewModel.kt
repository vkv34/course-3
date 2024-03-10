package ru.online.education.app.feature.course.presentation.viewModel

import androidx.compose.runtime.Stable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.CourseDto
import model.ListResponse
import repository.CourseRepository
import ru.online.education.app.core.util.api.toApiState
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.core.util.model.ApiState
import util.ApiResult

@Stable
class AllCoursesScreenViewModel(
    private val courseRepository: CourseRepository,
    val coroutineScope: CoroutineScope
) {

    private val _courseResult: MutableStateFlow<ApiState<ListResponse<CourseDto>>> =
        MutableStateFlow(ApiState.Default())
    val courseResult = _courseResult.asStateFlow()

    init {
        fetchCourses()
    }


    fun fetchCourses() {
        coroutineScope.launch(DispatcherProvider.IO) {
            _courseResult.update { ApiState.Loading() }
            _courseResult.update { courseRepository.getAll(0).toApiState() }
        }
    }
}