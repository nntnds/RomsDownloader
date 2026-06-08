package com.nntndscvtcvt.romsdownloader.data.mappers

import com.nntndscvtcvt.romsdownloader.data.local.model.GameDownloadFileEntity
import com.nntndscvtcvt.romsdownloader.data.local.model.GameDownloadsEntity
import com.nntndscvtcvt.romsdownloader.data.local.model.GameEntity
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadFile
import com.nntndscvtcvt.romsdownloader.domain.model.Downloads
import com.nntndscvtcvt.romsdownloader.domain.model.Game

fun Game.toEntity(): GameEntity = GameEntity(
    id = this.id,
    communityRating = this.communityRating,
    alternateNames = this.alternateNames,
    cooperative = this.cooperative,
    coverUrl = this.coverUrl,
    developer = this.developer,
    downloads = this.downloads.map { it.toEntity() }, // Маппим вложенный список
    genres = this.genres,
    name = this.name,
    overview = this.overview,
    publisher = this.publisher,
    releaseDate = this.releaseDate,
    screenshots = this.screenshots
)

fun Downloads.toEntity(): GameDownloadsEntity = GameDownloadsEntity(
    fileType = this.fileType,
    files = this.files.map { it.toEntity() },
    size = this.size,
    title = this.title,
    zipUrl = this.zipUrl,
)

fun DownloadFile.toEntity(): GameDownloadFileEntity = GameDownloadFileEntity(
    type = this.type,
    url = this.url
)