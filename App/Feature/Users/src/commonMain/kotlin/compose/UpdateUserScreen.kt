package compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import domain.SignInViewModel
import ru.online.education.domain.repository.model.UserDto

@Composable
fun UpdateUserScreen(
    viewModel: SignInViewModel,
    onSuccess: (UserDto) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit){
        viewModel.reload()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
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

        var success by remember { mutableStateOf(false) }

        AnimatedVisibility(success) {
            Text(
                text = "Успешно",
                color = MaterialTheme.colorScheme.primary
            )
        }

        TextButton(
            modifier = Modifier
//                .weight(0.5f)
                .padding(20.dp, 0.dp, 16.dp, 0.dp),
//            enabled = uiState.error,
            onClick = {
                viewModel.updateUser(
                    onSuccess = {
                        onSuccess(it)
                        success = true

                    }
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
                text = "Изменить данные",
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(Modifier.height(52.dp))
    }
}