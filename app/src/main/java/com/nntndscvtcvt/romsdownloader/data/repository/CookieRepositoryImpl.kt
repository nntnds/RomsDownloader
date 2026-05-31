package com.nntndscvtcvt.romsdownloader.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nntndscvtcvt.romsdownloader.domain.repository.CookieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class CookieRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : CookieRepository {
    private val LOGGED_IN_SIG = stringPreferencesKey("logged-in-sig")
    private val LOGGED_IN_USER = stringPreferencesKey("logged-in-user")

    override val loggedInSig: Flow<String?> = dataStore.data
        .map { it[LOGGED_IN_SIG] }
        .catch { emit(null) }

    override val loggedInUser: Flow<String?> = dataStore.data
        .map { it[LOGGED_IN_USER] }
        .catch { emit(null) }

    override suspend fun saveCookies(sig: String, user: String) {
        dataStore.edit { preferences ->
            preferences[LOGGED_IN_SIG] = sig
            preferences[LOGGED_IN_USER] = user
        }
    }

    override suspend fun clearCookies() {
        dataStore.edit { preferences ->
            preferences.remove(LOGGED_IN_SIG)
            preferences.remove(LOGGED_IN_USER)
        }
    }
}