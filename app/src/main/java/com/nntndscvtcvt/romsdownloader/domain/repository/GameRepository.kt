package com.nntndscvtcvt.romsdownloader.domain.repository

import com.nntndscvtcvt.romsdownloader.data.local.model.GameEntity
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadGamesCatalogState
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getAllGames(): Flow<List<Game>>
    fun searchGame(query: String): Flow<List<Game>>
    fun searchGamesByPlatform(platform: String, query: String): Flow<List<Game>>
    fun downloadConsoleGames(consoleName: String): Flow<DownloadGamesCatalogState>
    fun getGamesCount(platform: String): Flow<Int>
    suspend fun getGameById(id: Int): Result<Game>
    suspend fun deletePlatformGames(platform: String): Result<Unit>
}