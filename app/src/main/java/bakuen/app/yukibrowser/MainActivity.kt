package bakuen.app.yukibrowser

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import bakuen.app.yukibrowser.core.main.MainScreen
import bakuen.app.yukibrowser.core.welcome.WelcomeScreen
import bakuen.app.yukibrowser.prefs.AppData
import bakuen.app.yukibrowser.prefs.rememberStore
import bakuen.app.yukibrowser.ui.BaseTheme
import bakuen.app.yukibrowser.ui.Theme
import com.patchself.compose.navigator.Navigator
import com.tencent.smtt.sdk.QbSdk
import java.io.File

fun Context.getX5CoreFile() = File(filesDir, "x5core.apk")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!QbSdk.isX5Core()) {
            QbSdk.reset(this)
            QbSdk.installLocalTbsCore(
                this,
                0,
                this.getX5CoreFile().path
            )
        }
        setContent {
            BaseTheme {
                BackHandler {
                    Navigator.navigateBack()
                }
                Navigator.ScreenContent(
                    initScreen = { if (rememberStore(serializer = AppData.serializer()).value.firstLaunch) WelcomeScreen() else MainScreen() },
                    screenWrapper = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Theme.color.surface)
                        ) {
                            it()
                        }
                    }
                )

            }
        }
    }
}