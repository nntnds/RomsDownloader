package com.nntndscvtcvt.romsdownloader.domain.repository

import kotlinx.coroutines.flow.Flow

interface CookieRepository {
    val loggedInSig: Flow<String?>
    val loggedInUser: Flow<String?>
    suspend fun saveCookies(sig: String, user: String)
    suspend fun clearCookies()
}