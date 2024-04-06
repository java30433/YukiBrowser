package bakuen.app.yukibrowser.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import bakuen.app.yukibrowser.utils.LaunchedEffectAsync
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Method
import java.io.File

interface DownloadState {
    val url: String
    val fileName: String
    val filePath: String
    val totalBytes: Long
    val downloadedBytes: Long
    val state: DownloadStates
}

@JvmInline
value class DownloadStates private constructor(private val inner: Int) {
    companion object {
        val DOWNLOADING = DownloadStates(1)
        val FINISHED = DownloadStates(2)
        val PAUSED = DownloadStates(3)
        val CANCELED = DownloadStates(4)
    }
}

@Composable
fun rememberDownloadFile(url: String, savePath: String, saveName: String): DownloadState {
    val state = remember {
        object : DownloadState {
            override val url = url
            override val fileName = saveName
            override val filePath = savePath
            override var totalBytes by mutableLongStateOf(0L)
            override var downloadedBytes by mutableLongStateOf(0L)
            override var state by mutableStateOf(DownloadStates.DOWNLOADING)
        }
    }
    LaunchedEffectAsync {
        Fuel.download(path = url, method = Method.GET)
            .fileDestination { _, _ -> File(savePath, saveName) }
            .progress { readBytes, totalBytes ->
                state.downloadedBytes = readBytes
                state.totalBytes = totalBytes
            }
            .response { _ ->
                state.state = DownloadStates.FINISHED
            }
    }
    return state
}