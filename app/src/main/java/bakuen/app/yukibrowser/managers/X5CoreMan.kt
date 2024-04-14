package bakuen.app.yukibrowser.managers

import android.content.Context
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import bakuen.app.yukibrowser.appContext
import bakuen.app.yukibrowser.http
import bakuen.lib.http.DownloadState
import bakuen.lib.http.DownloadTask
import bakuen.lib.http.getDownloadFileInfo
import com.tencent.smtt.sdk.QbSdk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File

object X5CoreMan {
    private val coreFile = File(appContext.getExternalFilesDir("x5core"), "x5core.apk")

    val canUse get() = QbSdk.isX5Core()
    val hasCore get() = coreFile.exists()
    var task by mutableStateOf<DownloadTask?>(null)
        private set
    @OptIn(DelicateCoroutinesApi::class)
    fun tryDownload() {
        if (task == null) {
            GlobalScope.launch {
                val up = UpdateCheckMan.response ?: return@launch
                val info = http.getDownloadFileInfo(if (Build.CPU_ABI == "arm64-v8a") up.x5a8 else up.x5a7)
                task = info.createTask(coreFile)
                task!!.start()
                //install()
            }
        } else if (task!!.downloadState is DownloadState.Error) {
            GlobalScope.launch {
                task!!.start()
            }
        }
    }
    fun install() {
        if (!canUse) {
            QbSdk.reset(appContext)
            QbSdk.installLocalTbsCore(
                appContext,
                46514,
                coreFile.path
            )
        }
    }
}