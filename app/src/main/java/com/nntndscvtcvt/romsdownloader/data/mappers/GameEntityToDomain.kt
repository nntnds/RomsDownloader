package com.nntndscvtcvt.romsdownloader.data.mappers

import com.nntndscvtcvt.romsdownloader.data.local.model.GameDownloadFileEntity
import com.nntndscvtcvt.romsdownloader.data.local.model.GameDownloadsEntity
import com.nntndscvtcvt.romsdownloader.data.local.model.GameEntity
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadFile
import com.nntndscvtcvt.romsdownloader.domain.model.Downloads
import com.nntndscvtcvt.romsdownloader.domain.model.Game

fun GameEntity.toDomain(): Game = Game(
    databaseID = this.databaseID,
    name = this.name,
    overview = this.overview,
    platform = this.platform,
    genres = this.genres,
    developer = this.developer,
    alternateNames = this.alternateNames,
    coverUrl = this.coverUrl,
    screenshots = this.screenshots,
    downloads = this.downloads.map { it.toDomain() }
)

fun GameDownloadsEntity.toDomain(): Downloads = Downloads(
    title = this.title,
    files = this.files.map { it.toDomain() }
)

fun GameDownloadFileEntity.toDomain(): DownloadFile = DownloadFile(
    type = this.type,
    url = this.url
)