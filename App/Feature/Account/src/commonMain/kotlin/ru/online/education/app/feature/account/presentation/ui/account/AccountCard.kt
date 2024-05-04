package ru.online.education.app.feature.account.presentation.ui.account

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.feature.account.presentation.model.AuthScreenState
import ru.online.education.app.feature.account.presentation.model.AuthState

@Composable
fun AccountCard(
    authState: AuthState,
    onClick: () -> Unit,
    onLogOutClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .debbugable()
        ,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .debbugable()
        ) {
            Row {
                Column {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.ManageAccounts, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = when (authState) {
                                is AuthState.LoggedIn -> authState.login
                                AuthState.LoggedOut -> "Аккаунт"
                                AuthState.Undefined -> "Загрузка"
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (authState) {
                                is AuthState.LoggedIn -> authState.displayName
                                AuthState.LoggedOut -> "Необходимо авторизоваться"
                                AuthState.Undefined -> ""
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (authState is AuthState.LoggedIn) {
                    IconButton(
                        onClick = {
                            onLogOutClick()
                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Logout, null)
                    }
                }
            }
        }
    }
}