package ru.online.education.app.feature.course.presentation.viewModel

import androidx.compose.runtime.Stable
import app.cash.paging.Pager
import app.cash.paging.cachedIn
import app.cash.paging.createPagingConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.CourseDto
import model.ListResponse
import repository.CourseRepository
import ru.online.education.app.core.util.api.toApiState
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.core.util.model.ApiState
import ru.online.education.app.feature.course.domain.repository.CoursePagingSource

@Stable
class AllCoursesScreenViewModel(
    private val courseRepository: CourseRepository,
    val coroutineScope: CoroutineScope
) {

    private val _courseResult: MutableStateFlow<ApiState<ListResponse<CourseDto>>> =
        MutableStateFlow(ApiState.Default())
    val courses = Pager(
        config = createPagingConfig(
            pageSize = courseRepository.pageSize,
        ),
        initialKey = 0,
        pagingSourceFactory = {
            CoursePagingSource(
                source = courseRepository::getAll
            )
        }
    ).flow
        .cachedIn(coroutineScope)

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