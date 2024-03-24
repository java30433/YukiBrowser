package com.patchself.compose.navigator

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi

typealias Content = @Composable () -> Unit

object Navigator {
    private val stack = NavigationStack()
    private var current: NavigationMode by mutableStateOf(NavigationMode.Rebase({}))
    private var currentIndex = 0

    internal var stateHolder: SaveableStateHolder? = null

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun ViewContent() {
        stateHolder = rememberSaveableStateHolder()
        Column {
            NavigationWrapper(current = current, stack = stack)
        }

    }

    fun navigateBack(): Boolean {
        stack.getPrevious()?.let {
            current = NavigationMode.Backward(it)
            return true
        }
        return false
    }

    fun push(content: Content) {
        stack.push(content, true)
        current = NavigationMode.Forward(content)
    }

    fun replaceTop(content: Content) {
        stack.removeLast()
        push(content)
    }

    fun initController(content: Content) {
        stack.clear()
        stack.push(content, true)
        current = NavigationMode.Rebase(current = content)
    }
}