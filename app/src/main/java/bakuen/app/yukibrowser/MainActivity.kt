package bakuen.app.yukibrowser

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import bakuen.app.yukibrowser.core.main.MainScreen
import bakuen.app.yukibrowser.core.welcome.WelcomeScreen
import bakuen.app.yukibrowser.prefs.Settings
import bakuen.app.yukibrowser.ui.BaseTheme
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
        Navigator.initController { if (Settings.firstLaunch.getBlocking()) WelcomeScreen() else MainScreen() }
        setContent {
            BackHandler {
                Navigator.navigateBack()
            }
            BaseTheme {
                Navigator.ViewContent()
            }
        }
    }
}