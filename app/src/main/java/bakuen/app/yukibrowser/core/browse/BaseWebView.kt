package bakuen.app.yukibrowser.core.browse

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

@SuppressLint("ViewConstructor", "SetJavaScriptEnabled")
class BaseWebView(context: Context, onLoadUrl: (String)->Unit) : WebView(context) {
    init {
        settings.apply {
            @Suppress("DEPRECATION")
            javaScriptEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            allowFileAccess = true
            allowContentAccess = true
            domStorageEnabled = true
            databaseEnabled = true
        }
        webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(webView: WebView, handler: SslErrorHandler, p2: SslError) {
                handler.proceed()
            }

            override fun shouldOverrideUrlLoading(webView: WebView, request: WebResourceRequest): Boolean {
                onLoadUrl(request.url.toString())
                return true
            }
        }
    }
}