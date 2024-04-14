package com.patchself.compose.navigator

sealed class NavigationEvent {
    data object Backward : NavigationEvent()
    class Forward(val next: ScreenNode) : NavigationEvent()
    class Replace(val target: ScreenNode) : NavigationEvent()
}