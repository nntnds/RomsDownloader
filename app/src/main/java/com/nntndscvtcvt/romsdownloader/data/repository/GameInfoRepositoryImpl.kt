package com.nntndscvtcvt.romsdownloader.data.repository

import com.nntndscvtcvt.romsdownloader.data.local.dto.GameDao
import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity
import com.nntndscvtcvt.romsdownloader.domain.repository.GameInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GameInfoRepositoryImpl(
    val gameDao: GameDao
) : GameInfoRepository {
    override fun getGameById(id: String): Flow<Result<GameEntity>> = flow {
        try {
            val result = gameDao.getGameById(id)
            emit(Result.success(result))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}