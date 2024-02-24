package utils

import androidx.compose.ui.graphics.Color
import model.Image
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

fun Image.Color.toComposeColor() = Color(r, g, b, a)

fun getContrastRatio(l1: Double, l2: Double): Double {
    val lighter = if (l1 > l2) l1 else l2
    val darker = if (l1 > l2) l2 else l1
    return (lighter + 0.05) / (darker + 0.05)
}

fun getLuminance(color: Color): Double {
    val r = if (color.red <= 0.03928) color.red / 12.92 else ((color.red + 0.055) / 1.055).pow(2.4)
    val g = if (color.green <= 0.03928) color.green / 12.92 else ((color.green + 0.055) / 1.055).pow(2.4)
    val b = if (color.blue <= 0.03928) color.blue / 12.92 else ((color.blue + 0.055) / 1.055).pow(2.4)
    return 0.2126 * r + 0.7152 * g + 0.0722 * b
}

fun getReadableColor(bgColor: Color): Color {
    val bgLuminance = getLuminance(bgColor)
    val whiteLuminance = getLuminance(Color.White)
    val blackLuminance = getLuminance(Color.Black)

    return if (getContrastRatio(bgLuminance, whiteLuminance) > getContrastRatio(bgLuminance, blackLuminance)) {
        Color.White
    } else {
        Color.Black
    }
}

fun adjustColor(color: Color, factor: Float): Color {
    return Color(
        red = (color.red * factor).coerceIn(0f, 1f),
        green = (color.green * factor).coerceIn(0f, 1f),
        blue = (color.blue * factor).coerceIn(0f, 1f),
        alpha = color.alpha
    )
}

///////
fun getReadableColorVariant(bgColor: Color): Color {
    val brightness = sqrt(
        0.299 * bgColor.red * bgColor.red +
                0.587 * bgColor.green * bgColor.green +
                0.114 * bgColor.blue * bgColor.blue
    )

    val saturation = sqrt(
        (bgColor.red - bgColor.green) * (bgColor.red - bgColor.green) +
                (bgColor.red - bgColor.blue) * (bgColor.red - bgColor.blue) +
                (bgColor.green - bgColor.blue) * (bgColor.green - bgColor.blue)
    )

    return when {
        brightness < 0.5 && saturation < 0.5 -> adjustColor(bgColor, 1.5f)
        brightness > 0.5 && saturation < 0.5 -> adjustColor(bgColor, 0.5f)
        else -> adjustColor(bgColor, (1 / brightness).toFloat())
    }
}
