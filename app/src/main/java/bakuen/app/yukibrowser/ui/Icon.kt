package bakuen.app.yukibrowser.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Icon(size: Dp = 16.dp, painter: Painter, color: Color = LocalColors.current.text) =
    Image(
        modifier = Modifier.size(size),
        painter = painter,
        contentDescription = null,
        colorFilter = ColorFilter.tint(color = color)
    )
@Composable
fun Icon(modifier: Modifier, painter: Painter, color: Color = LocalColors.current.text) =
    Image(
        modifier = modifier,
        painter = painter,
        contentDescription = null,
        colorFilter = ColorFilter.tint(color = color)
    )