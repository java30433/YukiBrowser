package bakuen.lib.http

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import okhttp3.Response

private val json = Json {
    ignoreUnknownKeys = true
}

sealed class Result {
    fun successOrNull() = if (this is Success) this else null
    class Success(private val okResponse: Response) : Result() {
        private val body = okResponse.body!!
        fun readBytes() = body.bytes()
        fun readString() = body.string()
        fun <T> readJson(serializable: KSerializer<T>): T? {
            return try {
                json.decodeFromString(serializable, readString())
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    data object NetworkError : Result()
    class ServerError(okResponse: Response) : Result() {
        val errorCode = okResponse.code
        val message = okResponse.message
    }
}