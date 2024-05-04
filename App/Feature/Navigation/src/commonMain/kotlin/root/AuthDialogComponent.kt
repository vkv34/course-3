package root

import androidx.compose.runtime.currentRecomposeScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.online.education.domain.repository.AccountRepository
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.feature.account.presentation.model.AuthScreenState

class AuthDialogComponent(
    private val context: ComponentContext,
    private val onDissmissed: () -> Unit
) : ComponentContext by context, KoinComponent {

    private val accountRepository by inject<AccountRepository>()
    val authScreenState = AuthScreenState(
        accountRepository = accountRepository,
        scope = CoroutineScope(DispatcherProvider.IO + SupervisorJob()),
        onSuccess = {
            onDissmiss()
        }
    )

    fun onDissmiss() {
        onDissmissed()
    }
}