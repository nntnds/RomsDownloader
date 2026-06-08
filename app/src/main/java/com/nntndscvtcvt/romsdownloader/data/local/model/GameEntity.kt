package com.nntndscvtcvt.romsdownloader.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey
    val id: String,
    val communityRating: String,
    val alternateNames: List<String>,
    val cooperative: Boolean,
    val coverUrl: String,
    val developer: String,
    val downloads: List<GameDownloadsEntity>,
    val genres: List<String>,
    val name: String,
    val overview: String,
    val publisher: String,
    val releaseDate: String,
    val screenshots: List<String>,
)

@Serializable
data class GameDownloadsEntity(
    val fileType: String,
    val files: List<GameDownloadFileEntity>,
    val size: String,
    val title: String,
    val zipUrl: String,
)

@Serializable
data class GameDownloadFileEntity(
    val type: String,
    val url: String
)