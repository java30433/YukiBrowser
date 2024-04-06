package bakuen.app.yukibrowser.core.browse

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import bakuen.app.yukibrowser.ui.systemDp

@Composable
fun GestureNavigator() {
    Box(modifier = Modifier.fillMaxSize()) {
        var activeY by remember { mutableFloatStateOf(-1f) }
        var offset by remember { mutableFloatStateOf(0f) }
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.systemDp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            activeY = it.y
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            offset += dragAmount.x
                            activeY = change.position.y
                        }
                    )
                }
        )


    }
}