package com.nntndscvtcvt.romsdownloader.data.repository

import com.nntndscvtcvt.romsdownloader.data.local.GameDao
import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity
import com.nntndscvtcvt.romsdownloader.domain.repository.SearchGameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchGameRepositoryImpl(
    val gameDao: GameDao
) : SearchGameRepository {
    override fun searchGame(query: String): Flow<Result<List<GameEntity>>> = flow {
        try {
            val result = gameDao.searchGame(query)
            emit(Result.success(result))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}