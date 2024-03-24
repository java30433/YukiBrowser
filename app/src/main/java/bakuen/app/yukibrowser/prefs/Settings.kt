package bakuen.app.yukibrowser.prefs

import android.app.Application
import android.content.Context
import com.dylanc.datastore.DataStoreOwner

object Settings : DataStoreOwner(name = "settings") {
    val firstLaunch by booleanPreference(true)
}