package com.nntndscvtcvt.romsdownloader.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nntndscvtcvt.romsdownloader.data.local.model.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games ORDER BY name ASC LIMIT :limit")
    fun getAllGames(limit: Int = 50): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<GameEntity>)

    @Transaction
    suspend fun syncGames(games: List<GameEntity>) {
        clearAll()
        insertAll(games)
    }

    @Query("DELETE FROM games")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM games")
    suspend fun getCount(): Int

    @Query("SELECT * FROM games WHERE Id = :id")
    suspend fun getGameById(id: String): GameEntity

    @Query("SELECT * FROM games WHERE name LIKE '%' || :query || '%' OR alternateNames LIKE '%' || :query || '%'")
    fun searchGame(query: String): Flow<List<GameEntity>>
}