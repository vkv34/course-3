package ru.online.education.app.feature.course.presentation.viewModel

import androidx.compose.runtime.Stable
import app.cash.paging.Pager
import app.cash.paging.cachedIn
import app.cash.paging.createPagingConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.UserRole
import repository.AccountRepository
import repository.CourseRepository
import ru.online.education.app.feature.course.domain.repository.CoursePagingSource
import util.ApiResult

@Stable
class AllCoursesScreenViewModel(
    private val courseRepository: CourseRepository,
    private val accountRepository: AccountRepository,
    val coroutineScope: CoroutineScope
) {
    data class ScreenState(
        val canEdit: Boolean = false
    )

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    //    private val _courseResult: MutableStateFlow<ApiState<ListResponse<CourseDto>>> =
//        MutableStateFlow(ApiState.Default())
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
//        fetchCourses()
        checkCanEdit()
    }


//    fun fetchCourses() {
//        coroutineScope.launch(DispatcherProvider.IO) {
//            _courseResult.update { ApiState.Loading() }
//            _courseResult.update { courseRepository.getAll(0).toApiState() }
//        }
//    }

    fun checkCanEdit() = coroutineScope.launch {
        val result = accountRepository.currentUser()
        val canEdit = result is ApiResult.Success && result.data.role == UserRole.Admin
        _screenState.update { it.copy(canEdit = canEdit) }
    }

}