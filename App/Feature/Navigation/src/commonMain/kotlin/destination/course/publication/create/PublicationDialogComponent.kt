package destination.course.publication.create

import com.arkivanov.decompose.ComponentContext
import domain.AddPublicationScreenState
import domain.AttachmentListState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class PublicationDialogComponent(
    componentContext: ComponentContext,
    private val courseId: Int,
    coroutineScope: CoroutineScope,
    private val onDismiss: () -> Unit
) : ComponentContext by componentContext, KoinComponent {

    private val job = SupervisorJob()
    private val scope = coroutineScope + job

    val dialogState = AddPublicationScreenState(
        publicationRepository = get(),
//        publicationOnCourseRepository = get(),
        userRepository = get(),
        coroutineScope = scope,
        onDismiss = {
            onDismiss()
            scope.cancel()
        },
        courseId = courseId
    )

    fun openPublication(publicationId: Int){
        dialogState.setPublicationId(publicationId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val attachmentListState = AttachmentListState(
        attachmentRepository = get(),
        publicationIdFlow = dialogState.publication.map { it.id },
        coroutineScope = scope
    )
}