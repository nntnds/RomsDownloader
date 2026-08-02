package com.nntndscvtcvt.romsdownloader.data.utils

import kotlinx.coroutines.CancellationException

suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (c: CancellationException) {
        throw c
    } catch (e: Exception) {
        Result.failure(e)
    }
}