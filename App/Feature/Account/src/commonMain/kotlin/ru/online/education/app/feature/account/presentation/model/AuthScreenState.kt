package ru.online.education.app.feature.account.presentation.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import repository.AccountRepository
import ru.online.education.app.core.util.api.toApiState
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.core.util.model.ApiState
import ru.online.education.app.feature.account.domain.model.Auth
import ru.online.education.app.feature.account.domain.repository.impl.AccountRepositoryImpl
import ru.online.education.app.feature.account.presentation.model.AuthResult

class AuthScreenState(
    private val accountRepository: AccountRepository,
    private val scope: CoroutineScope
) {
    private val _authState = MutableStateFlow(Auth())
    val authState = _authState.asStateFlow()

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
        }
    }
}