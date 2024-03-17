package ru.online.education.app.feature.account.presentation.ui.account

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.online.education.app.core.util.compose.debbugable
import ru.online.education.app.feature.account.presentation.model.AuthScreenState
import ru.online.education.app.feature.account.presentation.model.AuthState

@Composable
fun AccountCard(
    authState: AuthState
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .debbugable()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .debbugable()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.ManageAccounts, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(authState.login)
            }
            Spacer(Modifier.width(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(authState.displayName)
            }
        }
    }
}