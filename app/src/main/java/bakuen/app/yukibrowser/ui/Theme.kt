package bakuen.app.yukibrowser.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.sp

fun Color.reduceBrightness(value: Float) =
    copy(
        red = (red - value).coerceIn(0f, 1f),
        green = (green - value).coerceIn(0f, 1f),
        blue = (blue - value).coerceIn(0f, 1f)
    )

val LocalColors = compositionLocalOf { Theme.Colors() }

object Theme {
    val color = Colors()

    data class Colors(
        val surface: Color = Color(0xFF_000000),
        val text: Color = Color(0xFF_FFFFFF),
        val textSecondary: Color = text.reduceBrightness(0.3f),
        val textDisabled: Color = text.reduceBrightness(0.6f),
        val container: Color = Color(0xFF_333333),
        val containerActive: Color = container.reduceBrightness(-0.5f),
        val containerDisabled: Color = container.reduceBrightness(0.5f),
        val outline: Color = Color(0xFF_7b757f),
        val primary: Color = Color(0xFF_b28a9f),
        val secondary: Color = Color(0xFF_bec2ff),
    )

    val typo = Typo()

    class Typo {
        val body
            @Composable get() = TextStyle(
                fontSize = 14.sp,
                color = LocalColors.current.text
            )
        val bodySecondary
            @Composable get() = TextStyle(
                fontSize = 14.sp,
                color = LocalColors.current.textSecondary
            )
        val headline
            @Composable get() = body.copy(
                fontSize = 18.sp
            )
        val small
            @Composable get() = bodySecondary.copy(
                fontSize = 10.sp
            )
    }
}

@Composable
inline fun BaseTheme(crossinline content: @Composable () -> Unit) {
    val designWidth = 192f
    val density = LocalContext.current.resources.displayMetrics.widthPixels / designWidth
    val fontScale = LocalDensity.current.fontScale
    CompositionLocalProvider(
        LocalDensity provides Density(density = density, fontScale = fontScale)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalColors.current.surface)
        ) {
            content()
        }
    }
}