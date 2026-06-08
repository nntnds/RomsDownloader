package com.nntndscvtcvt.romsdownloader.data.repository

import com.nntndscvtcvt.romsdownloader.data.local.dto.GameDao
import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity
import com.nntndscvtcvt.romsdownloader.domain.repository.SearchGameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SearchGameRepositoryImpl(
    val gameDao: GameDao
) : SearchGameRepository {
    override fun searchGame(query: String): Flow<Result<List<GameEntity>>> {
        return gameDao.searchGame(query)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}