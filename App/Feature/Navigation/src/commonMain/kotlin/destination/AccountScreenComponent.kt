package destination

import com.arkivanov.decompose.ComponentContext
import domain.SignInViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.online.education.app.core.util.coruotines.DispatcherProvider

class AccountScreenComponent(
    private val componentContext: ComponentContext,
    private val coroutineScope: CoroutineScope = CoroutineScope(DispatcherProvider.Default + SupervisorJob())
) : ComponentContext by componentContext, KoinComponent {

    val viewModel = SignInViewModel(
        repository = get(),
        userRepository = get(),
        coroutineScope = coroutineScope
    )
}