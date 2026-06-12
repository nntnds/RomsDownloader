package com.nntndscvtcvt.romsdownloader.domain.model

data class DownloadTask(
    val downloadId: Long,
    val gameId: Int,
    val gameName: String,
    val coverUrl: String,
    val fileName: String,
    val url: String,
    val isStopped: Boolean = false
)