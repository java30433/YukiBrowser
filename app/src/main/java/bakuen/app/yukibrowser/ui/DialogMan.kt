package bakuen.app.yukibrowser.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val LocalDialog = compositionLocalOf<DialogMan.Dialog> { error("") }
object DialogMan {
    class Dialog(val content: @Composable () -> Unit) {
        fun close() {
            dialogs.remove(this)
        }
    }
    @Composable
    fun DialogsContent() {
        if (dialogs.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .background(color = Color(0x80_000000))
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                dialogs.forEach {
                    CompositionLocalProvider(LocalDialog provides it) {
                        it.content()
                    }
                }
            }
        }
    }
    private val dialogs = mutableStateListOf<Dialog>()
    fun showDialog(content: @Composable () -> Unit) {
        dialogs.add(Dialog(content))
    }

    @Composable
    fun <T> OptionsDialog(
        title: String,
        content: @Composable () -> Unit,
        leftOptions: List<Pair<T, String>>,
        rightOptions: List<Pair<T, String>>,
        onClick: (T) -> Unit
    ) {
        val dialog = LocalDialog.current
        Box(
            modifier = Modifier
                .border(
                    width = 0.5.dp,
                    color = Theme.color.outline,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(color = Theme.color.container, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Column {
                Headline(text = title)
                Space(size = 4.dp)
                content()
                Space(size = 6.dp)
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.align(Alignment.TopStart)) {
                        leftOptions.forEach { (id, label) ->
                            Text(modifier = Modifier
                                .clickable {
                                    onClick(id)
                                    dialog.close()
                                }
                                .padding(5.dp), text = label, color = Theme.color.primary)
                        }
                    }
                    Row(modifier = Modifier.align(Alignment.TopEnd)) {
                        rightOptions.forEach { (id, label) ->
                            Box(Modifier
                                .clickable {
                                    onClick(id)
                                    dialog.close()
                                }
                                .padding(5.dp)
                                .widthIn(min = 26.dp), contentAlignment = Alignment.Center) {
                                Text(text = label, color = Theme.color.primary)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun YesOrNot(
        title: String,
        body: String,
        yes: String = "是",
        no: String = "否",
        onClick: (Boolean) -> Unit = {}
    ) {
        OptionsDialog(
            title = title,
            content = { Text(text = body) },
            leftOptions = listOf(false to no),
            rightOptions = listOf(true to yes),
            onClick = onClick
        )
    }
}

@RoundPreview
@Composable
private fun Preview() {
    DialogMan.YesOrNot(
        title = "提醒",
        body = "需要下载内核（约50MB），是否立即开始下载？",
        yes = "下载",
        no = "暂时不要"
    )
}