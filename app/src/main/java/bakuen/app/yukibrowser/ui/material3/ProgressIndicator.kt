package bakuen.app.yukibrowser.ui.material3

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import bakuen.app.yukibrowser.ui.Theme
import kotlin.math.abs
import kotlin.math.min

@Composable
fun LinearProgressIndicator(
    progress: () -> Float,
    modifier: Modifier = Modifier,
    color: Color = ProgressIndicatorDefaults.linearColor,
    trackColor: Color = ProgressIndicatorDefaults.linearTrackColor,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
) {
    LinearProgressIndicator(
        progress,
        modifier,
        color,
        trackColor,
        strokeCap,
        gapSize = ProgressIndicatorDefaults.LinearIndicatorTrackGapSize
    )
}

/**
 * <a href="https://m3.material.io/components/progress-indicators/overview" class="external" target="_blank">Determinate Material Design linear progress indicator</a>.
 *
 * Progress indicators express an unspecified wait time or display the duration of a process.
 *
 * ![Linear progress indicator image](https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flqdiyyvh-1P-progress-indicator-configurations.png?alt=media)
 *
 * By default there is no animation between [progress] values. You can use
 * [ProgressIndicatorDefaults.ProgressAnimationSpec] as the default recommended [AnimationSpec] when
 * animating progress, such as in the following example:
 *
 * @sample androidx.compose.material3.samples.LinearProgressIndicatorSample
 *
 * @param progress the progress of this progress indicator, where 0.0 represents no progress and 1.0
 * represents full progress. Values outside of this range are coerced into the range.
 * @param modifier the [Modifier] to be applied to this progress indicator
 * @param color color of this progress indicator
 * @param trackColor color of the track behind the indicator, visible when the progress has not
 * reached the area of the overall indicator yet
 * @param strokeCap stroke cap to use for the ends of this progress indicator
 * @param gapSize size of the gap between the progress indicator and the track
 * @param drawStopIndicator lambda that will be called to draw the stop indicator
 */

@Composable
fun LinearProgressIndicator(
    progress: () -> Float,
    modifier: Modifier = Modifier,
    color: Color = ProgressIndicatorDefaults.linearColor,
    trackColor: Color = ProgressIndicatorDefaults.linearTrackColor,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
    gapSize: Dp = ProgressIndicatorDefaults.LinearIndicatorTrackGapSize,
    drawStopIndicator: (DrawScope.() -> Unit)? = {
        drawStopIndicator(
            stopSize = ProgressIndicatorDefaults.LinearTrackStopIndicatorSize,
            color = color,
            strokeCap = strokeCap
        )
    },
) {
    val coercedProgress = { progress().coerceIn(0f, 1f) }
    Canvas(
        modifier
            .then(IncreaseSemanticsBounds)
            .semantics(mergeDescendants = true) {
                progressBarRangeInfo = ProgressBarRangeInfo(coercedProgress(), 0f..1f)
            }
            .size(LinearIndicatorWidth, LinearIndicatorHeight)
    ) {
        val strokeWidth = size.height
        val adjustedGapSize = if (strokeCap == StrokeCap.Butt || size.height > size.width) {
            gapSize
        } else {
            gapSize + strokeWidth.toDp()
        }
        val gapSizeFraction = adjustedGapSize / size.width.toDp()
        val currentCoercedProgress = coercedProgress()

        // track
        val trackStartFraction =
            currentCoercedProgress + min(currentCoercedProgress, gapSizeFraction)
        if (trackStartFraction <= 1f) {
            drawLinearIndicator(
                trackStartFraction, 1f, trackColor, strokeWidth, strokeCap
            )
        }
        // indicator
        drawLinearIndicator(
            0f, currentCoercedProgress, color, strokeWidth, strokeCap
        )
        // stop
        drawStopIndicator?.invoke(this)
    }
}

/**
 * Draws the stop indicator at the end of the track.
 *
 * @param stopSize size of this stop indicator, it cannot be bigger than the track's height
 * @param color color of this stop indicator
 * @param strokeCap stroke cap to use for the ends of this stop indicator
 */
fun DrawScope.drawStopIndicator(
    stopSize: Dp,
    color: Color,
    strokeCap: StrokeCap,
) {
    val adjustedStopSize = min(stopSize.toPx(), size.height) // Stop can't be bigger than track
    val stopOffset = (size.height - adjustedStopSize) / 2 // Offset from end
    if (strokeCap == StrokeCap.Round) {
        drawCircle(
            color = color,
            radius = adjustedStopSize / 2f,
            center = Offset(
                x = size.width - (adjustedStopSize / 2f) - stopOffset,
                y = size.height / 2f
            )
        )
    } else {
        drawRect(
            color = color,
            topLeft = Offset(
                x = size.width - adjustedStopSize - stopOffset,
                y = (size.height - adjustedStopSize) / 2f
            ),
            size = Size(width = adjustedStopSize, height = adjustedStopSize)
        )
    }
}

/**
 * <a href="https://m3.material.io/components/progress-indicators/overview" class="external" target="_blank">Indeterminate Material Design linear progress indicator</a>.
 *
 * Progress indicators express an unspecified wait time or display the duration of a process.
 *
 * ![Linear progress indicator image](https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flqdiyyvh-1P-progress-indicator-configurations.png?alt=media)
 *
 * @sample androidx.compose.material3.samples.IndeterminateLinearProgressIndicatorSample
 *
 * @param modifier the [Modifier] to be applied to this progress indicator
 * @param color color of this progress indicator
 * @param trackColor color of the track behind the indicator, visible when the progress has not
 * reached the area of the overall indicator yet
 * @param strokeCap stroke cap to use for the ends of this progress indicator
 */
@Deprecated(
    message = "Use the overload that takes `gapSize`, see `" +
            "LegacyIndeterminateLinearProgressIndicatorSample` on how to restore the previous behavior",
    replaceWith = ReplaceWith(
        "LinearProgressIndicator(modifier, color, trackColor, strokeCap, gapSize)"
    ),
    level = DeprecationLevel.HIDDEN
)

@Composable
fun LinearProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = ProgressIndicatorDefaults.linearColor,
    trackColor: Color = ProgressIndicatorDefaults.linearTrackColor,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
) {
    LinearProgressIndicator(
        modifier,
        color,
        trackColor,
        strokeCap,
        gapSize = ProgressIndicatorDefaults.LinearIndicatorTrackGapSize,
    )
}

/**
 * <a href="https://m3.material.io/components/progress-indicators/overview" class="external" target="_blank">Indeterminate Material Design linear progress indicator</a>.
 *
 * Progress indicators express an unspecified wait time or display the duration of a process.
 *
 * ![Linear progress indicator image](https://firebasestorage.googleapis.com/v0/b/design-spec/o/projects%2Fgoogle-material-3%2Fimages%2Flqdiyyvh-1P-progress-indicator-configurations.png?alt=media)
 *
 * @sample androidx.compose.material3.samples.IndeterminateLinearProgressIndicatorSample
 *
 * @param modifier the [Modifier] to be applied to this progress indicator
 * @param color color of this progress indicator
 * @param trackColor color of the track behind the indicator, visible when the progress has not
 * reached the area of the overall indicator yet
 * @param strokeCap stroke cap to use for the ends of this progress indicator
 * @param gapSize size of the gap between the progress indicator and the track
 */

@Composable
fun LinearProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = ProgressIndicatorDefaults.linearColor,
    trackColor: Color = ProgressIndicatorDefaults.linearTrackColor,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
    gapSize: Dp = ProgressIndicatorDefaults.LinearIndicatorTrackGapSize,
) {
    val infiniteTransition = rememberInfiniteTransition()
    // Fractional position of the 'head' and 'tail' of the two lines drawn, i.e. if the head is 0.8
    // and the tail is 0.2, there is a line drawn from between 20% along to 80% along the total
    // width.
    val firstLineHead = infiniteTransition.animateFloat(
        0f,
        1f,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = LinearAnimationDuration
                0f at FirstLineHeadDelay using FirstLineHeadEasing
                1f at FirstLineHeadDuration + FirstLineHeadDelay
            }
        )
    )
    val firstLineTail = infiniteTransition.animateFloat(
        0f,
        1f,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = LinearAnimationDuration
                0f at FirstLineTailDelay using FirstLineTailEasing
                1f at FirstLineTailDuration + FirstLineTailDelay
            }
        )
    )
    val secondLineHead = infiniteTransition.animateFloat(
        0f,
        1f,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = LinearAnimationDuration
                0f at SecondLineHeadDelay using SecondLineHeadEasing
                1f at SecondLineHeadDuration + SecondLineHeadDelay
            }
        )
    )
    val secondLineTail = infiniteTransition.animateFloat(
        0f,
        1f,
        infiniteRepeatable(
            animation = keyframes {
                durationMillis = LinearAnimationDuration
                0f at SecondLineTailDelay using SecondLineTailEasing
                1f at SecondLineTailDuration + SecondLineTailDelay
            }
        )
    )
    Canvas(
        modifier
            .then(IncreaseSemanticsBounds)
            .progressSemantics()
            .size(LinearIndicatorWidth, LinearIndicatorHeight)
    ) {
        val strokeWidth = size.height
        val adjustedGapSize = if (strokeCap == StrokeCap.Butt || size.height > size.width) {
            gapSize
        } else {
            gapSize + strokeWidth.toDp()
        }
        val gapSizeFraction = adjustedGapSize / size.width.toDp()

        // Track before line 1
        if (firstLineHead.value < 1f - gapSizeFraction) {
            val start = if (firstLineHead.value > 0) firstLineHead.value + gapSizeFraction else 0f
            drawLinearIndicator(
                start, 1f, trackColor, strokeWidth, strokeCap
            )
        }

        // Line 1
        if (firstLineHead.value - firstLineTail.value > 0) {
            drawLinearIndicator(
                firstLineHead.value,
                firstLineTail.value,
                color,
                strokeWidth,
                strokeCap,
            )
        }

        // Track between line 1 and line 2
        if (firstLineTail.value > gapSizeFraction) {
            val start = if (secondLineHead.value > 0) secondLineHead.value + gapSizeFraction else 0f
            val end = if (firstLineTail.value < 1f) firstLineTail.value - gapSizeFraction else 1f
            drawLinearIndicator(
                start, end, trackColor, strokeWidth, strokeCap
            )
        }

        // Line 2
        if (secondLineHead.value - secondLineTail.value > 0) {
            drawLinearIndicator(
                secondLineHead.value,
                secondLineTail.value,
                color,
                strokeWidth,
                strokeCap,
            )
        }

        // Track after line 2
        if (secondLineTail.value > gapSizeFraction) {
            val end = if (secondLineTail.value < 1) secondLineTail.value - gapSizeFraction else 1f
            drawLinearIndicator(
                0f, end, trackColor, strokeWidth, strokeCap
            )
        }
    }
}

@Deprecated(
    message = "Use the overload that takes `progress` as a lambda",
    replaceWith = ReplaceWith(
        "LinearProgressIndicator(\n" +
                "progress = { progress },\n" +
                "modifier = modifier,\n" +
                "color = color,\n" +
                "trackColor = trackColor,\n" +
                "strokeCap = strokeCap,\n" +
                ")"
    )
)
@Composable
fun LinearProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = ProgressIndicatorDefaults.linearColor,
    trackColor: Color = ProgressIndicatorDefaults.linearTrackColor,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
) = LinearProgressIndicator(
    progress = { progress },
    modifier = modifier,
    color = color,
    trackColor = trackColor,
    strokeCap = strokeCap,
)

@Suppress("DEPRECATION")
@Deprecated("Maintained for binary compatibility", level = DeprecationLevel.HIDDEN)
@Composable
fun LinearProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = ProgressIndicatorDefaults.linearColor,
    trackColor: Color = ProgressIndicatorDefaults.linearTrackColor,
) = LinearProgressIndicator(
    progress,
    modifier,
    color,
    trackColor,
    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
)

@Deprecated("Maintained for binary compatibility", level = DeprecationLevel.HIDDEN)
@Composable
fun LinearProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = ProgressIndicatorDefaults.linearColor,
    trackColor: Color = ProgressIndicatorDefaults.linearTrackColor,
) = LinearProgressIndicator(
    modifier,
    color,
    trackColor,
    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
)

private fun DrawScope.drawLinearIndicator(
    startFraction: Float,
    endFraction: Float,
    color: Color,
    strokeWidth: Float,
    strokeCap: StrokeCap,
) {
    val width = size.width
    val height = size.height
    // Start drawing from the vertical center of the stroke
    val yOffset = height / 2

    val isLtr = layoutDirection == LayoutDirection.Ltr
    val barStart = (if (isLtr) startFraction else 1f - endFraction) * width
    val barEnd = (if (isLtr) endFraction else 1f - startFraction) * width

    // if there isn't enough space to draw the stroke caps, fall back to StrokeCap.Butt
    if (strokeCap == StrokeCap.Butt || height > width) {
        // Progress line
        drawLine(color, Offset(barStart, yOffset), Offset(barEnd, yOffset), strokeWidth)
    } else {
        // need to adjust barStart and barEnd for the stroke caps
        val strokeCapOffset = strokeWidth / 2
        val coerceRange = strokeCapOffset..(width - strokeCapOffset)
        val adjustedBarStart = barStart.coerceIn(coerceRange)
        val adjustedBarEnd = barEnd.coerceIn(coerceRange)

        if (abs(endFraction - startFraction) > 0) {
            // Progress line
            drawLine(
                color,
                Offset(adjustedBarStart, yOffset),
                Offset(adjustedBarEnd, yOffset),
                strokeWidth,
                strokeCap,
            )
        }
    }
}

private val SemanticsBoundsPadding: Dp = 10.dp
private val IncreaseSemanticsBounds: Modifier = Modifier
    .layout { measurable, constraints ->
        val paddingPx = SemanticsBoundsPadding.roundToPx()
        // We need to add vertical padding to the semantics bounds in order to meet
        // screenreader green box minimum size, but we also want to
        // preserve a visual appearance and layout size below that minimum
        // in order to maintain backwards compatibility. This custom
        // layout effectively implements "negative padding".
        val newConstraint = constraints.offset(0, paddingPx * 2)
        val placeable = measurable.measure(newConstraint)

        // But when actually placing the placeable, create the layout without additional
        // space. Place the placeable where it would've been without any extra padding.
        val height = placeable.height - paddingPx * 2
        val width = placeable.width
        layout(width, height) {
            placeable.place(0, -paddingPx)
        }
    }
    .semantics(mergeDescendants = true) {}
    .padding(vertical = SemanticsBoundsPadding)

/**
 * Contains the default values used for [LinearProgressIndicator] and [CircularProgressIndicator].
 */
object ProgressIndicatorDefaults {
    /** Default color for a linear progress indicator. */
    val linearColor: Color
        @Composable get() =
            ProgressIndicatorTokens.ActiveIndicatorColor

    /** Default track color for a linear progress indicator. */
    val linearTrackColor: Color
        @Composable get() = ProgressIndicatorTokens.TrackColor

    /** Default stroke cap for a linear progress indicator. */
    val LinearStrokeCap: StrokeCap = StrokeCap.Round

    /** Default track stop indicator size for a linear progress indicator. */
    @Suppress("OPT_IN_MARKER_ON_WRONG_TARGET")
    val LinearTrackStopIndicatorSize: Dp = ProgressIndicatorTokens.StopSize

    /** Default indicator track gap size for a linear progress indicator. */
    @Suppress("OPT_IN_MARKER_ON_WRONG_TARGET")
    val LinearIndicatorTrackGapSize: Dp = ProgressIndicatorTokens.ActiveTrackSpace

    /**
     * The default [AnimationSpec] that should be used when animating between progress in a
     * determinate progress indicator.
     */
    val ProgressAnimationSpec = SpringSpec(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessVeryLow,
        // The default threshold is 0.01, or 1% of the overall progress range, which is quite
        // large and noticeable. We purposefully choose a smaller threshold.
        visibilityThreshold = 1 / 1000f
    )
}

// LinearProgressIndicator Material specs

// Width is given in the spec but not defined as a token.
/*@VisibleForTesting*/
internal val LinearIndicatorWidth = 240.dp

/*@VisibleForTesting*/
internal val LinearIndicatorHeight = ProgressIndicatorTokens.TrackThickness

// Indeterminate linear indicator transition specs

// Total duration for one cycle
private const val LinearAnimationDuration = 1800

// Duration of the head and tail animations for both lines
private const val FirstLineHeadDuration = 750
private const val FirstLineTailDuration = 850
private const val SecondLineHeadDuration = 567
private const val SecondLineTailDuration = 533

// Delay before the start of the head and tail animations for both lines
private const val FirstLineHeadDelay = 0
private const val FirstLineTailDelay = 333
private const val SecondLineHeadDelay = 1000
private const val SecondLineTailDelay = 1267

private val FirstLineHeadEasing = CubicBezierEasing(0.2f, 0f, 0.8f, 1f)
private val FirstLineTailEasing = CubicBezierEasing(0.4f, 0f, 1f, 1f)
private val SecondLineHeadEasing = CubicBezierEasing(0f, 0f, 0.65f, 1f)
private val SecondLineTailEasing = CubicBezierEasing(0.1f, 0f, 0.45f, 1f)

object ProgressIndicatorTokens {
    val ActiveIndicatorColor = Theme.color.primary
    val ActiveTrackSpace = 4.0.dp
    val StopSize = 4.0.dp
    val TrackColor = Theme.color.container
    val TrackThickness = 4.0.dp
    val Size = 48.0.dp
}