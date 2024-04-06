package bakuen.app.yukibrowser.core.browse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BrowseScreen(defaultUrl: String) {
    val webViewState = rememberWebViewState(defaultUrl = defaultUrl)
    Box(modifier = Modifier.fillMaxSize()) {
        WebView(modifier = Modifier.fillMaxSize(), state = webViewState)
        GestureNavigator()
    }
}