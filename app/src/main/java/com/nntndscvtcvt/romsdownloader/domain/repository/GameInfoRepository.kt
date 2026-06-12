package com.nntndscvtcvt.romsdownloader.domain.repository

import com.nntndscvtcvt.romsdownloader.domain.model.Game

interface GameInfoRepository {
    suspend fun getGameById(id: Int): Result<Game>
}