package destination.course.publication

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.online.education.domain.repository.PublicationRepository
import ru.online.education.domain.repository.UserRepository

@Immutable
class PublicationComponent(
    val context: ComponentContext
): ComponentContext by context, KoinComponent{
    val publicationRepository by inject<PublicationRepository>()
    val userRepository by inject<UserRepository>()

}