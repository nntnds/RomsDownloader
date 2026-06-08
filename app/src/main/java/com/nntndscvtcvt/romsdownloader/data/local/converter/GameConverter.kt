package com.nntndscvtcvt.romsdownloader.data.local.converter

import androidx.room.TypeConverter
import com.nntndscvtcvt.romsdownloader.data.local.model.GameDownloadsEntity
import kotlinx.serialization.json.Json

class GameConverter {
    @TypeConverter
    fun fromList(list: List<String>): String = Json.encodeToString(list)

    @TypeConverter
    fun toList(string: String): List<String> = Json.Default.decodeFromString(string)

    @TypeConverter
    fun fromDownload(downloads: List<GameDownloadsEntity>): String = Json.encodeToString(downloads)

    @TypeConverter
    fun toDownload(string: String): List<GameDownloadsEntity> = Json.Default.decodeFromString(string)
}