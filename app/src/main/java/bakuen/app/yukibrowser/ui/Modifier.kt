package bakuen.app.yukibrowser.ui

import androidx.compose.ui.Modifier

fun Modifier.conditional(condition: Boolean, chain: Modifier.()->Modifier) = if (condition) then(chain(this)) else this