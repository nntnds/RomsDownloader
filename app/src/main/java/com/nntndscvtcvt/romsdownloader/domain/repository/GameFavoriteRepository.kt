package com.nntndscvtcvt.romsdownloader.domain.repository

import com.nntndscvtcvt.romsdownloader.domain.model.FavoriteEntity
import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity
import kotlinx.coroutines.flow.Flow

interface GameFavoriteRepository {
    suspend fun addToFavorite(favorite: FavoriteEntity)
    suspend fun removeFromFavorite(favorite: FavoriteEntity)
    fun isFavoriteExist(id: String): Flow<Boolean>
    fun getAllFavorites(): Flow<Result<List<GameEntity>>>
}