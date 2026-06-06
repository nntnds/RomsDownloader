package com.nntndscvtcvt.romsdownloader.presentation.utils

fun Throwable.toUserMessage(): String = when (this) {
    is java.net.UnknownHostException -> "No internet connection"
    is java.net.SocketTimeoutException -> "Connection timeout"
    else -> "Something went wrong"
}

fun String.cut(startChars: Int = 15, endChars: Int = 15): String {
    if (length <= startChars + endChars) return this
    return "${take(startChars)}...${takeLast(endChars)}"
}