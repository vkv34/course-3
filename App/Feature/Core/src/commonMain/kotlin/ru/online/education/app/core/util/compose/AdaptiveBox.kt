package ru.online.education.app.core.util.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Switch
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Stable
data class DeviceConfiguration(
    val screenWidthDp: Dp = 0.dp,
    val screenHeightDp: Dp = 0.dp,
    val screenWidthPixels: Int = 0,
    val screenHeightPixels: Int = 0,
    val deviceType: DeviceType = DeviceType.Phone,
    val isDebbugable: Boolean = true
)

interface Orienatation {
    interface Horizontal
    interface Vertical
}


sealed class DeviceType {
    data object Phone : DeviceType() {
        object Horizontal : DeviceType(), Orienatation.Horizontal {
            override fun toString(): String =
                "Phone.Horizontal"
        }

        data object Vertical : DeviceType(), Orienatation.Vertical {
            override fun toString(): String =
                "Phone.Vertical"
        }
    }

    data object Computer : DeviceType() {
        data object Horizontal : DeviceType(), Orienatation.Horizontal {
            override fun toString(): String =
                "Computer.Horizontal"
        }

        data object Vertical : DeviceType(), Orienatation.Vertical {
            override fun toString(): String =
                "Computer.Vertical"
        }
    }
}

fun Modifier.debbugable() = composed {
    val localDeviceConfiguration = LocalDeviceConfiguration.current
    if (localDeviceConfiguration.isDebbugable) {
        border(width = 1.dp, color = Color.Gray)
    } else {
        this
    }

}


val LocalDeviceConfiguration = compositionLocalOf { DeviceConfiguration() }


@Composable
fun AdaptiveBox(
    modifier: Modifier = Modifier,
    adaptiveContent: @Composable BoxScope.(deviceConfig: DeviceConfiguration) -> Unit
) {
    val density = LocalDensity.current
    val localDeviceConfiguration = remember { mutableStateOf(DeviceConfiguration()) }
    BoxWithConstraints(
        modifier = modifier
    ) {
        val widthDp = with(density) { constraints.maxWidth.toDp() }
        val heightDp = with(density) { constraints.maxHeight.toDp() }
        val screenSize: DeviceType = if (widthDp.value >= 600) {
            if (widthDp >= heightDp) DeviceType.Computer.Horizontal else DeviceType.Computer.Vertical
        } else {
            if (widthDp >= heightDp) DeviceType.Phone.Horizontal else DeviceType.Phone.Vertical
        }
        localDeviceConfiguration.value =
            localDeviceConfiguration.value.copy(
                screenWidthDp = widthDp,
                screenHeightDp = heightDp,
                screenWidthPixels = constraints.maxWidth,
                screenHeightPixels = constraints.maxHeight,
                deviceType = screenSize
            )

        CompositionLocalProvider(LocalDeviceConfiguration provides localDeviceConfiguration.value) {
            adaptiveContent(localDeviceConfiguration.value)
        }

       

        if (localDeviceConfiguration.value.isDebbugable) {
            Box(
                modifier = Modifier.align(Alignment.TopEnd)
                    .background(Color.Gray.copy(alpha = 0.7f))
            ) {
                Column {
                    with(localDeviceConfiguration.value) {
                        Text("width: $screenWidthDp.dp $screenWidthPixels.px")
                        Text("height: $screenHeightDp.dp $screenHeightPixels.px")
                        Text("deviceType: ${screenSize}")
                    }
                }
            }
        }
        
        Box(
            modifier = Modifier.align(Alignment.TopEnd)
                .background(Color.Gray.copy(alpha = 0.7f))
        ) {
            Row {
                Text("Debug")
                Switch(
                    localDeviceConfiguration.value.isDebbugable,
                    onCheckedChange = {
                        localDeviceConfiguration.value = localDeviceConfiguration.value.copy(isDebbugable = it)
                    })
            }
        }
    }
}