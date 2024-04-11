package bakuen.app.yukibrowser.managers

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import bakuen.app.yukibrowser.appContext
import bakuen.app.yukibrowser.http
import bakuen.lib.http.DownloadTask
import bakuen.lib.http.getDownloadFileInfo
import com.tencent.smtt.sdk.QbSdk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File

object X5CoreMan {
    private val coreFile = File(appContext.filesDir, "x5core.apk")

    val canUse get() = QbSdk.isX5Core()
    var task by mutableStateOf<DownloadTask?>(null)
        private set
    @OptIn(DelicateCoroutinesApi::class)
    fun tryDownload() {
        if (!canUse) {
            install()
            if (canUse) return
        }
        if (task == null) {
            GlobalScope.launch {
                val up = UpdateCheckMan.get() ?: return@launch
                val info = http.getDownloadFileInfo(up.x5url)
                task = info.createTask(coreFile)
                task!!.start()
                install()
            }
        }
    }
    private fun install() {
        if (!canUse) {
            QbSdk.reset(appContext)
            QbSdk.installLocalTbsCore(
                appContext,
                0,
                coreFile.path
            )
        }
    }
}