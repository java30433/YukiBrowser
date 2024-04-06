package bakuen.app.yukibrowser.prefs

import android.content.res.Resources
import android.os.Build
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class Settings(
    @ProtoNumber(1)
    val isScreenRound: Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Resources.getSystem().configuration.isScreenRound else false,
    @ProtoNumber(2)
    val webCore: Int = SYSTEM_WEBVIEW
) : ProtoStore {
    companion object {
        const val SYSTEM_WEBVIEW = 0
        const val X5_CORE = 1
    }
}