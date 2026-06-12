package com.nntndscvtcvt.romsdownloader.data.mappers

import com.nntndscvtcvt.romsdownloader.data.dto.DownloadDto
import com.nntndscvtcvt.romsdownloader.data.dto.DownloadFileDto
import com.nntndscvtcvt.romsdownloader.data.dto.GameDto
import com.nntndscvtcvt.romsdownloader.data.local.model.GameDownloadFileEntity
import com.nntndscvtcvt.romsdownloader.data.local.model.GameDownloadsEntity
import com.nntndscvtcvt.romsdownloader.data.local.model.GameEntity

fun GameDto.toEntity(): GameEntity = GameEntity(
    databaseID = this.databaseId ?: 0,
    name = this.name ?: "",
    overview = this.overview ?: "",
    platform = this.platform ?: "",
    genres = this.genres ?: emptyList(),
    developer = this.developer ?: "",
    alternateNames = this.alternateNames ?: emptyList(),
    coverUrl = this.coverUrl ?: "",
    screenshots = this.screenshots ?: emptyList(),
    downloads = this.downloads?.map { it.toEntity() } ?: emptyList()
)

fun DownloadDto.toEntity(): GameDownloadsEntity = GameDownloadsEntity(
    title = this.title ?: "",
    files = this.files?.map { it.toEntity() } ?: emptyList()
)

fun DownloadFileDto.toEntity(): GameDownloadFileEntity = GameDownloadFileEntity(
    type = this.type ?: "",
    url = this.url ?: ""
)