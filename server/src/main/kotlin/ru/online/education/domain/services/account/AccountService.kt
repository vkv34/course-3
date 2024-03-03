package ru.online.education.domain.services.account

import model.AuthResponse
import repository.AccountRepository
import util.ApiResult

class AccountService(
    val accountRepository: AccountRepository,

) {

    suspend fun loginByEmailAndPassword(login: CharSequence, password: CharSequence, hostName: String): ApiResult<AuthResponse> =
        accountRepository.loginByEmailAndPassword(login, password, hostName)

    suspend fun getTestAdminAccount() {
        accountRepository
    }


}