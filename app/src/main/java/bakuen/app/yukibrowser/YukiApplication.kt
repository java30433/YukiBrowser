package bakuen.app.yukibrowser

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Looper
import android.widget.Toast
import bakuen.lib.http.HttpClient

val http = HttpClient()
@SuppressLint("StaticFieldLeak")
private lateinit var _context: Context
val appContext get() = _context
class YukiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        _context = this
    }
}