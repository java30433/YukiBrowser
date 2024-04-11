package bakuen.app.yukibrowser.managers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

object DownloadMan {

}

@Serializable
data class SerializableDownloadTask @OptIn(ExperimentalSerializationApi::class) constructor(
    @ProtoNumber(0) val id: Int,
    @ProtoNumber(1) val state: Int,
    @ProtoNumber(2) val url: String,
    @ProtoNumber(3) val name: String,
    @ProtoNumber(4) val totalBytes: Long,
    @ProtoNumber(5) val readBytes: Long,
    @ProtoNumber(6) val code: Int
)
