package com.nntndscvtcvt.romsdownloader.data.dto

data class GameDto(
    val communityRating: String? = "",
    val alternateNames: List<String> = emptyList(),
    val cooperative: Boolean? = false,
    val coverUrl: String? = "",
    val developer: String? = "",
    val downloads: List<DownloadDto> = emptyList(),
    val genres: List<String> = emptyList(),
    val name: String? = "",
    val overview: String? = "",
    val publisher: String? = "",
    val releaseDate: String? = "",
    val screenshots: List<String> = emptyList(),
)

data class DownloadDto(
    val title: String = "",
    val fileType: String = "",
    val size: String? = "",
    val zipUrl: String? = "",
    val files: List<DownloadFileDto> = emptyList()
)

data class DownloadFileDto(
    val type: String = "",
    val url: String = ""
)