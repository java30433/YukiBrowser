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
object Navigator {
    val screens = mutableStateListOf<Content>()
    val currentScreen get() = screens.last()

    fun push(content: Content) {
        screens.add(content)
    }
    fun pop() {
        screens.removeLast()
    }
    fun replaceTop(content: Content) {
        screens[screens.lastIndex] = content
    }
}

private var stateHolder: SaveableStateHolder? = null
@Composable
fun NavHost(initScreen: Content, content: Content) {
    if(stateHolder == null) stateHolder = rememberSaveableStateHolder()
    Navigator.push(initScreen)
    content()
}

@Composable
fun CurrentScreen() {
    Navigator.screens.forEach {
        stateHolder!!.SaveableStateProvider(key = it.hashCode()) {
            AnimatedVisibility(
                visible = it == Navigator.currentScreen,
                enter = slideInHorizontally(),
                exit = slideOutHorizontally()
            ) {
                it()
            }
        }
    }
}