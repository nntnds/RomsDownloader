package com.nntndscvtcvt.romsdownloader.domain.repository

import com.nntndscvtcvt.romsdownloader.domain.model.Game
import kotlinx.coroutines.flow.Flow

interface GameFavoriteRepository {
    suspend fun addToFavorite(id: String)
    suspend fun removeFromFavorite(id: String)
    fun isFavoriteExist(id: String): Flow<Boolean>
    fun getAllFavorites(): Flow<List<Game>>
}