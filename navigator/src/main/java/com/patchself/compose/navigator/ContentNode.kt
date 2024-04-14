package com.patchself.compose.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ScreenNode internal constructor(internal val content: @Composable () -> Unit) {
    var enableSwipeBack by mutableStateOf(true)
}