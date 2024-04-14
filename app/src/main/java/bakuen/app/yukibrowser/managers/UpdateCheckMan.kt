package bakuen.app.yukibrowser.managers

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import bakuen.app.yukibrowser.appContext
import bakuen.app.yukibrowser.http
import bakuen.lib.http.get
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@OptIn(DelicateCoroutinesApi::class)
object UpdateCheckMan {
    private const val url = "https://pastebin.com/raw/F61jWGTX"
    var response by mutableStateOf<UpdateCheckResponse?>(null)
        private set

    init {
        GlobalScope.launch {
            response = http.get(url).successOrNull()?.readJson(UpdateCheckResponse.serializer())
        }
    }

    val currentVersionCode: Int get() {
        return appContext.packageManager.getPackageInfo(appContext.packageName, 0).versionCode
    }
}

@Serializable
data class UpdateCheckResponse(
    val versionName: String,
    val versionCode: Int,
    val x5code: Int,
    val x5description: String,
    val x5a7: String,
    val x5a8: String,
)