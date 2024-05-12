package destination.course.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.doOnDestroy
import destination.course.list.create.CreateCourseDialogComponent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.feature.course.presentation.viewModel.AllCoursesScreenViewModel
import ru.online.education.domain.repository.UserOnCourseRepository
import ru.online.education.domain.repository.model.UserOnCourseDto
import util.ApiResult

class CourseListComponent(
    componentContext: ComponentContext,
    selectedArticleId: Flow<Long?>,
    private val onCourseSelected: (id: Long) -> Unit
) : ComponentContext by componentContext, KoinComponent {

    private val scope = CoroutineScope(DispatcherProvider.Main + SupervisorJob())

    private val dialogNavigation = SlotNavigation<Unit>()

    val createCourseDialog = childSlot(
        source = dialogNavigation,
        serializer = null,
        handleBackButton = true
    ) { configuration, componentContext ->
        CreateCourseDialogComponent(
            context = componentContext,
            coroutineScope = scope,
            onDismiss = dialogNavigation::dismiss,
            onSuccess = {
                dialogNavigation.dismiss()
                allCoursesViewModel.refresh()
            },
        )
    }

    init {
        lifecycle.doOnDestroy {
            scope.cancel(cause = CancellationException("CourseListComponent destroyed"))
        }
    }

    val allCoursesViewModel = AllCoursesScreenViewModel(
        courseRepository = get(),
        coroutineScope = scope,
        accountRepository = get(),
        userRepository = get(),
        authStore = get()
    )

    fun onCourseClicked(id: Long) {
        onCourseSelected(id)
    }

    fun openCreateDialog() {
        dialogNavigation.activate(Unit)
    }

    val userOnCourseRepository by inject<UserOnCourseRepository>()

    val addError = MutableValue("")
    val loading = MutableValue(false)

    fun tryToJoinCourse(code: Int) {
        scope.launch(DispatcherProvider.IO) {
            withContext(DispatcherProvider.Main){
                loading.value = true
            }
            val result = userOnCourseRepository.add(
                UserOnCourseDto(
                    courseId = code
                )
            )
            if (result is ApiResult.Success){
                allCoursesViewModel.refresh()
            }else{
                withContext(DispatcherProvider.Main){
                    addError.value = result.message
                }
            }

            withContext(DispatcherProvider.Main){
                loading.value = false
            }
        }
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
