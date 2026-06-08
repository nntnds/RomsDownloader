package com.nntndscvtcvt.romsdownloader.presentation.utils

import android.util.Log
import java.net.URLDecoder

fun Throwable.toUserMessage(): String = when (this) {
    is java.net.UnknownHostException -> "No internet connection"
    is java.net.SocketTimeoutException -> "Connection timeout"
    else -> {
        Log.e("ERROR", this.toString())
        "Something went wrong"
    }
}

fun String.cut(startChars: Int = 15, endChars: Int = 15): String {
    if (length <= startChars + endChars) return this
    return "${take(startChars)}...${takeLast(endChars)}"
}

fun String.extractCookie(cookieName: String): String? {
    if (isBlank()) return null
    return Regex("(?:^|;\\s*)$cookieName=([^;]*)")
        .find(this)
        ?.groups?.get(1)?.value
        ?.trim()
        ?.takeIf { it.isNotBlank() }
        ?.let { URLDecoder.decode(it, "UTF-8") }
}