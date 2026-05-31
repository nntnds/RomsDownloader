package com.nntndscvtcvt.romsdownloader.domain.repository

import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getAllGames(): Flow<Result<List<GameEntity>>>
    suspend fun sync()
}