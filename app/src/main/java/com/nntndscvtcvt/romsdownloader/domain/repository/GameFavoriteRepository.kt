package com.nntndscvtcvt.romsdownloader.domain.repository

import com.nntndscvtcvt.romsdownloader.domain.model.Game
import kotlinx.coroutines.flow.Flow

interface GameFavoriteRepository {
    suspend fun addToFavorite(id: Int)
    suspend fun removeFromFavorite(id: Int)
    fun isFavoriteExist(id: Int): Flow<Boolean>
    fun getAllFavorites(): Flow<List<Game>>
}