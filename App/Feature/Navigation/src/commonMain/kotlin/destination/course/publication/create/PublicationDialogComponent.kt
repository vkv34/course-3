package destination.course.publication.create

import com.arkivanov.decompose.ComponentContext
import domain.AddPublicationScreenState
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class PublicationDialogComponent(
    componentContext: ComponentContext,
    private val courseId: Int,
    private val coroutineScope: CoroutineScope,
    private val onDismiss: () -> Unit
) : ComponentContext by componentContext, KoinComponent {
    val dialogState = AddPublicationScreenState(
        publicationRepository = get(),
//        publicationOnCourseRepository = get(),
        userRepository = get(),
        coroutineScope = coroutineScope,
        onDismiss = onDismiss,
        courseId = courseId
    )
}