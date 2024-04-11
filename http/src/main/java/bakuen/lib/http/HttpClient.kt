package bakuen.lib.http

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class HttpClient {
    @PublishedApi
    internal val okClient = OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    inline fun call(builder: RequestBuilder.() -> Unit): Result {
        val response = try {
            okClient.newCall(RequestBuilder().apply(builder).build()).execute()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.NetworkError
        }
        return if (response.isSuccessful) Result.Success(response)
        else Result.ServerError(response)
    }

    class RequestBuilder {
        private val requestBuilder = Request.Builder()
        fun url(url: String) {
            requestBuilder.url(url)
        }
        fun header(name: String, value: String) {
            requestBuilder.header(name, value)
        }

        fun build() = requestBuilder.build()
    }
}