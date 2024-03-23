package bakuen.app.yukibrowser.ui

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Headline(modifier: Modifier = Modifier, text: String) {
    BasicText(modifier = modifier, text = text, style = Theme.typo.headline)
}

@Composable
fun Text(modifier: Modifier = Modifier, text: String) {
    BasicText(modifier = Modifier, text = text, style = Theme.typo.body)
}
@Composable
fun Text(modifier: Modifier = Modifier, text: String, color: Color) {
    BasicText(modifier = Modifier, text = text, style = Theme.typo.body.copy(color = color))
}