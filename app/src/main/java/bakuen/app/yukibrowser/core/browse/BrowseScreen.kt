package bakuen.app.yukibrowser.core.browse

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patchself.compose.navigator.LocalScreen

@Composable
fun BrowseScreen(defaultUrl: String) {
    LocalScreen.current.enableSwipeBack = false
    val webViewState = rememberWebViewState(defaultUrl = defaultUrl)
    Box(modifier = Modifier.fillMaxSize()) {
        WebView(modifier = Modifier.fillMaxSize(), state = webViewState)
        GestureNavigator(webViewState)
    }
}