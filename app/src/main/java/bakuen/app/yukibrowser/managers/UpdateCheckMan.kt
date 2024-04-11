package bakuen.app.yukibrowser.managers

import android.util.Log
import bakuen.app.yukibrowser.http
import bakuen.lib.http.get
import kotlinx.serialization.Serializable

object UpdateCheckMan {
    private const val url = "https://mirror.ghproxy.com/https://raw.githubusercontent.com/java30433/YukiBrowser-web/main/dist/version.json"
    private var cache: UpdateCheckResponse? = null
    suspend fun get(): UpdateCheckResponse? {
        if (cache == null) {
            cache = http.get(url).successOrNull()?.readJson(UpdateCheckResponse.serializer())
        }
        return cache
    }
}

@Serializable
data class UpdateCheckResponse(
    val versionName: String,
    val versionCode: Int,
    val x5code: Int,
    val x5description: String,
    val x5url: String
)