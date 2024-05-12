package ru.online.education.app.feature.account.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.online.education.app.feature.account.presentation.model.SignInViewModel
import ru.online.education.domain.repository.model.UserDto
import util.ApiResult


/**
 *
 * Экран авторизации
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    onSuccess: (UserDto) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
//            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "",
            style = MaterialTheme.typography.displayLarge
        )
        OutlineTextFieldWithValidation(
            modifier = Modifier
                .padding(16.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            value = uiState.surname,
            onValueChange = {
                viewModel.setSurname(it)
            },
            isError = uiState.isSurnameError,
            label = {
                Text(text = "Фамилия")
            },
            errorMsg = uiState.surnameError
        )
        OutlineTextFieldWithValidation(
            modifier = Modifier
                .padding(16.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            value = uiState.name,
            onValueChange = {
                viewModel.setName(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            isError = uiState.isNameError,
            label = {
                Text(text = "Имя")
            },
            errorMsg = uiState.nameError
        )
        OutlineTextFieldWithValidation(
            modifier = Modifier
                .padding(16.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            value = uiState.lastName,
            onValueChange = {
                viewModel.setLastname(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            isError = false,
            label = {
                Text(text = "Отчество")
            },
        )


        OutlineTextFieldWithValidation(
            modifier = Modifier
                .padding(16.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            value = uiState.email,
            onValueChange = {
                viewModel.setEmail(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = uiState.isEmailError,
            label = {
                Text(text = "Почта")
            },
            errorMsg = uiState.emailError
        )

        PasswordField(
            value = uiState.password,
            modifier = Modifier
                .padding(16.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            isError = uiState.isPasswordError,
            onValueChange = {
                viewModel.setPassword(it)
            })
        if (uiState.isPasswordError) {
            Text(
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
                text = uiState.passwordErrorValue
            )
        }
        PasswordField(
            value = uiState.confirmPassword,
            labelText = "Подтвердите пароль",
            isError = uiState.isConfirmPasswordError,
            modifier = Modifier
                .padding(16.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            onValueChange = {
                viewModel.setConfirmPassword(it)
            })
        if (uiState.isConfirmPasswordError) {
            Text(
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
                text = uiState.confirmPasswordErrorValue
            )
        }
        if (uiState.error.isNotEmpty()) {
            Text(
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
                text = uiState.error
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            TextButton(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(20.dp, 0.dp, 16.dp, 0.dp),
                onClick = {
                    viewModel.registerStudent (
                        onSuccess = onSuccess
                    )
                }) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                }
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Создать аккаунт студента",
                    style = MaterialTheme.typography.labelLarge
                )
            }
            TextButton(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(20.dp, 0.dp, 16.dp, 0.dp),
                onClick = {
                    viewModel.registerTeacher(
                        onSuccess = onSuccess
                    )
                }) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                }
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Создать аккаунт преподавателя",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
