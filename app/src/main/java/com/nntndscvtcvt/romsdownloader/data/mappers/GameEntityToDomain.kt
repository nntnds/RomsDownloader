package com.nntndscvtcvt.romsdownloader.data.mappers

import com.nntndscvtcvt.romsdownloader.data.local.model.GameDownloadFileEntity
import com.nntndscvtcvt.romsdownloader.data.local.model.GameDownloadsEntity
import com.nntndscvtcvt.romsdownloader.data.local.model.GameEntity
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadFile
import com.nntndscvtcvt.romsdownloader.domain.model.Downloads
import com.nntndscvtcvt.romsdownloader.domain.model.Game

fun GameEntity.toDomain(): Game = Game(
    id = this.id,
    communityRating = this.communityRating,
    alternateNames = this.alternateNames,
    cooperative = this.cooperative,
    coverUrl = this.coverUrl,
    developer = this.developer,
    downloads = this.downloads.map { it.toDomain() },
    genres = this.genres,
    name = this.name,
    overview = this.overview,
    publisher = this.publisher,
    releaseDate = this.releaseDate,
    screenshots = this.screenshots,
)

fun GameDownloadsEntity.toDomain(): Downloads = Downloads(
    fileType = this.fileType,
    files = this.files.map { it.toDomain() },
    size = this.size,
    title = this.title,
    zipUrl = this.zipUrl,
)

fun GameDownloadFileEntity.toDomain(): DownloadFile = DownloadFile(
    type = this.type,
    url = this.url
)