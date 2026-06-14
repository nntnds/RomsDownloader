package com.nntndscvtcvt.romsdownloader.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getUseExternalDownloader(): Flow<Boolean>
    suspend fun setUseExternalDownloader(value: Boolean)
}