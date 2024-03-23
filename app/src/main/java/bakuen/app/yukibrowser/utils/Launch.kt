package bakuen.app.yukibrowser.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LaunchedEffectAsync(key: Any = Unit, block: suspend CoroutineScope.() -> Unit) {
    LaunchedEffect(key) {
        launch(block = block)
    }
}