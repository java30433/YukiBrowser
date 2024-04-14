package bakuen.app.yukibrowser.core.browse

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import bakuen.app.yukibrowser.R
import bakuen.app.yukibrowser.ui.Icon
import bakuen.app.yukibrowser.ui.systemDp
import bakuen.app.yukibrowser.ui.toPx

@Composable
fun GestureNavigator(webViewState: WebViewState) {
    Box(modifier = Modifier.fillMaxSize()) {
        val maxOffset = 36.dp.toPx.toInt()
        var offset by remember { mutableFloatStateOf(0f) }
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.TopStart)
                .width(1.systemDp)
                .draggable(state = rememberDraggableState {
                    offset += it
                }, orientation = Orientation.Horizontal, onDragStopped = {
                    if (offset >= maxOffset) webViewState.back()
                })
        )
        Icon(
            modifier = Modifier
                .background(
                    color = Color(0x80_000000),
                    shape = RoundedCornerShape(100)
                )
                .padding(4.dp)
                .offset {
                    IntOffset(
                        x = offset.toInt().coerceAtMost(maxOffset), y = 0
                    )
                },
            painter = painterResource(id = R.drawable.ic_angle_left)
        )
    }
}