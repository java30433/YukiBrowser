package com.patchself.compose.navigator

internal sealed class NavigationMode(var current: Content?=null) {
    class Forward(current: Content) :NavigationMode(current)
    class Backward(current: Content?) :NavigationMode(current)
    class Rebase(current: Content):NavigationMode(current)
    class Reset(current: Content):NavigationMode(current)
}