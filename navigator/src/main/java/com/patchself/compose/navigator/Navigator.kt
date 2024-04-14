package com.patchself.compose.navigator

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

val LocalScreen = staticCompositionLocalOf<ScreenNode> { error("") }
internal lateinit var stateHolder: SaveableStateHolder
internal lateinit var screenRoot: @Composable (currentScreen: @Composable () -> Unit) -> Unit
internal val screens = mutableStateListOf<ScreenNode>()
object Navigator {

    private var event = EventChannel<NavigationEvent>()

    @SuppressLint("Range")
    internal fun nodeBackward() {
        screens.removeLastOrNull()
    }

    internal fun nodeForward(node: ScreenNode) {
        screens.add(node)
    }

    fun navigateBack(): Boolean {
        if (screens.size >= 2) {
            event.emit(NavigationEvent.Backward)
            return true
        } else return false
    }

    fun forward(screen: @Composable () -> Unit) {
        event.emit(NavigationEvent.Forward(ScreenNode(screen)))
    }

    fun replaceTop(screen: @Composable () -> Unit) {
        event.emit(NavigationEvent.Replace(ScreenNode(screen)))
    }

    @Composable
    fun ScreenContent(
        initScreen: @Composable () -> Unit,
        screenWrapper: @Composable (currentScreen: @Composable () -> Unit) -> Unit
    ) {
        stateHolder = rememberSaveableStateHolder()
        screenRoot = screenWrapper
        var isInit by rememberSaveable { mutableStateOf(true) }
        if (isInit) {
            screens.clear()
            screens.add(ScreenNode(initScreen))
            isInit = false
        }
        ReduceRecompose {
            if (screens.isNotEmpty()) {
                NavigationWrapper(
                    event = event.consumeAsState().value
                )
            }
        }
    }

    @Composable
    private fun ReduceRecompose(content: @Composable () -> Unit) {
        content()
    }
}