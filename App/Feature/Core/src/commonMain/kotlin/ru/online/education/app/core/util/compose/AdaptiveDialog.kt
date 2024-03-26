package ru.online.education.app.core.util.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    adaptiveContent: @Composable () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        Card(
            modifier = Modifier.fillMaxSize()
                .padding(8.dp)
        ) {
            adaptiveContent()
        }
    }
}