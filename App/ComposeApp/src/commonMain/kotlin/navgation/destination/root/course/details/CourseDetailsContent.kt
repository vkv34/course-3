package navgation.destination.root.course.details

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.sample.shared.multipane.details.CourseDetailsComponent
import domain.PublicationScreenState
import presentation.PublicationListScreen
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.core.util.model.ApiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsContent(
    context: CourseDetailsComponent
) {
    val publicationComponent = context.publicationComponent
    val screenState = PublicationScreenState(
        publicationRepository = publicationComponent.publicationRepository,
        userRepository = publicationComponent.userRepository,
        courseId = context.courseId.toInt(),
        scope = context.coroutineScope
    )
    val currentCourse by context.currentCourse.subscribeAsState()
    val isToolBarVisible by context.isToolbarVisible.collectAsState(initial = null)
    Column(
        modifier = Modifier.fillMaxSize()
            .debbugable()
    ) {
        if (isToolBarVisible == true) {
            TopAppBar(
                title = {
                    Crossfade(
                        currentCourse,
                        modifier = Modifier
                            .animateContentSize()
                            .debbugable()
                    ) { course ->
                        when (course) {
                            is ApiState.Default -> Unit
                            is ApiState.Error -> Unit
                            is ApiState.Loading -> CircularProgressIndicator(
                                modifier = Modifier.debbugable()
                            )

                            is ApiState.Success -> Text(
                                text = course.data.name,
                                modifier = Modifier.debbugable()
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = context::onCloseClicked) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.debbugable()
            )
        }
        PublicationListScreen(state = screenState)


    }
}