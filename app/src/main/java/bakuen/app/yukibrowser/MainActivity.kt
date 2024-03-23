package bakuen.app.yukibrowser

import android.app.Activity
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!QbSdk.isX5Core()) {
            val coreName = "tbs_core_046514_armv7.apk"
            val outputFile = File(filesDir, coreName)
            val inputStream = assets.open(coreName)
            val outputStream = FileOutputStream(outputFile)
            val buffer = ByteArray(1024)
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
            }
            outputStream.close()
            inputStream.close()
            Log.i("X5内核安装", "生成内核：${outputFile.path}")
            QbSdk.reset(this)
            QbSdk.installLocalTbsCore(
                this,
                46514,
                outputFile.path
            )
        }
        setContent {
            BaseTheme {
                NavHost(initScreen = { MainScreen() }) {
                    CurrentScreen()
                }
            }
        }
    }
}