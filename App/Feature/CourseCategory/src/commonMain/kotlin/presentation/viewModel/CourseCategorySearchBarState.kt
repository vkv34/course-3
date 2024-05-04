package presentation.viewModel

import androidx.compose.runtime.Stable
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import app.cash.paging.createPager
import app.cash.paging.createPagingConfig
import domain.CourseCategorySearchPager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import model.CourseCategory
import ru.online.education.domain.repository.CourseCategoryRepository
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import kotlin.time.Duration.Companion.milliseconds

@Stable
class CourseCategorySearchBarState(
    private val courseCategoryRepository: CourseCategoryRepository,
    private val coroutineScope: CoroutineScope
) {
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _selectedCourseCategory = MutableStateFlow<CourseCategory?>(null)
    val selectedCourseCategory = _selectedCourseCategory.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val courseCategorySearchPager = query
        .debounce(500.milliseconds)
        .flatMapLatest { search ->
            createPager(
                config = createPagingConfig(
                    pageSize = courseCategoryRepository.pageSize,
                )
            ) {
                CourseCategorySearchPager(
                    query = search,
                    source = { searchString, page -> courseCategoryRepository.findByName(searchString, page) }
                )
            }.flow
                .cachedIn(coroutineScope)
                .flowOn(DispatcherProvider.IO)
        } as Flow<PagingData<CourseCategory>>

    fun search(query: String) {
        _query.value = query
    }

    fun selectCategory(category: CourseCategory) {
        _selectedCourseCategory.value = category
        _query.value = category.name
    }
}