package com.nntndscvtcvt.romsdownloader.data.dto

import com.nntndscvtcvt.romsdownloader.domain.model.DownloadFile
import com.nntndscvtcvt.romsdownloader.domain.model.Downloads
import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity

data class GameModelDto(
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

fun GameModelDto.toDomain(id: String): GameEntity {
    return GameEntity(
        id = id,
        communityRating = this.communityRating ?: "",
        alternateNames = this.alternateNames,
        cooperative = this.cooperative ?: false,
        coverUrl = this.coverUrl ?: "",
        developer = this.developer ?: "",
        downloads = this.downloads.map { it.toDomain() },
        genres = this.genres,
        name = this.name ?: "",
        overview = this.overview ?: "",
        publisher = this.publisher ?: "",
        releaseDate = this.releaseDate ?: "",
        screenshots = this.screenshots,
    )
}

fun DownloadDto.toDomain(): Downloads {
    return Downloads(
        title = this.title,
        fileType = this.fileType,
        size = this.size ?: "",
        zipUrl = this.zipUrl ?: "",
        files = this.files.map { it.toDomain() }
    )
}

fun DownloadFileDto.toDomain(): DownloadFile {
    return DownloadFile(
        type = this.type,
        url = this.url
    )
}