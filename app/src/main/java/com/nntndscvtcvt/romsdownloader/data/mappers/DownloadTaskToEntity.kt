package com.nntndscvtcvt.romsdownloader.data.mappers

import com.nntndscvtcvt.romsdownloader.data.local.model.DownloadTaskEntity
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadTask

fun DownloadTask.toEntity(): DownloadTaskEntity = DownloadTaskEntity(
    downloadId = this.downloadId,
    gameId = this.gameId,
    gameName = this.gameName,
    coverUrl = this.coverUrl,
    fileName = this.fileName,
    url = this.url,
    isStopped = this.isStopped
)