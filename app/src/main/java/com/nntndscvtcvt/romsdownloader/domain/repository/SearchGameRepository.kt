package com.nntndscvtcvt.romsdownloader.domain.repository

import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity
import kotlinx.coroutines.flow.Flow

interface SearchGameRepository {
    fun searchGame(query: String): Flow<Result<List<GameEntity>>>
}