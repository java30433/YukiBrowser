package bakuen.app.yukibrowser.utils

fun Long.bytesToMB() = (toDouble() / 1024).toBigDecimal().setScale(1)