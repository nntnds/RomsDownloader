package com.nntndscvtcvt.romsdownloader.domain.repository

import com.nntndscvtcvt.romsdownloader.domain.model.DownloadItem
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadTask
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    suspend fun checkAccess(sig: String, user: String): Result<Boolean>
    fun downloadFile(url: String, sig: String, user: String, fileName: String): Long
    fun getActiveDownloads(): Flow<List<DownloadItem>>
    suspend fun deleteMultiple(ids: List<Long>)
    suspend fun stopDownload(downloadId: Long)
    suspend fun retryDownload(downloadId: Long, sig: String, user: String): Result<Long>
    suspend fun deleteDownload(downloadId: Long)
    suspend fun saveDownload(downloadTask: DownloadTask)
}