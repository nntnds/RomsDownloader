package com.nntndscvtcvt.romsdownloader.presentation.util

fun Throwable.toUserManager(): String = when (this) {
    is java.net.UnknownHostException -> "No internet connection"
    is java.net.SocketTimeoutException -> "Connection timeout"
    else -> "Something went wrong"
}