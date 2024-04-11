package bakuen.app.yukibrowser.core.welcome

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bakuen.app.yukibrowser.R
import bakuen.app.yukibrowser.core.main.MainScreen
import bakuen.app.yukibrowser.managers.X5CoreMan
import bakuen.app.yukibrowser.prefs.AppData
import bakuen.app.yukibrowser.prefs.Settings
import bakuen.app.yukibrowser.prefs.rememberStore
import bakuen.app.yukibrowser.prefs.setStore
import bakuen.app.yukibrowser.ui.DialogMan
import bakuen.app.yukibrowser.ui.Icon
import bakuen.app.yukibrowser.ui.LocalColors
import bakuen.app.yukibrowser.ui.RoundPreview
import bakuen.app.yukibrowser.ui.SmallText
import bakuen.app.yukibrowser.ui.Space
import bakuen.app.yukibrowser.ui.Text
import bakuen.app.yukibrowser.ui.TextSecondary
import bakuen.app.yukibrowser.ui.Theme
import bakuen.app.yukibrowser.ui.material3.HorizontalDivider
import com.patchself.compose.navigator.Navigator

@RoundPreview
@Composable
fun WelcomeScreen() {
    var settings by rememberStore(preview = Settings())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Space(32.dp)
        TextSecondary(text = "欢迎使用")
        Space(7.5.dp)
        Text(text = "Yuki 浏览器", color = Theme.color.primary, fontSize = 24.sp)
        Space(58.5.dp)
        TextSecondary(text = "进一步设置")
        Icon(
            size = 24.dp,
            painter = painterResource(id = R.drawable.ic_arrow_drop_down),
            color = Theme.color.textSecondary
        )
        Space(24.dp)
        HorizontalDivider(modifier = Modifier.width(100.dp))
        Space(24.dp)
        Text(text = "选择屏幕类型")
        Space(12.dp)
        LargeShadowCheckbox(
            checked = !settings.isScreenRound,
            onClick = { settings = settings.copy(isScreenRound = false) },
            icon = painterResource(id = R.drawable.ic_round_screen),
            title = "方屏"
        )
        Space(4.dp)
        LargeShadowCheckbox(
            checked = settings.isScreenRound,
            onClick = { settings = settings.copy(isScreenRound = true) },
            icon = painterResource(id = R.drawable.ic_round_screen),
            title = "圆屏"
        )
        Space(36.dp)
        Text(text = "选择浏览器内核")
        Space(12.dp)
        val hasWebview = try {
            true //TODO: Check
        } catch (e: Exception) {
            false
        }
        LargeShadowCheckbox(
            disabled = if (LocalInspectionMode.current) true else !hasWebview,
            checked = settings.webCore == Settings.SYSTEM_WEBVIEW,
            onClick = { settings = settings.copy(webCore = Settings.SYSTEM_WEBVIEW) },
            icon = painterResource(id = R.drawable.ic_logo_webview),
            title = "系统 Webview"
        )
        Space(4.dp)
        LargeShadowCheckbox(
            checked = settings.webCore == Settings.X5_CORE,
            onClick = {
                DialogMan.showDialog {
                    DialogMan.YesOrNot(
                        title = "提醒",
                        body = "需要下载内核（约50MB），是否立即开始下载？",
                        yes = "下载",
                        no = "暂时不要",
                        onClick = {
                            if (it) {
                                X5CoreMan.tryDownload()
                            }
                        }
                    )
                }
                settings = settings.copy(webCore = Settings.X5_CORE)
            },
            icon = painterResource(id = R.drawable.ic_logo_x5),
            title = "腾讯 X5 内核"
        )
        Space(12.dp)
        SmallText(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = "建议无WebView或系统版本过旧时使用X5内核"
        )
        Space(36.dp)
        RoundButton(onClick = {
            setStore<AppData> { copy(firstLaunch = false) }
            Navigator.replaceTop { MainScreen() }
        }) {
            Icon(size = 24.dp, painter = painterResource(id = R.drawable.ic_arrow_forward))
        }
        Space(66.dp)
    }
}

@Composable
inline fun RoundButton(
    noinline onClick: () -> Unit = {},
    crossinline content: @Composable BoxScope.() -> Unit
) {
    CompositionLocalProvider(LocalColors provides Theme.Colors(text = LocalColors.current.surface)) {
        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .background(color = Theme.color.text, shape = RoundedCornerShape(100))
                .height(52.dp)
                .fillMaxWidth()
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
            content = content
        )
    }
}

@Composable
inline fun LargeShadowCheckbox(
    disabled: Boolean = false,
    checked: Boolean,
    noinline onClick: () -> Unit,
    icon: Painter,
    title: String,
    crossinline info: @Composable BoxScope.() -> Unit = {
        if (disabled) {
            Text(text = "不可用")
        } else if (checked) {
            Icon(painter = painterResource(id = R.drawable.ic_done))
        }
    }
) {
    CompositionLocalProvider(
        LocalColors provides if (disabled) Theme.Colors(text = Theme.color.textDisabled)
        else if (checked) Theme.Colors(text = LocalColors.current.surface) else LocalColors.current
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .height(86.dp)
                .border(
                    width = 0.5.dp,
                    shape = RoundedCornerShape(16.dp),
                    color = if (disabled) Theme.color.container else Theme.color.containerActive
                )
                .background(
                    color = if (disabled) Theme.color.containerDisabled
                    else if (checked) Theme.color.containerActive else Theme.color.container,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .clickable(enabled = !disabled, onClick = onClick)
        ) {
            Icon(painter = icon)
            Box(modifier = Modifier.align(Alignment.BottomStart)) {
                Text(text = title)
            }
            Box(modifier = Modifier.align(Alignment.TopEnd), content = info)
        }
    }
}