package com.nntndscvtcvt.romsdownloader.data.repository

import com.nntndscvtcvt.romsdownloader.data.local.dto.FavoriteDao
import com.nntndscvtcvt.romsdownloader.data.local.model.FavoriteEntity
import com.nntndscvtcvt.romsdownloader.data.mappers.toDomain
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import com.nntndscvtcvt.romsdownloader.domain.repository.GameFavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameFavoriteRepositoryImpl(
    private val favoriteDao: FavoriteDao,
) : GameFavoriteRepository {
    override suspend fun addToFavorite(id: String) {
        favoriteDao.addToFavorite(FavoriteEntity(id))
    }

    override suspend fun removeFromFavorite(id: String) {
        favoriteDao.removeFromFavorite(FavoriteEntity(id))
    }

    override fun isFavoriteExist(id: String): Flow<Boolean> {
        return favoriteDao.isFavoriteExist(id)
    }

    override fun getAllFavorites(): Flow<List<Game>> {
        return favoriteDao.getAllFavorites()
            .map { games -> games.map { it.toDomain() } }
    }
}