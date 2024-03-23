package bakuen.app.yukibrowser

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.ComposeView
import bakuen.app.yukibrowser.core.main.MainScreen
import bakuen.app.yukibrowser.ui.BaseTheme
import bakuen.app.yukibrowser.ui.CurrentScreen
import bakuen.app.yukibrowser.ui.NavHost
import com.tencent.smtt.sdk.QbSdk
import java.io.File
import java.io.FileOutputStream

fun Context.getX5CoreFile() = File(filesDir, "x5core.apk")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseTheme {
                NavHost(initScreen = { MainScreen() }) {
                    CurrentScreen()
                }
            }
        }
    }
}