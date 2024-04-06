package com.patchself.compose.navigator

import androidx.compose.runtime.Composable
class ScreenNode internal constructor(internal val content: @Composable () -> Unit) {
    var enableSwipeBack = true
}