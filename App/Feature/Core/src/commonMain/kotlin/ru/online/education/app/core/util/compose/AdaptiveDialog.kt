package ru.online.education.app.core.util.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    adaptiveContent: @Composable () -> Unit
) {
    val localDeviceConfiguration = LocalDeviceConfiguration.current
    if (localDeviceConfiguration.isHorizontal) {
        BasicAlertDialog(
            onDismissRequest = onDismiss,
            modifier = modifier,
        ) {
            Card(
                modifier = Modifier
            ) {
                adaptiveContent()
            }
        }
    } else {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
//            modifier = modifier
//                .wrapContentSize()
//                ,
            windowInsets = WindowInsets.statusBars
                .add(WindowInsets.ime)
                .add(WindowInsets.waterfall)
        ) {
            Box(
                modifier = modifier
//                    .verticalScroll(rememberScrollState())
//                    .imePadding()

            ) {

                adaptiveContent()
            }
        }
    }
}