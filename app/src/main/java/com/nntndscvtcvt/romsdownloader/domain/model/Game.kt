package com.nntndscvtcvt.romsdownloader.domain.model

data class Game(
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

data class Downloads(
    val fileType: String,
    val files: List<DownloadFile>,
    val size: String,
    val title: String,
    val zipUrl: String,
)

data class DownloadFile(
    val type: String,
    val url: String
)