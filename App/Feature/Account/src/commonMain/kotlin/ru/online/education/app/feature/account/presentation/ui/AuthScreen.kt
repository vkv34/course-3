package ru.online.education.app.feature.account.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import onlineeducation.app.feature.account.generated.resources.Res
import onlineeducation.app.feature.account.generated.resources.login
import onlineeducation.app.feature.account.generated.resources.register
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import ru.online.education.app.feature.account.presentation.model.AuthScreenState

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AuthScreen(
    authScreenState: AuthScreenState,
    modifier: Modifier = Modifier
) {

    val authState by authScreenState.authState.collectAsState()

    Column {
        OutlinedTextField(
            value = authState.email,
            onValueChange = authScreenState::setEmail,
            isError = authState.error.isNotEmpty(),
            label = {
                Text("Почта")
            }
        )
        OutlinedTextField(
            value = authState.password,
            onValueChange = authScreenState::setPassword,
            isError = authState.error.isNotEmpty(),
            supportingText = {
                if (authState.error.isNotEmpty()) {
                    Text(authState.error)
                }
            },
            label = {
                Text("Пароль")
            },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = authScreenState::auth) {
            Text(/*stringResource(Res.string.login)*/"Войти")
        }
        TextButton(onClick = authScreenState::auth) {
            Text(/*stringResource(Res.string.register)*/"Зарегестрироваться")
        }

//        Text(token)
    }
}

@Preview
@Composable
fun AuthScreenPreview() {
    Text("")
}