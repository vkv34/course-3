package ru.online.education.app.feature.account.presentation.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.online.education.app.core.util.api.toApiState
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.core.util.model.ApiState
import ru.online.education.app.feature.account.domain.model.Auth
import ru.online.education.domain.repository.AccountRepository
import util.ApiResult

class AuthScreenState(
    private val accountRepository: AccountRepository,
    private val scope: CoroutineScope,
    private val onSuccess: () -> Unit
) {
    private val _authState = MutableStateFlow(Auth())
    val authState = _authState.asStateFlow()

    val currentScreen = MutableStateFlow(0)

    val signInViewModel = SignInViewModel(repository = accountRepository, coroutineScope = scope)

    private val _authResult = MutableStateFlow<ApiState<AuthResult>>(ApiState.Default())
    val authResult = _authResult.asStateFlow()

    fun setEmail(email: String) {
        _authState.update { it.copy(email = email) }
    }

    fun setPassword(password: String) {
        _authState.update { it.copy(password = password) }
    }

    fun auth() {
        scope.launch(DispatcherProvider.IO) {
            val result = with(_authState.value) {
                accountRepository.loginByEmailAndPassword(
                    login = email,
                    password = password,
                    hostName = ""
                )
            }

            _authResult.update { result.toApiState { AuthResult(it.token) } }

            if (result is ApiResult.Success) {
                _authState.update { it.copy(error = "") }
                withContext(DispatcherProvider.Main) { onSuccess() }
            } else {
                _authState.update { it.copy(error = result.message) }
            }
        }
    }
}