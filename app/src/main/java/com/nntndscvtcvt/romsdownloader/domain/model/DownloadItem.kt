package com.nntndscvtcvt.romsdownloader.domain.model

data class DownloadItem(
    val id: Long,
    val gameId: String,
    val gameName: String,
    val coverUrl: String,
    val fileName: String,
    val status: Int,
    val downloadedMbs: Long,
    val isStopped: Boolean = false,
    val url: String
)
