package com.nntndscvtcvt.romsdownloader.data.repository

import com.nntndscvtcvt.romsdownloader.data.local.dao.GameDao
import com.nntndscvtcvt.romsdownloader.data.mappers.toDomain
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import com.nntndscvtcvt.romsdownloader.domain.repository.SearchGameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchGameRepositoryImpl(
    val gameDao: GameDao
) : SearchGameRepository {
    override fun searchGame(query: String): Flow<List<Game>> {
        return gameDao.searchGame(query)
            .map { games -> games.map { it.toDomain() } }
    }
}