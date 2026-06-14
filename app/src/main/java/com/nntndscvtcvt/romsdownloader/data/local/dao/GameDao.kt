package com.nntndscvtcvt.romsdownloader.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nntndscvtcvt.romsdownloader.data.local.model.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games ORDER BY name")
    fun getAllGames(): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<GameEntity>)

    @Query("DELETE FROM games WHERE platform = :consoleName")
    suspend fun deletePlatformGames(consoleName: String)

    @Query("SELECT COUNT(*) FROM games WHERE platform = :platform")
    fun getGamesCount(platform: String): Flow<Int>

    @Query("DELETE FROM games WHERE platform = :consoleName")
    suspend fun clearPlatformGames(consoleName: String)

    @Query("SELECT COUNT(*) FROM games")
    suspend fun getCount(): Int

    @Query("SELECT * FROM games WHERE databaseID = :id")
    suspend fun getGameById(id: Int): GameEntity

    @Query("SELECT * FROM games WHERE name LIKE '%' || :query || '%' OR alternateNames LIKE '%' || :query || '%'")
    fun searchGame(query: String): Flow<List<GameEntity>>
}