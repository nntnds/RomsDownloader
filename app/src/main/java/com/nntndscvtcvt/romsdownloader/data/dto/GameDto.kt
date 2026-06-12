package com.nntndscvtcvt.romsdownloader.data.dto

data class GameDto(
    val name: String? = null,
    val overview: String? = null,
    val platform: String? = null,
    val genres: List<String>? = null,
    val developer: String? = null,
    val alternateNames: List<String>? = null,
    val databaseId: Int? = null,
    val coverUrl: String? = null,
    val screenshots: List<String>? = null,
    val downloads: List<DownloadDto>? = null
)

data class DownloadDto(
    val title: String? = null,
    val files: List<DownloadFileDto>? = null
)

data class DownloadFileDto(
    val type: String? = null,
    val url: String? = null
)