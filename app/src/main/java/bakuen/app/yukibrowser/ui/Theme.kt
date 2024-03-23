package bakuen.app.yukibrowser.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

object Theme {
    val color = Color()
    class Color {
        val surface = Color(0xFF_000000)
        val onSurface = Color(0xFF_EEEEEE)
        val outline = Color(0xFF_7b757f)
        val primary = Color(0xFF_b28a9f)
        val secondary = Color(0xFF_bec2ff)
    }
    val typo = Typo()
    class Typo {
        val body = TextStyle(
            fontSize = 12.sp,
            color = color.onSurface
        )
        val headline = body.copy(
            fontSize = 15.sp
        )
    }
}
@Composable
inline fun BaseTheme(content: @Composable ()->Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Theme.color.surface)) {
        content()
    }
}