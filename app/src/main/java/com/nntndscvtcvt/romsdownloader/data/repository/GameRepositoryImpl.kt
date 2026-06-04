package com.nntndscvtcvt.romsdownloader.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.google.firebase.firestore.FirebaseFirestore
import com.nntndscvtcvt.romsdownloader.data.dto.GameModelDto
import com.nntndscvtcvt.romsdownloader.data.dto.toDomain
import com.nntndscvtcvt.romsdownloader.data.local.GameDao
import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity
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

    override fun getAllGames(): Flow<Result<List<GameEntity>>> = gameDao.getAllGames()
        .map { Result.success(it) }
        .catch { emit(Result.failure(it)) }

    override suspend fun sync() {
        try {
            val remoteVersion = getRemoteVersion()
            val savedVersion = datastore.data.first()[SAVED_VERSION_KEY] ?: -1
            val isEmpty = gameDao.getCount() == 0

            if (isEmpty || savedVersion < remoteVersion) {
                saveAllGames()
                datastore.edit { it[SAVED_VERSION_KEY] = remoteVersion }
            }
        } catch (e: Exception) { Log.e("Error", "Sync failed", e) }
    }

    private suspend fun getRemoteVersion(): Int {
        return firestore.document("config/games_version")
            .get()
            .await()
            .getLong("version")?.toInt() ?: 0
    }

    private suspend fun saveAllGames() = withContext(Dispatchers.IO){
        val entities = firestore.collection("games")
            .get()
            .await()
            .documents.mapNotNull { documentSnapshot ->
                documentSnapshot.toObject(GameModelDto::class.java)?.toDomain(documentSnapshot.id)
            }
        gameDao.syncGames(entities)
    }
}