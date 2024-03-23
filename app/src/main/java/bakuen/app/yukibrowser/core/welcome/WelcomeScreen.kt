package bakuen.app.yukibrowser.core.welcome

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.tooling.preview.devices.WearDevices
import bakuen.app.yukibrowser.R
import bakuen.app.yukibrowser.ui.Navigator
import bakuen.app.yukibrowser.ui.Text
import bakuen.app.yukibrowser.ui.Theme

@Preview(showBackground = true, device = WearDevices.LARGE_ROUND)
@Composable
fun WelcomeScreen() {
    CenterBox {
        Column {
            Text(text = "欢迎使用")
            Text(text = "Yuki 浏览器", color = Theme.color.primary, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            RoundButton(onClick = {
                Navigator.replaceTop { FirstInstallX5CoreScreen() }
            }) {
                Image(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
inline fun RoundButton(noinline onClick: ()->Unit = {}, crossinline content: @Composable BoxScope.() -> Unit) {
    CompositionLocalProvider(Theme.LocalTextColor provides Theme.color.surface) {
        CenterBox(
            modifier = Modifier
                .background(color = Theme.color.onSurface, shape = RoundedCornerShape(100))
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .sizeIn(minWidth = 36.dp)
                .clickable(onClick = onClick),
            content = content
        )
    }
}

@Composable
inline fun CenterBox(@SuppressLint("ModifierParameter") modifier: Modifier? = null, content: @Composable BoxScope.()->Unit) {
    Box(modifier = modifier ?: Modifier.fillMaxSize(), contentAlignment = Alignment.Center, content = content)
}