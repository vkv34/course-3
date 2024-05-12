package ru.online.education.app.feature.account.presentation.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.online.education.app.feature.account.presentation.model.AuthScreenState

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AuthScreen(
    authScreenState: AuthScreenState,
    modifier: Modifier = Modifier
) {

    val authState by authScreenState.authState.collectAsState()

    val currentScreen by authScreenState.currentScreen.collectAsState()

    Column {
        TabRow(
            selectedTabIndex = currentScreen
        ){
            Tab(
                selected = currentScreen == 0,
                onClick = {
                    authScreenState.currentScreen.update { 0 }
                },
                text = {
                    Text("Вход")
                }
            )
            Tab(
                selected = currentScreen == 1,
                onClick = {
                    authScreenState.currentScreen.update { 1 }
                },
                text = {
                    Text("Регистрация")
                }
            )
        }
        AnimatedContent(currentScreen) {
            if (it == 0) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = authState.email,
                        onValueChange = authScreenState::setEmail,
                        isError = authState.error.isNotEmpty(),
                        label = {
                            Text("Почта")
                        },
                        modifier = Modifier.fillMaxWidth()

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
                        modifier = Modifier.fillMaxWidth(),
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
            } else {
                SignInScreen(
                    viewModel = authScreenState.signInViewModel,
                    onSuccess = {
                        authScreenState.setEmail(email = it.email)
                        authScreenState.setPassword(password = it.password)

                        authScreenState.currentScreen.update { 0 }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AuthScreenPreview() {
    Text("")
}