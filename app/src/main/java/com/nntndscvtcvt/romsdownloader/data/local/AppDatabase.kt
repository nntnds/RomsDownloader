package com.nntndscvtcvt.romsdownloader.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nntndscvtcvt.romsdownloader.data.local.converter.GameConverter
import com.nntndscvtcvt.romsdownloader.data.local.dto.DownloadDao
import com.nntndscvtcvt.romsdownloader.data.local.dto.FavoriteDao
import com.nntndscvtcvt.romsdownloader.data.local.dto.GameDao
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadEntity
import com.nntndscvtcvt.romsdownloader.domain.model.FavoriteEntity
import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity

@Database(entities = [GameEntity::class, FavoriteEntity::class, DownloadEntity::class], version = 1, exportSchema = false)
@TypeConverters(GameConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun downloadDao(): DownloadDao
}