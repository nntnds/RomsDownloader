package com.nntndscvtcvt.romsdownloader.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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

    override suspend fun downloadConsoleGames(
        consoleName: String
    ): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            a("psp_games")
            val entities = firestore.collection(consoleName)
                .get()
                .await()
                .documents
                .mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(GameDto::class.java)?.toEntity()
                }
            entities
                .chunked(500)
                .forEach { gameDao.insertAll(it) }
        }
    }.onFailure {
        Log.e("GameRepository", "Failed to download $consoleName", it)
    }

    private suspend fun a(
        consoleName: String
    ): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            val entities = firestore.collection(consoleName)
                .get()
                .await()
                .documents
                .mapNotNull { documentSnapshot ->
                    documentSnapshot.toObject(GameDto::class.java)?.toEntity()
                }
            entities
                .chunked(500)
                .forEach { gameDao.insertAll(it) }
        }
    }.onFailure {
        Log.e("GameRepository", "Failed to download $consoleName", it)
    }
}