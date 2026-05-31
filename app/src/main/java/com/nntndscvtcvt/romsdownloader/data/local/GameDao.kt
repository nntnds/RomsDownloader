package com.nntndscvtcvt.romsdownloader.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games ORDER BY name ASC LIMIT 99")
    fun getAllGames(): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<GameEntity>)

    @Query("DELETE FROM games")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM games")
    suspend fun getCount(): Int

    @Query("SELECT * FROM games WHERE Id = :id")
    suspend fun getGameById(id: String): GameEntity

    @Query("SELECT * FROM games WHERE name LIKE '%' || :query || '%' OR alternateNames LIKE '%' || :query || '%'")
    suspend fun searchGame(query: String): List<GameEntity>
}