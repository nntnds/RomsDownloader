package com.nntndscvtcvt.romsdownloader.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.google.firebase.firestore.FirebaseFirestore
import com.nntndscvtcvt.romsdownloader.data.dto.GameDto
import com.nntndscvtcvt.romsdownloader.data.local.dao.GameDao
import com.nntndscvtcvt.romsdownloader.data.mappers.toDomain
import com.nntndscvtcvt.romsdownloader.data.mappers.toEntity
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import com.nntndscvtcvt.romsdownloader.domain.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class GameRepositoryImpl(
    val firestore: FirebaseFirestore,
    val gameDao: GameDao,
    val datastore: DataStore<Preferences>
) : GameRepository {
    private val SAVED_VERSION_KEY = intPreferencesKey("version")

    override fun getAllGames(): Flow<List<Game>> = gameDao.getAllGames()
        .map { games -> games.map { it.toDomain() } }
        .catch { Log.e("ERROR", "$it") }

    override suspend fun sync(): Result<Unit> = runCatching {
        val remoteVersion = getRemoteVersion()
        val savedVersion = datastore.data.first()[SAVED_VERSION_KEY] ?: -1
        val isEmpty = gameDao.getCount() == 0

        if (isEmpty || savedVersion < remoteVersion) {
            saveAllGames()
            datastore.edit { it[SAVED_VERSION_KEY] = remoteVersion }
        }
    }.onFailure { Log.e("SYNC_ERROR", "Sync failed", it) }

    private suspend fun getRemoteVersion(): Int {
        return firestore.document("config/games_version")
            .get()
            .await()
            .getLong("version")?.toInt() ?: 0
    }

    private suspend fun saveAllGames() = withContext(Dispatchers.IO) {
        val entities = firestore.collection("psp_games")
            .get()
            .await()
            .documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(GameDto::class.java)?.toEntity(documentSnapshot.id)
            }
        gameDao.syncGames(entities)
    }
}