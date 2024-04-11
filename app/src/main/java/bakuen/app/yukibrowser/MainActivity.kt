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
import bakuen.app.yukibrowser.prefs.getStore
import bakuen.app.yukibrowser.prefs.rememberStore
import bakuen.app.yukibrowser.ui.BaseTheme
import bakuen.app.yukibrowser.ui.DialogMan
import bakuen.app.yukibrowser.ui.Theme
import com.patchself.compose.navigator.Navigator
import com.tencent.smtt.sdk.QbSdk
import java.io.File


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseTheme {
                BackHandler {
                    Navigator.navigateBack()
                }
                Navigator.ScreenContent(
                    initScreen = { if (getStore<AppData>().firstLaunch) WelcomeScreen() else MainScreen() },
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
                DialogMan.DialogsContent()
            }
        }
    }
}