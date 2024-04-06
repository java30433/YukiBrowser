package bakuen.app.yukibrowser.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object DialogMan {
    val dialogs = arrayListOf<@Composable () -> Unit>()
    fun showDialog(content: @Composable ()->Unit) {
        dialogs.add(content)
    }
    
    @Composable
    fun <T> OptionsDialog(
        title: String,
        content: @Composable () -> Unit,
        leftOptions: List<Pair<T, String>>,
        rightOptions: List<Pair<T, String>>,
        onClick: (T)->Unit
    ) {
        Box(modifier = Modifier
            .border(width = 0.5.dp, color = Theme.color.outline, shape = RoundedCornerShape(8.dp))
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
                                .clickable { onClick(id) }
                                .padding(5.dp), text = label, color = Theme.color.primary)
                        }
                    }
                    Row(modifier = Modifier.align(Alignment.TopEnd)) {
                        rightOptions.forEach { (id, label) ->
                            Text(modifier = Modifier
                                .clickable { onClick(id) }
                                .padding(5.dp), text = label, color = Theme.color.primary)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun YesOrNot(title: String, body: String, onClick: (Boolean) -> Unit = {}) {
        OptionsDialog(
            title = title,
            content = { Text(text = body) },
            leftOptions = emptyList(),
            rightOptions = listOf(false to "取消", true to "确定"),
            onClick = onClick
        )
    }
}
@RoundPreview
@Composable
private fun Preview() {
    DialogMan.YesOrNot(
        title = "标题",
        body = "由于此行为的细微差别，如果用于重启效应的参数不是适当的参数，可能会出现问题"
    )
}