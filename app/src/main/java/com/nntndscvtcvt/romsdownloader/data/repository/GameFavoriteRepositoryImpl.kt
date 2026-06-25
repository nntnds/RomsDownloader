package com.nntndscvtcvt.romsdownloader.data.repository

import com.nntndscvtcvt.romsdownloader.data.local.dao.FavoriteDao
import com.nntndscvtcvt.romsdownloader.data.local.model.FavoriteEntity
import com.nntndscvtcvt.romsdownloader.data.mappers.toDomain
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import com.nntndscvtcvt.romsdownloader.domain.repository.GameFavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameFavoriteRepositoryImpl(
    private val favoriteDao: FavoriteDao,
) : GameFavoriteRepository {
    override suspend fun addToFavorite(id: Int) {
        favoriteDao.addToFavorite(FavoriteEntity(id))
    }

    override suspend fun removeFromFavorite(id: Int) {
        favoriteDao.removeFromFavorite(FavoriteEntity(id))
    }

    override fun isFavoriteExist(id: Int): Flow<Boolean> {
        return favoriteDao.isFavoriteExist(id)
    }

    override fun getAllFavorites(): Flow<List<Game>> = favoriteDao.getAllFavorites()
        .map { games -> games.map { it.toDomain() } }
}