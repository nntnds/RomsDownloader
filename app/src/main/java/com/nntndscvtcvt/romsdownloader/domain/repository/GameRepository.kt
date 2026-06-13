package com.nntndscvtcvt.romsdownloader.domain.repository

import com.nntndscvtcvt.romsdownloader.domain.model.DownloadGamesState
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getAllGames(): Flow<List<Game>>
    fun downloadConsoleGames(consoleName: String): Flow<DownloadGamesState>
    fun getGamesCount(platform: String): Flow<Int>
    suspend fun deletePlatformGames(platform: String): Result<Unit>
}