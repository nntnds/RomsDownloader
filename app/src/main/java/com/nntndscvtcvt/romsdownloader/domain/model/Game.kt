package com.nntndscvtcvt.romsdownloader.domain.model

data class Game(
    val databaseID: Int,
    val name: String,
    val overview: String,
    val platform: String,
    val genres: List<String>,
    val developer: String,
    val alternateNames: List<String>,
    val coverUrl: String,
    val screenshots: List<String>,
    val downloads: List<Downloads>
)

data class Downloads(
    val title: String,
    val files: List<DownloadFile>
)

data class DownloadFile(
    val type: String,
    val url: String
)