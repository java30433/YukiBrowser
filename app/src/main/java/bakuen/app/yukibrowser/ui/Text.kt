package bakuen.app.yukibrowser.ui

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit

@Composable
fun Headline(modifier: Modifier = Modifier, text: String) {
    BasicText(modifier = modifier, text = text, style = Theme.typo.headline)
}

@Composable
fun Text(modifier: Modifier = Modifier, text: String) {
    BasicText(modifier = modifier, text = text, style = Theme.typo.body)
}
@Composable
fun TextSecondary(modifier: Modifier = Modifier, text: String) {
    BasicText(modifier = modifier, text = text, style = Theme.typo.bodySecondary)
}

@Composable
fun SmallText(modifier: Modifier = Modifier, text: String) {
    BasicText(modifier = modifier, text = text, style = Theme.typo.small)
}

@Composable
fun Text(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Theme.typo.body.color,
    fontSize: TextUnit = Theme.typo.body.fontSize
) {
    BasicText(modifier = modifier, text = text, style = Theme.typo.body.copy(color = color, fontSize = fontSize))
}