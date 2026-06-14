package com.nntndscvtcvt.romsdownloader.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val databaseID: Int,
    val name: String,
    val overview: String,
    val platform: String,
    val genres: List<String>,
    val developer: String,
    val alternateNames: List<String>,
    val coverUrl: String,
    val screenshots: List<String>,
    val downloads: List<GameDownloadsEntity>
)

@Serializable
data class GameDownloadsEntity(
    val title: String,
    val files: List<GameDownloadFileEntity>
)
@Serializable
data class GameDownloadFileEntity(
    val type: String,
    val url: String
)