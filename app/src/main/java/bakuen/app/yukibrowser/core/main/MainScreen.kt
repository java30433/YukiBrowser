package bakuen.app.yukibrowser.core.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.tooling.preview.devices.WearDevices
import bakuen.app.yukibrowser.core.browse.BrowseScreen
import bakuen.app.yukibrowser.core.download.DownloadScreen
import bakuen.app.yukibrowser.core.welcome.RoundButton
import bakuen.app.yukibrowser.getX5CoreFile
import bakuen.app.yukibrowser.ui.Headline
import bakuen.app.yukibrowser.ui.Text
import bakuen.app.yukibrowser.ui.Theme
import bakuen.app.yukibrowser.utils.LaunchedEffectAsync
import com.patchself.compose.navigator.Navigator
import com.tencent.smtt.sdk.QbSdk

@Preview(showBackground = true, device = WearDevices.LARGE_ROUND)
@Composable
fun MainScreen() {
    //TODO 内核未安装的提示
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Headline(modifier = Modifier.padding(top = 4.dp, bottom = 6.dp), text = "Yuki 浏览器")
        SearchBox(onSearch = {
            Navigator.push { BrowseScreen(defaultUrl = "https://ie.icoa.cn/") }
        })
        RoundButton(onClick = {
            Navigator.push { DownloadScreen() }
        }) {
            Text(text = "测试页面")
        }
    }
}

@Composable
inline fun SearchBox(crossinline onSearch: (String) -> Unit = {}) {
    var focused by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .border(
                width = Dp.Hairline,
                brush = SolidColor(if (focused) Theme.color.primary else Theme.color.outline),
                shape = RoundedCornerShape(100)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            modifier = Modifier
                .onFocusChanged {
                    focused = it.isFocused
                }
                .weight(1f),
            value = value,
            onValueChange = { value = it },
            singleLine = true,
            decorationBox = {
                Box {
                    if (value.isEmpty()) Text(text = "搜索或粘贴链接", color = Theme.color.outline)
                    it()
                }
            }
        )
        Box(modifier = Modifier.clickable { onSearch(value) }) {
            Text(
                text = "开始",
                color = Theme.color.primary
            )
        }
    }
}