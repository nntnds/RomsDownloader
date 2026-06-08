package com.nntndscvtcvt.romsdownloader.data.local.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nntndscvtcvt.romsdownloader.data.local.model.FavoriteEntity
import com.nntndscvtcvt.romsdownloader.data.local.model.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun removeFromFavorite(favorite: FavoriteEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites where id = :id)")
    fun isFavoriteExist(id: String): Flow<Boolean>

    @Query("SELECT games.* FROM games INNER JOIN favorites ON games.id = favorites.id")
    fun getAllFavorites(): Flow<List<GameEntity>>
}