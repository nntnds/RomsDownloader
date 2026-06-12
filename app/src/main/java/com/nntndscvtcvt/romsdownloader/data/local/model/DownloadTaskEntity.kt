package com.nntndscvtcvt.romsdownloader.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloads")
data class DownloadTaskEntity(
    @PrimaryKey val downloadId: Long,
    val gameId: Int,
    val gameName: String,
    val coverUrl: String,
    val fileName: String,
    val url: String,
    val isStopped: Boolean = false
)