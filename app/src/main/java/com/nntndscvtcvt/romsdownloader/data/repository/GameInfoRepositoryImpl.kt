package com.nntndscvtcvt.romsdownloader.data.repository

import com.nntndscvtcvt.romsdownloader.data.local.dao.GameDao
import com.nntndscvtcvt.romsdownloader.data.mappers.toDomain
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import com.nntndscvtcvt.romsdownloader.domain.repository.GameInfoRepository

class GameInfoRepositoryImpl(
    val gameDao: GameDao
) : GameInfoRepository {

    override suspend fun getGameById(id: Int): Result<Game> = runCatching {
        gameDao.getGameById(id).toDomain()
    }
}