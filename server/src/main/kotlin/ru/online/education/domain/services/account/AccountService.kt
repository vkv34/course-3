package ru.online.education.domain.services.account

import ru.online.education.domain.repository.model.AuthResponse
import ru.online.education.domain.repository.AccountRepository
import util.ApiResult

class AccountService(
    val accountRepository: AccountRepository,
) {
    suspend fun loginByEmailAndPassword(
        login: CharSequence,
        password: CharSequence,
        hostName: String,
    ): ApiResult<AuthResponse> = accountRepository.loginByEmailAndPassword(login, password, hostName)

    suspend fun getTestAdminAccount() {
        accountRepository
    }
}
