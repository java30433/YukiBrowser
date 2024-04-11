package bakuen.lib.http

import okhttp3.Request

suspend fun HttpClient.get(url: String) = call {
    url(url)
}