package com.nntndscvtcvt.romsdownloader.domain.repository

import com.nntndscvtcvt.romsdownloader.domain.model.DownloadEntity
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadItem
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    suspend fun checkAccess(sig: String, user: String): Boolean
    fun downloadFile(url: String, sig: String, user: String, fileName: String): Long
    fun getActiveDownloads(): Flow<List<DownloadItem>>

    suspend fun deleteMultiple(ids: List<Long>)
    suspend fun stopDownload(downloadId: Long)
    suspend fun retryDownload(downloadId: Long, sig: String, user: String): Long
    suspend fun deleteDownload(downloadId: Long)
    suspend fun saveDownload(downloadEntity: DownloadEntity)
}