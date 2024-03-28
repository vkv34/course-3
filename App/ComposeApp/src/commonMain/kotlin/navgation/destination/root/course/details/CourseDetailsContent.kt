package navgation.destination.root.course.details

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.sample.shared.multipane.details.CourseDetailsComponent
import domain.PublicationScreenState
import presentation.EditPublicationDialog
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
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .debbugable()
        ) {
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
            PublicationListScreen(state = screenState)


        }

        FloatingActionButton(
            onClick = context::openAddPublicationDialog,
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
        ){
            Icon(
                imageVector = Icons.Default.PostAdd,
                contentDescription = null
            )
        }
    }

    val addingDialog by context.publicationDialogChild.subscribeAsState()

    addingDialog.child?.instance?.also { dialog ->
        EditPublicationDialog(
            publicationState = dialog.dialogState,
            onDismiss = context::closeAddPublicationDialog
        )
    }
}