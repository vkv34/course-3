package destination.course.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.feature.course.presentation.viewModel.AllCoursesScreenViewModel

class CourseListComponent(
    componentContext: ComponentContext,
    selectedArticleId: Flow<Long?>,
    private val onCourseSelected: (id: Long) -> Unit
) : ComponentContext by componentContext, KoinComponent {

    private val scope = CoroutineScope(DispatcherProvider.Main + SupervisorJob())

    init {
        lifecycle.doOnDestroy {
            scope.cancel(cause = CancellationException("CourseListComponent destroyed"))
        }
    }

    val allCoursesViewModel = AllCoursesScreenViewModel(
        courseRepository = get(),
        coroutineScope = scope,
    )

    fun onCourseClicked(id: Long) {
        onCourseSelected(id)
    }
//    private val _models =
//        MutableValue(
//            Model(
//                articles = database.getAll().map { it.toArticle() },
//                selectedArticleId = null
//            )
//        )
//
//    override val models: Value<Model> = _models
//
//    init {
//        selectedArticleId.subscribeScoped { id ->
//            _models.update { it.copy(selectedArticleId = id) }
//        }
//    }
//
//    private fun ArticleEntity.toArticle(): Article =
//        Article(
//            id = id,
//            title = title
//        )
//
//    override fun onArticleClicked(id: Long) {
//        onArticleSelected(id)
//    }
}
