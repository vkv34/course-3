package ru.online.education.app.feature.account.presentation.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.online.education.domain.repository.AccountRepository
import ru.online.education.domain.repository.model.UserDto
import ru.online.education.domain.repository.model.UserRole


/**
 * ViewModel для авторизации и регистрации пользователя.
 */
class SignInViewModel(
    val repository: AccountRepository,
    private val coroutineScope: CoroutineScope
) {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    /**
     * Устанавливает имя пользователя.
     *
     * @param name имя пользователя
     */
    fun setName(name: String) {
        coroutineScope.launch {
            _uiState.update {
                it.copy(
                    name = name
                )
            }
            checkName(name)
        }
    }

    /**
     * Устанавливает фамилию пользователя.
     *
     * @param surname фамилия пользователя
     */
    fun setSurname(surname: String) {
        coroutineScope.launch {
            _uiState.update {
                it.copy(
                    surname = surname
                )
            }
            checkSurname(surname)
        }
    }

    /**
     * Устанавливает отчество пользователя.
     *
     * @param lastname отчество пользователя
     */
    fun setLastname(lastname: String) {
        coroutineScope.launch {
            _uiState.update {
                it.copy(
                    lastName = lastname
                )
            }
        }
    }


    /**
     * Устанавливает пароль пользователя.
     *
     * @param password пароль пользователя
     */
    fun setPassword(password: String) {
        coroutineScope.launch {
            _uiState.update {
                it.copy(
                    password = password
                )
            }
            checkPassword(password)
        }
    }

    /**
     * Устанавливает подтверждение пароля пользователя.
     *
     * @param password подтверждение пароля пользователя
     */
    fun setConfirmPassword(password: String) {
        coroutineScope.launch {
            _uiState.update {
                it.copy(
                    confirmPassword = password
                )
            }
            comparePasswords(_uiState.value.password, password)
        }
    }

    /**
     * Устанавливает электронную почту пользователя.
     *
     * @param email электронная почта пользователя
     */
    fun setEmail(email: String) {
        if (email.isBlank())
            _uiState.update {
                it.copy(
                    emailError = "Поле почта не может быть пустым",
                    isEmailError = true,
                    email = email
                )
            }
        else if (!Regex("^[\\w.-]+@[\\w.-]+\\.\\w+\$").matches(email)) {
            _uiState.update {
                it.copy(
                    emailError = "Почта не соответсвует формату",
                    isEmailError = true,
                    email = email
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    emailError = "",
                    isEmailError = false,
                    email = email
                )
            }
        }
    }


    /**
     * Проверяет пароль пользователя.
     *
     * @param password пароль пользователя
     */
    private fun checkPassword(password: String) {
        if (password.length < 6) {
            _uiState.update {
                it.copy(
                    isPasswordError = true,
                    passwordErrorValue = "Длина пароля должна быть не меньше 6 символов"
                )
            }
        } else if (
            !Regex("^(?=.*[A-Z])(?=.*[!@#\$%^&*])(?=.*\\d)[A-Za-z\\d!@#\$%^&*]+\$").matches(password)
        ) {
            _uiState.update {
                it.copy(
                    isPasswordError = true,
                    passwordErrorValue = "Пароль может состоять только из латинских букв и цифр"
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isPasswordError = false,
                    passwordErrorValue = ""
                )
            }
        }
    }

    /**
     * Сравнивает пароль пользователя с подтверждением пароля.
     *
     * @param password         пароль пользователя
     * @param confirmPassword подтверждение пароля пользователя
     */
    private fun comparePasswords(password: String, confirmPassword: String) {
        if (password == confirmPassword) {
            _uiState.update {
                it.copy(
                    isConfirmPasswordError = false,
                    confirmPasswordErrorValue = ""
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isConfirmPasswordError = true,
                    confirmPasswordErrorValue = "Пароли должны совпадать"
                )
            }
        }
    }

    /**
     * Проверяет имя пользователя.
     *
     * @param name имя пользователя
     */
    private fun checkName(name: String) {
        if (name.isEmpty()) {
            _uiState.update {
                it.copy(
                    isNameError = true,
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isNameError = false,
                )
            }
        }
    }

    /**
     * Проверяет фамилию пользователя.
     *
     * @param surname фамилия пользователя
     */
    private fun checkSurname(surname: String) {
        if (surname.isEmpty()) {
            _uiState.update {
                it.copy(
                    isSurnameError = true,
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isSurnameError = false,
                )
            }
        }
    }

    private fun UiState.isValid(): Boolean =
        !(isNameError or
                isSurnameError or
                isPasswordError or
                isConfirmPasswordError)

//    /**
//     * Проверяет валидность всех полей и выполняет вход пользователя.
//     *
//     * @param onSuccess функция обратного вызова, вызывается при успешной аутентификации
//     */
//    fun signIn(onSuccess: (user: User) -> Unit) {
//        _uiState.update {
//            it.copy(
//                isLoading = true
//            )
//        }
//        coroutineScope.launch {
//
//            with(_uiState.value) {
//                checkLogin(login)
//                checkPassword(password)
//                comparePasswords(password, confirmPassword)
//                checkName(name)
//                checkSurname(surname)
//            }
//            if (_uiState.value.isValid()) {
//
//                val user = with(_uiState.value) {
//                    User(
//                        login = login,
//                        password = password
//                    )
//                }
//                val person = with(_uiState.value) {
//                    Person(
//                        name = name,
//                        secondName = surname,
//                        middleName = lastName,
//                        email = email
//                    )
//                }
//
//                coroutineScope.launch {
//                    repository.signIn(
//                        user,
//                        person
//                    ).collect { apiResult ->
//                        if (apiResult is ApiResult.Success) {
//                            onSuccess.invoke(user)
//                        }
//                        _uiState.update {
//                            it.copy(
//                                isLoading = false,
//                                apiSignInState = apiResult
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }

    fun registerTeacher(onSuccess: (UserDto) -> Unit) {
        val state = uiState.value
        if (state.isValid()) {
            with(state) {
                coroutineScope.launch {
                    val result = repository.createAccount(
                        UserDto(
                            email = email,
                            firstName = name,
                            secondName = surname,
                            lastName = lastName,
                            password = password,
                            role = UserRole.Admin
                        )
                    )
                    if (result.successOrNull() == null) {
                        _uiState.update {
                            it.copy(
                                error = result.message
                            )
                        }
                    } else {
                        onSuccess(result.successOrNull()!!)
                    }

                }
            }

        }

    }

    fun registerStudent(onSuccess: (UserDto) -> Unit) {
        val state = uiState.value
        if (state.isValid()) {
            with(state) {
                coroutineScope.launch {
                    val result = repository.createAccount(
                        UserDto(
                            email = email,
                            firstName = name,
                            secondName = surname,
                            lastName = lastName,
                            password = password,
                            role = UserRole.Student
                        )
                    )
                    if (result.successOrNull() == null) {
                        _uiState.update {
                            it.copy(
                                error =
                                if (result.message.lowercase().contains("duplicate")) {
                                    "Пользователь с такой почтой уже существует"
                                } else {
                                    result.message
                                }
                            )
                        }
                    } else {
                        onSuccess(result.successOrNull()!!)
                    }
                }
            }

        }

    }

    /**
     * Состояние пользовательского интерфейса.
     *
     * @param name                       имя пользователя
     * @param isNameError                флаг, указывающий на наличие ошибки в поле имени
     * @param nameError                  сообщение об ошибке в поле имени
     * @param surname                    фамилия пользователя
     * @param isSurnameError             флаг, указывающий на наличие ошибки в поле фамилии
     * @param surnameError               сообщение об ошибке в поле фамилии
     * @param lastName                   отчество пользователя
     * @param login                      логин пользователя
     * @param isLoginError               флаг, указывающий на наличие ошибки в поле логина
     * @param loginErrorValue            сообщение об ошибке в поле логина
     * @param password                   пароль пользователя
     * @param isPasswordError            флаг, указывающий на наличие ошибки в поле пароля
     * @param passwordErrorValue         сообщение об ошибке в поле пароля
     * @param confirmPassword           подтверждение пароля пользователя
     * @param isConfirmPasswordError     флаг, указывающий на наличие ошибки в поле подтверждения пароля
     * @param confirmPasswordErrorValue  сообщение об ошибке в поле подтверждения пароля
     * @param email                      электронная почта пользователя
     * @param isEmailError               флаг, указывающий на наличие ошибки в поле электронной почты
     * @param emailError                 сообщение об ошибке в поле электронной почты
     * @param isLoading                  флаг, указывающий на выполнение операции загрузки
     * @param apiSignInState             состояние аутентификации API
     */
    data class UiState(
        val name: String = "",
        val isNameError: Boolean = false,
        val nameError: String = "Поле имя не может быть пустым",

        val surname: String = "",
        val isSurnameError: Boolean = false,
        val surnameError: String = "Поле фамилия не может быть пустым",

        val lastName: String = "",

        val password: String = "",
        val isPasswordError: Boolean = false,
        val passwordErrorValue: String = "",

        val confirmPassword: String = "",
        val isConfirmPasswordError: Boolean = false,
        val confirmPasswordErrorValue: String = "",

        val email: String = "",
        val emailError: String = "",
        val isEmailError: Boolean = false,

        val isLoading: Boolean = false,

        val error: String = ""
    )

}
