package bakuen.app.yukibrowser.prefs

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class AppData @OptIn(ExperimentalSerializationApi::class) constructor(
    @ProtoNumber(1)
    val firstLaunch: Boolean = true
) : ProtoStore