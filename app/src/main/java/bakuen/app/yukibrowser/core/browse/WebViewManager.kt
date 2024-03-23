package bakuen.app.yukibrowser.core.browse

import android.content.Context
import android.content.MutableContextWrapper
import android.view.View
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

class WebViewState(private val context: Context) {
    class Page(val url: String, val webView: BaseWebView?)

    private val maxSize = 3
    private val pages = mutableListOf<Page>()
    private val forwardPages = mutableListOf<Page>()
    private var cacheWebViews = mutableListOf<BaseWebView>()

    val currentPage get() = pages.lastOrNull()?.let {
        it.webView ?: requireWebView(it.url)
    }

    fun open(url: String) {
        forwardPages.forEach {
            cacheWebViews.add(it.webView ?: return)
        }
        forwardPages.clear()

        pages.add(Page(url, requireWebView(url)))
    }

    fun back() {
        forwardPages.add(pages.removeLastOrNull() ?: return)
    }
    fun forward() {
        pages.add(forwardPages.removeLastOrNull() ?: return)
    }

    private fun requireWebView(url: String): BaseWebView {
        val webView = cacheWebViews.lastOrNull()
            ?: if (pages.size >= maxSize) pages.find { it.webView != null }!!.webView!!
            else BaseWebView(context, onLoadUrl = {
                open(it)
            })
        webView.loadUrl(url)
        return webView
    }
}
@Composable
fun rememberWebViewState(defaultUrl: String): WebViewState {
    val context = LocalContext.current
    return remember {
        WebViewState(context).apply { open(defaultUrl) }
    }
}

@Composable
fun WebView(modifier: Modifier = Modifier, state: WebViewState) {
    AndroidView(
        modifier = modifier,
        factory = { FrameLayout(it).apply {
            addView(state.currentPage)
        } },
        update = {
            it.removeAllViews()
            it.addView(state.currentPage)
        }
    )
}