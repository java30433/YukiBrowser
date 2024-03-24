package com.patchself.compose.navigator

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.max
import kotlin.math.min

@ExperimentalComposeUiApi
@Composable
internal fun NavigationWrapper(
    current: NavigationMode,
    stack: NavigationStack,
    modifier: Modifier = Modifier
) {
    var isAnimating = remember { false }
    BoxWithConstraints(modifier = modifier
        .fillMaxSize()
        .pointerInteropFilter { isAnimating }) {
        val coroutineScope = rememberCoroutineScope()

        val state = remember { NavigationState() }
        val swipeOffset = remember { Animatable(0f) }
        val minValue = 0f
        val maxValue = constraints.maxWidth.toFloat()
        val left = remember { mutableStateOf<Content?>(null) }
        val right = remember { mutableStateOf<Content?>(null) }

        when (current) {
            is NavigationMode.Backward -> {
                left.value = stack.getPrevious()
                right.value = stack.getCurrent()
            }

            is NavigationMode.Forward -> {
                left.value = state.current
                runBlocking {
                    swipeOffset.snapTo(maxValue)
                }
                right.value = current.current!!
            }

            is NavigationMode.Rebase -> {
                left.value = null
                right.value = current.current
                state.current = current.current
            }

            is NavigationMode.Reset -> {
                left.value = stack.getCurrent()
                right.value = state.current
            }
        }
        DisposableEffect(current, effect = {
            var autoAnimTargetValue = 0f
            var autoAnimStartValue = 0f
            when (current) {
                is NavigationMode.Backward -> {
                    autoAnimTargetValue = maxValue
                    autoAnimStartValue = swipeOffset.value
                }

                is NavigationMode.Reset -> {
                    autoAnimTargetValue = maxValue
                    autoAnimStartValue = minValue
                }

                is NavigationMode.Forward -> {
                    autoAnimTargetValue = minValue
                    autoAnimStartValue = maxValue
                }

                else -> {}
            }
            coroutineScope.launch {
                isAnimating = true
                if (current !is NavigationMode.Backward) {
                    swipeOffset.snapTo(autoAnimStartValue)
                }
                swipeOffset.animateTo(autoAnimTargetValue, tween(400))
                when (current) {
                    is NavigationMode.Forward -> {
                    }

                    is NavigationMode.Backward -> {
                        stack.removeLast()
//                        right.value = left.value
                        right.value = stack.getCurrent()
                        left.value = stack.getPrevious()
                        state.current = right.value
                    }

                    is NavigationMode.Reset -> {
                        state.current = left.value
                        right.value = left.value
                        left.value = stack.getPrevious()
                    }

                    else -> {}
                }
                state.current = current.current!!
                swipeOffset.snapTo(0f)
                isAnimating = false
            }
            onDispose { }
        })
        Box {
            Box(
                Modifier
                    .draggable(state = DraggableState {
                        if (stack.size() <= 1 || isAnimating) {
                            return@DraggableState
                        }
                        runBlocking {
                            swipeOffset.snapTo(
                                min(
                                    max((swipeOffset.value + it), minValue), maxValue
                                )
                            )
                        }
                    }, orientation = Orientation.Horizontal, onDragStopped = { velocity ->
                        if (stack.size() <= 1 || isAnimating) {
                            return@draggable
                        }
                        val targetValue = if (FloatExponentialDecaySpec().getTargetValue(
                                swipeOffset.value, velocity
                            ) > maxValue / 2f
                        ) maxValue else minValue
                        isAnimating = true
                        swipeOffset.animateTo(targetValue)
                        isAnimating = false
                        if (targetValue != 0f) {
                            Navigator.navigateBack()
                        }
                    })
            ) {
                Layout(content = {
                    Box(Modifier.layoutId(0)) { StateHoldInvoke(left.value) }
                    Box(
                        Modifier
                            .layoutId(1)
                            .shadow(Dp(8f))
                    ) { StateHoldInvoke(right.value) }
                }, measurePolicy = { list, constraints ->
                    val placeables = list.map { it.measure(constraints) to it.layoutId }
                    val height = placeables.maxByOrNull { it.first.height }?.first?.height ?: 0
                    layout(constraints.maxWidth, height) {
                        placeables.forEach { (placeable, tag) ->
                            if (tag is Int) {
                                placeable.place(
                                    x = if (tag == 0) {
                                        ((-constraints.maxWidth + swipeOffset.value * 1f) * 0.3f).toInt()
                                    } else {
                                        swipeOffset.value.toInt()
                                    },
                                    y = 0
                                )
                            }
                        }
                    }
                })
            }
            if (isAnimating) {
                Box(Modifier.pointerInteropFilter {
                    return@pointerInteropFilter true
                }) {

                }
            }
        }
    }
}

private class NavigationState {
    var current: Content? = null
}

@Composable
private fun StateHoldInvoke(content: Content?) {
    if (content != null) {
        Navigator.stateHolder!!.SaveableStateProvider(key = content.hashCode(), content = content)
    }
}