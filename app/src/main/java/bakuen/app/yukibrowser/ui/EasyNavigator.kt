package bakuen.app.yukibrowser.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder

private typealias Content = @Composable ()->Unit
class Navigator(initScreen: Content = {}) {
    val screens = mutableStateListOf(initScreen)
    val currentScreen get() = screens.last()

    fun push(content: Content) {
        screens.add(content)
    }
    fun pop() {
        screens.removeLast()
    }
}

val LocalNavigator = compositionLocalOf { Navigator() }
private var stateHolder: SaveableStateHolder? = null
@Composable
fun NavHost(initScreen: Content, content: Content) {
    if(stateHolder == null) stateHolder = rememberSaveableStateHolder()
    CompositionLocalProvider(LocalNavigator provides Navigator(initScreen)) {
        content()
    }
}

@Composable
fun CurrentScreen() {
    val navigator = LocalNavigator.current
    navigator.screens.forEach {
        stateHolder!!.SaveableStateProvider(key = it.hashCode()) {
            AnimatedVisibility(
                visible = it == navigator.currentScreen,
                enter = slideInHorizontally(),
                exit = slideOutHorizontally()
            ) {
                it()
            }
        }
    }
}