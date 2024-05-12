package ru.online.education.app.feature.course.presentation.viewModel

import androidx.compose.runtime.Stable
import app.cash.paging.Pager
import app.cash.paging.cachedIn
import app.cash.paging.createPagingConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.online.education.domain.repository.model.UserRole
import ru.online.education.domain.repository.AccountRepository
import ru.online.education.domain.repository.CourseRepository
import ru.online.education.domain.repository.UserRepository
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.feature.account.domain.repository.UserAuthStore
import ru.online.education.app.feature.course.domain.repository.CoursePagingSource
import util.ApiResult
import kotlin.time.Duration.Companion.milliseconds

@Stable
class AllCoursesScreenViewModel(
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository,
    private val authStore: UserAuthStore,
    val coroutineScope: CoroutineScope
) {
    data class ScreenState(
        val canEdit: Boolean = false
    )

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    //    private val _courseResult: MutableStateFlow<ApiState<ListResponse<CourseDto>>> =
//        MutableStateFlow(ApiState.Default())
    private val allCoursesPagingSource = MutableStateFlow(
        CoursePagingSource(
            source = courseRepository::getAll,
            authorSource = {  userRepository.getById(it).successOrNull()?.fio ?: "Автор не указан" }
        )
    )

    val tab = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val courses = allCoursesPagingSource
        .debounce(300.milliseconds)
        .flatMapLatest { pagingSource ->
            Pager(
                config = createPagingConfig(
                    pageSize = courseRepository.pageSize,
                ),
                initialKey = 0,
                pagingSourceFactory = {
                    pagingSource
                }
            ).flow
                .cachedIn(coroutineScope)
        }.flowOn(DispatcherProvider.IO)

    private val allArchivedPagingSource = MutableStateFlow(
        CoursePagingSource(
            source = {
                courseRepository.filterByUser(
                    page = it,
                    archived = true
                )
            },
            authorSource = {  userRepository.getById(it).successOrNull()?.fio ?: "Автор не указан" }
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val archivedCourses = allArchivedPagingSource
        .debounce(300.milliseconds)
        .flatMapLatest { pagingSource ->
            Pager(
                config = createPagingConfig(
                    pageSize = courseRepository.pageSize,
                ),
                initialKey = 0,
                pagingSourceFactory = {
                    pagingSource
                }
            ).flow
                .cachedIn(coroutineScope)
        }.flowOn(DispatcherProvider.IO)

    fun refreshArchivedCourses(){
        allArchivedPagingSource.value =
            CoursePagingSource(
                source = {
                    courseRepository.filterByUser(
                        page = it,
                        archived = true
                    )
                },
                authorSource = {  userRepository.getById(it).successOrNull()?.fio ?: "Автор не указан" }
            )
    }



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

    fun checkCanEdit() = coroutineScope.launch(DispatcherProvider.IO) {
        authStore.readAsFlow().collect{
            val result = accountRepository.currentUser()
            val canEdit = result is ApiResult.Success && result.data.role == UserRole.Admin
            _screenState.update { it.copy(canEdit = canEdit) }

            refresh()
        }
    }

    fun refresh() {
//        checkCanEdit()
        allCoursesPagingSource.value = CoursePagingSource(
            source = courseRepository::getAll,
            authorSource = {
                userRepository.getById(it).successOrNull()?.fio ?: "Автор не указан"
            }
        )
    }

    fun archive(courseId: Int){
        coroutineScope.launch(DispatcherProvider.IO) {
            courseRepository.archiveCourse(
                courseId = courseId,
                archived = tab.value == 0
            )

            refresh()
            refreshArchivedCourses()
        }
    }



}