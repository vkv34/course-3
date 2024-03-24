package destination.course

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.children.*
import com.arkivanov.decompose.router.children.ChildNavState.Status
import com.arkivanov.decompose.value.Value
import com.arkivanov.sample.shared.multipane.MultiPaneComponent
import com.arkivanov.sample.shared.multipane.MultiPaneComponent.Children
import com.arkivanov.sample.shared.multipane.details.CourseDetailsComponent
import destination.course.list.CourseListComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable

internal class DefaultMultiPaneComponent(
    componentContext: ComponentContext
) : MultiPaneComponent, ComponentContext by componentContext {

    //    private val database = DefaultArticleDatabase()
    private val navigation = SimpleNavigation<(NavigationState) -> NavigationState>()
    private val navState = MutableStateFlow<NavigationState?>(null)

    override val children: Value<Children> =
        children(
            source = navigation,
            stateSerializer = NavigationState.serializer(),
            key = "children",
            initialState = DefaultMultiPaneComponent::NavigationState,
            navTransformer = { navState, event -> event(navState) },
            stateMapper = { navState, children ->
                @Suppress("UNCHECKED_CAST")
                Children(
                    isMultiPane = navState.isMultiPane,
                    listChild = children.first { it.instance is CourseListComponent } as Child.Created<*, CourseListComponent>,
                    detailsChild = children.find { it.instance is CourseDetailsComponent } as Child.Created<*, CourseDetailsComponent>?,
                )
            },
            onStateChanged = { newState, _ -> navState.update { newState } },
            childFactory = ::child,
        )

    private fun child(config: Config, componentContext: ComponentContext): Any =
        when (config) {
            is Config.List -> listComponent(componentContext)
            is Config.Details -> detailsComponent(config, componentContext)
        }

    private fun listComponent(componentContext: ComponentContext): CourseListComponent =
        CourseListComponent(
            componentContext = componentContext,
            selectedArticleId = navState.filterNotNull().map { it.articleId },
            onCourseSelected = { id -> navigation.navigate { it.copy(articleId = id) } },
        )

    private fun detailsComponent(config: Config.Details, componentContext: ComponentContext): CourseDetailsComponent =
        CourseDetailsComponent(
            componentContext = componentContext,
            articleId = config.articleId,
            isToolbarVisible = navState.filterNotNull().map { it.isMultiPane },
            onFinished = { navigation.navigate { it.copy(articleId = null) } },
        )

    override fun setMultiPane(isMultiPane: Boolean) {
        navigation.navigate { it.copy(isMultiPane = isMultiPane) }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object List : Config

        @Serializable
        data class Details(val articleId: Long) : Config
    }

    @Serializable
    private data class NavigationState(
        val isMultiPane: Boolean = false,
        val articleId: Long? = null,
    ) : NavState<Config> {
        override val children: List<ChildNavState<Config>> by lazy {
            listOfNotNull(
                SimpleChildNavState(
                    Config.List,
                    if (isMultiPane || (articleId == null)) Status.STARTED else Status.RESUMED
                ),
                if (articleId != null) SimpleChildNavState(Config.Details(articleId), Status.RESUMED) else null,
            )
        }
    }
}
