package com.nntndscvtcvt.romsdownloader.domain.repository

import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity
import kotlinx.coroutines.flow.Flow

interface GameInfoRepository {
    fun getGameById(id: String): Flow<Result<GameEntity>>
}