package ru.online.education.app.feature.account.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
        TextField(authState.email, onValueChange = authScreenState::setEmail)
        TextField(authState.password, onValueChange = authScreenState::setPassword)
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