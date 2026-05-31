package com.nntndscvtcvt.romsdownloader.data.local

import androidx.room.TypeConverter
import com.nntndscvtcvt.romsdownloader.domain.model.Downloads
import kotlinx.serialization.json.Json

class GameConverter {
    @TypeConverter
    fun fromList(list: List<String>): String = Json.encodeToString(list)

    @TypeConverter
    fun toList(string: String): List<String> = Json.decodeFromString(string)

    @TypeConverter
    fun fromDownload(downloads: List<Downloads>): String = Json.encodeToString(downloads)

    @TypeConverter
    fun toDownload(string: String): List<Downloads> = Json.decodeFromString(string)
}