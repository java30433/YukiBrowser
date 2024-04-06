package bakuen.app.yukibrowser.prefs

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.File

@OptIn(ExperimentalSerializationApi::class)
private val protobuf = ProtoBuf { }

private val storeCache = mutableMapOf<String, ProtoStore>()

interface ProtoStore
@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalSerializationApi::class)
@Composable
fun <T : ProtoStore> rememberStore(serializer: KSerializer<T>, preview: T? = null): MutableState<T> {
    if (preview != null && LocalInspectionMode.current) return remember { mutableStateOf(preview) }
    val name = serializer.descriptor.serialName
    val context = LocalContext.current
    if (!storeCache.containsKey(name)) {
        storeCache[name] = protobuf.decodeFromByteArray(serializer, context.getFile(name).readBytes())
    }
    val state = remember { mutableStateOf(storeCache[name] as T) }
    LaunchedEffect(state.value == storeCache[name]) {
        storeCache[name] = state.value
        launch {
            context.getFile(name).writeBytes(protobuf.encodeToByteArray(serializer, state.value))
        }
    }
    return state
}

fun Context.getFile(name: String): File {
    val f = File(filesDir, name)
    if (!f.exists()) f.createNewFile()
    return f
}