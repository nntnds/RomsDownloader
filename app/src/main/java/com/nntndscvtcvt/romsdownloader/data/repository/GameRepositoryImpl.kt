package com.nntndscvtcvt.romsdownloader.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.nntndscvtcvt.romsdownloader.data.dto.GameDto
import com.nntndscvtcvt.romsdownloader.data.local.dao.GameDao
import com.nntndscvtcvt.romsdownloader.data.local.model.GameEntity
import com.nntndscvtcvt.romsdownloader.data.mappers.toDomain
import com.nntndscvtcvt.romsdownloader.data.mappers.toEntity
import com.nntndscvtcvt.romsdownloader.data.utils.suspendRunCatching
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadGamesCatalogState
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import com.nntndscvtcvt.romsdownloader.domain.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class GameRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val gameDao: GameDao
) : GameRepository {

    override fun getAllGames(): Flow<List<Game>> = gameDao.getAllGames()
        .map { games -> games.map { it.toDomain() } }
        .flowOn(Dispatchers.Default)

    override fun downloadConsoleGames(consoleName: String): Flow<DownloadGamesCatalogState> = flow {
        emit(DownloadGamesCatalogState.Started)

        val entities = firestore.collection(consoleName)
            .get()
            .await()
            .documents
            .mapNotNull { it.toObject(GameDto::class.java)?.toEntity() }

        gameDao.replacePlatformGames(consoleName, entities)

        emit(DownloadGamesCatalogState.Success)
    }.catch { e ->
        Log.e("GameRepository", "Failed to download catalog for $consoleName", e)
        emit(DownloadGamesCatalogState.Failed)
    }.flowOn(Dispatchers.IO)

    override fun getGamesCount(platform: String): Flow<Int> = gameDao.getGamesCount(platform)

    override suspend fun deletePlatformGames(platform: String): Result<Unit> = suspendRunCatching {
        gameDao.deletePlatformGames(platform)
    }

    override suspend fun getGameById(id: Int): Result<Game> = suspendRunCatching {
        gameDao.getGameById(id).toDomain()
    }

    override fun searchGame(query: String): Flow<List<Game>> = gameDao.searchGame(query)
        .map { games ->
            games.map { it.toDomain() }
        }

    override fun searchGamesByPlatform(platform: String, query: String): Flow<List<Game>> =
        gameDao.searchGamesByPlatform(platform, query).map { gameEntities ->
            gameEntities.map { it.toDomain() }
        }
}