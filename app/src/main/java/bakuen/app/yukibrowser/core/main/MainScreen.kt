package bakuen.app.yukibrowser.core.main

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import bakuen.app.yukibrowser.core.browse.BrowseScreen
import bakuen.app.yukibrowser.managers.X5CoreMan
import bakuen.app.yukibrowser.prefs.Settings
import bakuen.app.yukibrowser.prefs.getStore
import bakuen.app.yukibrowser.ui.Headline
import bakuen.app.yukibrowser.ui.LocalColors
import bakuen.app.yukibrowser.ui.RoundPreview
import bakuen.app.yukibrowser.ui.Text
import bakuen.app.yukibrowser.ui.Theme
import bakuen.lib.http.DownloadState
import bakuen.lib.http.toReadable
import com.patchself.compose.navigator.Navigator
import kotlinx.coroutines.flow.flow

@RoundPreview
@Composable
fun MainScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (getStore<Settings>().webCore == Settings.X5_CORE && !X5CoreMan.canUse) X5DownloadCard()
        Headline(modifier = Modifier.padding(top = 4.dp, bottom = 6.dp), text = "Yuki 浏览器")
        SearchBox(onSearch = {
            Navigator.forward { BrowseScreen(defaultUrl = "https://ie.icoa.cn/") }
        })
    }
}

@Composable
fun X5DownloadCard() {
    val task = X5CoreMan.task
    if (task == null) {
        Text(
            modifier = Modifier.clickable { X5CoreMan.tryDownload() },
            text = "X5内核未下载，点此继续"
        )
    } else {
        val ds by task.downloadState.collectAsState()
        when (ds) {
            DownloadState.Downloading -> Text(text = "X5内核下载中 ${task.readBytes.toReadable()}/${task.totalBytes.toReadable()}")
            is DownloadState.Error -> Text(
                modifier = Modifier.clickable { X5CoreMan.tryDownload() },
                text = "X5内核下载出错！点击重试"
            )
            DownloadState.Finished -> Text(text = "下载完成")
            else -> Text(text = "准备下载中...")
        }
    }
}

@Composable
inline fun SearchBox(crossinline onSearch: (String) -> Unit = {}) {
    var focused by remember { mutableStateOf(false) }
    var value by rememberSaveable { mutableStateOf("") }
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .border(
                width = Dp.Hairline,
                brush = SolidColor(if (focused) LocalColors.current.primary else LocalColors.current.outline),
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
            textStyle = Theme.typo.body,
            value = value,
            onValueChange = { value = it },
            singleLine = true,
            decorationBox = {
                Box {
                    if (value.isEmpty()) Text(
                        text = "搜索或粘贴链接",
                        color = LocalColors.current.outline
                    )
                    it()
                }
            }
        )
        Box(modifier = Modifier.clickable { onSearch(value) }) {
            Text(
                text = "开始",
                color = LocalColors.current.primary
            )
        }
    }
}