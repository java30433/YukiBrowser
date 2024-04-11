package bakuen.lib.http

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.Call
import okhttp3.Request
import java.io.File
import java.io.RandomAccessFile
import java.util.UUID

sealed class DownloadState {
    data object Paused : DownloadState()
    data object Downloading : DownloadState()
    data object Finished : DownloadState()

    abstract class Error: DownloadState()
    class ServerError(val code: Int, val message: String) : Error()
    class ClientError(val exception: Exception): Error()
}

class DownloadFileInfo(
    val fileName: String,
    val fileSize: Long,
    val client: HttpClient,
    val url: String
) {
    fun createTask(saveFile: File) =
        DownloadTask(client = client, url = url, saveFile = saveFile, totalBytes = fileSize)
}

suspend fun HttpClient.getDownloadFileInfo(url: String): DownloadFileInfo {
    val response = okClient.newCall(Request.Builder().url(url).build()).execute()
    var fileName: String? = null
    var fileSize: Long? = null
    if (response.isSuccessful) {
        val contentDispositionHeader = response.header("Content-Disposition")
        val contentLengthHeader = response.header("Content-Length")
        if (contentDispositionHeader != null) {
            val fileNameRegex = Regex("filename=\"([^\"]+)\"")
            val matchResult = fileNameRegex.find(contentDispositionHeader)
            fileName = matchResult?.groupValues?.getOrNull(1)
        }
        if (contentLengthHeader != null) {
            fileSize = contentLengthHeader.toLongOrNull()
        }
    }
    return DownloadFileInfo(
        fileName = fileName ?: url.split("/").lastOrNull() ?: UUID.randomUUID().toString(),
        fileSize = fileSize ?: -1L,
        client = this,
        url = url
    )
}

//TODO: 多进程访问安全
class DownloadTask(
    private val client: HttpClient,
    val url: String,
    val saveFile: File,
    val totalBytes: Long
) {
    private val _downloadState = MutableStateFlow<DownloadState>(DownloadState.Paused)
    val downloadState: StateFlow<DownloadState> get() = _downloadState
    private var call: Call? = null
    var readBytes = saveFile.length()
        private set
    suspend fun pause() {
        call?.cancel()
        call = null
        _downloadState.emit(DownloadState.Paused)
    }

    suspend fun start() {
        if (call != null) return
        call = client.okClient.newCall(Request.Builder().url(url).run {
            if (totalBytes > 0) addHeader("Range", "bytes=$readBytes-$totalBytes")
            else this
        }.build())

        try {
            val response = call!!.execute()
            if (!response.isSuccessful) {
                _downloadState.emit(DownloadState.ServerError(response.code, response.message))
                return
            }
            val body = response.body
            val accFile = RandomAccessFile(saveFile, "rw")
            accFile.seek(readBytes)

            body?.byteStream()?.use { inputStream ->
                val buffer = ByteArray(8192)
                var len: Int
                while (-1 != inputStream.read(buffer).also { len = it }) {
                    accFile.write(buffer, 0, len)
                    readBytes += len
                    _downloadState.emit(DownloadState.Downloading)
                }
            }

            _downloadState.emit(DownloadState.Finished)
        } catch (e: Exception) {
            e.printStackTrace()
            _downloadState.emit(DownloadState.ClientError(e))
        }
    }
}

private val gb = 1 shl 30
private val mb = 1 shl 20
private val kb = 1 shl 10
fun Long.toReadable() = when {
    this >= gb -> "%.1f GB".format(toDouble() / gb)
    this >= mb -> "%.1f MB".format(toDouble() / mb)
    this >= kb -> "%.0f kB".format(toDouble() / kb)
    else -> "$this bytes"
}