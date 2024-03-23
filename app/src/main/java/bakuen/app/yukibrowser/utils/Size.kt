package bakuen.app.yukibrowser.utils

import java.text.NumberFormat

fun Long.bytesToMB() = (toDouble() / 1024).toBigDecimal().setScale(1)