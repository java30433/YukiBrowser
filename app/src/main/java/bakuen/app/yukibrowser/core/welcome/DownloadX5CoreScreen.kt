package bakuen.app.yukibrowser.core.welcome

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.tooling.preview.devices.WearDevices
import bakuen.app.yukibrowser.core.main.MainScreen
import bakuen.app.yukibrowser.getX5CoreFile
import bakuen.app.yukibrowser.prefs.Settings
import bakuen.app.yukibrowser.ui.Text
import bakuen.app.yukibrowser.ui.material3.LinearProgressIndicator
import bakuen.app.yukibrowser.utils.LaunchedEffectAsync
import bakuen.app.yukibrowser.utils.bytesToMB
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Method
import com.patchself.compose.navigator.Navigator
import com.tencent.smtt.sdk.QbSdk
import java.io.File

private data class DState(
    val readBytes: Long = 20L,
    val totalBytes: Long = 100L,
    val downloaded: Boolean = false
)

@Composable
fun DownloadX5CoreScreen(url: String) {
    var state by remember { mutableStateOf(DState()) }
    val context = LocalContext.current
    LaunchedEffectAsync {
        Fuel.download(path = url, method = Method.GET)
            .fileDestination { _, _ -> context.getX5CoreFile() }
            .progress { readBytes, totalBytes ->
                state = state.copy(readBytes = readBytes, totalBytes = totalBytes)
            }
            .response { _ ->
                state = state.copy(downloaded = true)
            }
    }
    MDownloadX5CoreScreen(state = state)
}

@Preview(showBackground = true, device = WearDevices.LARGE_ROUND)
@Composable
private fun MDownloadX5CoreScreen(state: DState = DState()) {
    CenterBox {
        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            if (state.downloaded) {
                Navigator.replaceTop {
                    LaunchedEffectAsync {
                        Settings.firstLaunch.set(false)
                    }
                    MainScreen()
                }
            } else {
                Text(text = "下载中 ${state.readBytes.bytesToMB()}MB/${state.totalBytes.bytesToMB()}MB")
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = {
                        state.readBytes.toFloat() / state.totalBytes.toFloat()
                    }
                )
            }
        }
    }
}