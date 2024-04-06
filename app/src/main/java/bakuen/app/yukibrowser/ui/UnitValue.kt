package bakuen.app.yukibrowser.ui

import android.content.res.Resources
import android.util.TypedValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt

fun Dp.toPx(density: Density) = with(density) { this@toPx.toPx() }
fun Int.pxToDp(density: Density) = with(density) { this@pxToDp.toDp() }
fun Int.systemDp(density: Density) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
).roundToInt().pxToDp(density)

val Int.systemDp @Composable get() = systemDp(LocalDensity.current)