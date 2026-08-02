package com.nntndscvtcvt.romsdownloader.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.nntndscvtcvt.romsdownloader.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {
    private val USE_EXTERNAL_DOWNLOADER = booleanPreferencesKey("use_external_downloader")

    override fun getUseExternalDownloader(): Flow<Boolean> {
        return dataStore.data.map { it[USE_EXTERNAL_DOWNLOADER] ?: false }
    }

    override suspend fun setUseExternalDownloader(value: Boolean) {
        dataStore.edit { it[USE_EXTERNAL_DOWNLOADER] = value }
    }
}