package com.nntndscvtcvt.romsdownloader.domain.model

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
    val downloads: List<Downloads>,
    val genres: List<String>,
    val name: String,
    val overview: String,
    val publisher: String,
    val releaseDate: String,
    val screenshots: List<String>,
)

@Serializable
data class Downloads(
    val fileType: String,
    val files: List<DownloadFile>,
    val size: String,
    val title: String,
    val zipUrl: String,
)

@Serializable
data class DownloadFile(
    val type: String,
    val url: String
)