package com.nntndscvtcvt.romsdownloader.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.nntndscvtcvt.romsdownloader.data.dto.GameDto
import com.nntndscvtcvt.romsdownloader.data.local.dao.GameDao
import com.nntndscvtcvt.romsdownloader.data.mappers.toDomain
import com.nntndscvtcvt.romsdownloader.data.mappers.toEntity
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadGamesState
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import com.nntndscvtcvt.romsdownloader.domain.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class GameRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val gameDao: GameDao
) : GameRepository {

    override fun getAllGames(): Flow<List<Game>> = gameDao.getAllGames()
        .map { games -> games.map { it.toDomain() } }
        .catch { Log.e("ERROR", "$it") }

    override fun downloadConsoleGames(consoleName: String): Flow<DownloadGamesState> = flow {
        emit(DownloadGamesState.Started)
        gameDao.clearPlatformGames(consoleName)

        val entities = firestore.collection(consoleName)
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(GameDto::class.java)?.toEntity() }

        entities.chunked(500).forEach { chunk ->
            gameDao.insertAll(chunk)
        }

        emit(DownloadGamesState.Success)
    }.catch {
        emit(DownloadGamesState.Failed)
    }.flowOn(Dispatchers.IO)

    override fun getGamesCount(platform: String): Flow<Int> = gameDao.getGamesCount(platform)

    override suspend fun deletePlatformGames(platform: String): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            gameDao.deletePlatformGames(platform)
        }
    }
}