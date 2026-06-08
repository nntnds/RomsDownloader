package com.nntndscvtcvt.romsdownloader.data.repository

import com.nntndscvtcvt.romsdownloader.data.local.dto.FavoriteDao
import com.nntndscvtcvt.romsdownloader.data.local.dto.GameDao
import com.nntndscvtcvt.romsdownloader.domain.model.FavoriteEntity
import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity
import com.nntndscvtcvt.romsdownloader.domain.repository.GameFavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GameFavoriteRepositoryImpl(
    private val favoriteDao: FavoriteDao,
    private val gameDao: GameDao
) : GameFavoriteRepository {
    override suspend fun addToFavorite(favorite: FavoriteEntity) {
        favoriteDao.addToFavorite(favorite)
    }

    override suspend fun removeFromFavorite(favorite: FavoriteEntity) {
        favoriteDao.removeFromFavorite(favorite)
    }

    override fun isFavoriteExist(id: String): Flow<Boolean> {
        return favoriteDao.isFavoriteExist(id)
    }

    override fun getAllFavorites(): Flow<Result<List<GameEntity>>> {
        return favoriteDao.getAllFavorites()
            .map { favorites ->
                val result = favorites.mapNotNull { favorite ->
                    gameDao.getGameById(favorite.id)
                }
                Result.success(result)
            }
            .catch { emit(Result.failure(it)) }
    }
}