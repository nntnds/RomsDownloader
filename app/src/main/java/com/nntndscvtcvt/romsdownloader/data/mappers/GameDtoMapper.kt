package com.nntndscvtcvt.romsdownloader.data.mappers

import com.nntndscvtcvt.romsdownloader.data.dto.DownloadDto
import com.nntndscvtcvt.romsdownloader.data.dto.DownloadFileDto
import com.nntndscvtcvt.romsdownloader.data.dto.GameDto
import com.nntndscvtcvt.romsdownloader.data.local.model.GameDownloadFileEntity
import com.nntndscvtcvt.romsdownloader.data.local.model.GameDownloadsEntity
import com.nntndscvtcvt.romsdownloader.data.local.model.GameEntity

fun GameDto.toEntity(id: String): GameEntity = GameEntity(
        id = id,
        communityRating = this.communityRating ?: "",
        alternateNames = this.alternateNames,
        cooperative = this.cooperative ?: false,
        coverUrl = this.coverUrl ?: "",
        developer = this.developer ?: "",
        downloads = this.downloads.map { it.toEntity() },
        genres = this.genres,
        name = this.name ?: "",
        overview = this.overview ?: "",
        publisher = this.publisher ?: "",
        releaseDate = this.releaseDate ?: "",
        screenshots = this.screenshots,
)

fun DownloadDto.toEntity(): GameDownloadsEntity = GameDownloadsEntity(
    title = this.title,
    fileType = this.fileType,
    size = this.size ?: "",
    zipUrl = this.zipUrl ?: "",
    files = this.files.map { it.toEntity() }
)

fun DownloadFileDto.toEntity(): GameDownloadFileEntity = GameDownloadFileEntity(
    type = this.type,
    url = this.url
)