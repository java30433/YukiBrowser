package bakuen.app.yukibrowser.core.welcome

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.tooling.preview.devices.WearDevices
import bakuen.app.yukibrowser.ui.Navigator
import bakuen.app.yukibrowser.ui.SmallText
import bakuen.app.yukibrowser.ui.Text
import bakuen.app.yukibrowser.utils.LaunchedEffectAsync
import bakuen.app.yukibrowser.utils.toJSONObject
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString

private data class FState(
    val fileName: String = "tbs_core_046514_armv7.apk",
    val description: String = "Chromium 98 (TBS 046514)",
    val url: String = ""
)

@Composable
fun FirstInstallX5CoreScreen() {
    var state by remember { mutableStateOf(FState()) }
    LaunchedEffectAsync {
        val result = Fuel.get("https://yuki-browser.vercel.app/getx5core.json").awaitString().toJSONObject()
        state = FState(
            fileName = result.getString("name"),
            description = result.getString("description"),
            url = result.getString("url")
        )
    }
    FirstInstallX5CoreScreen(state = state)
}

@Preview(showBackground = true, device = WearDevices.LARGE_ROUND)
@Composable
private fun FirstInstallX5CoreScreen(state: FState = FState()) {
    CenterBox {
        Column {
            Text(text = "使用浏览器需要在线安装X5内核")
            Text(text = "内核大小约50MB，请保持网络通畅")
            SmallText(text = state.fileName)
            SmallText(text = state.description)
            Spacer(modifier = Modifier.size(8.dp))
            RoundButton(onClick = {
                Navigator.replaceTop {
                    DownloadX5CoreScreen(state.url)
                }
            }) {
                Text(text = "继续")
            }
        }
    }
}