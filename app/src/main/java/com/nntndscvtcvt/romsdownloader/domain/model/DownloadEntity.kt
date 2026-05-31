package com.nntndscvtcvt.romsdownloader.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloads")
data class DownloadEntity(
    @PrimaryKey val downloadId: Long,
    val gameId: String,
    val gameName: String,
    val coverUrl: String,
    val fileName: String,
    val url: String,
    val isStopped: Boolean = false
)