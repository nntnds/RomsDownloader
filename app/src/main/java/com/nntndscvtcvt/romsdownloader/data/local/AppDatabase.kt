package com.nntndscvtcvt.romsdownloader.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nntndscvtcvt.romsdownloader.data.local.converter.GameConverter
import com.nntndscvtcvt.romsdownloader.data.local.dao.DownloadDao
import com.nntndscvtcvt.romsdownloader.data.local.dao.FavoriteDao
import com.nntndscvtcvt.romsdownloader.data.local.dao.GameDao
import com.nntndscvtcvt.romsdownloader.data.local.model.DownloadTaskEntity
import com.nntndscvtcvt.romsdownloader.data.local.model.FavoriteEntity
import com.nntndscvtcvt.romsdownloader.data.local.model.GameEntity

@Database(
    entities = [
        GameEntity::class,
        FavoriteEntity::class,
        DownloadTaskEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(GameConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun downloadDao(): DownloadDao
}