package com.nntndscvtcvt.romsdownloader.data.local.dao

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

    @Query("SELECT EXISTS(SELECT 1 FROM favorites where databaseId = :id)")
    fun isFavoriteExist(id: Int): Flow<Boolean>

    @Query("SELECT games.* FROM games INNER JOIN favorites ON games.databaseID = favorites.databaseId")
    fun getAllFavorites(): Flow<List<GameEntity>>
}