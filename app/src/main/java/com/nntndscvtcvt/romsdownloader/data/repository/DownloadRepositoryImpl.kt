package com.nntndscvtcvt.romsdownloader.data.repository

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.nntndscvtcvt.romsdownloader.data.local.DownloadDao
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadEntity
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadItem
import com.nntndscvtcvt.romsdownloader.domain.repository.DownloadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class DownloadRepositoryImpl(
    private val client: OkHttpClient,
    private val context: Application,
    private val downloadDao: DownloadDao
) : DownloadRepository {
    override suspend fun checkAccess(sig: String, user: String): Boolean = withContext(Dispatchers.IO) {
            runCatching {
                client.newCall(
                    Request.Builder()
                        .url("https://archive.org/download/sony_playstation2_numberssymbols/_Sony%20PlayStation%202_thumb.jpg")
                        .head()
                        .addHeader("Cookie", "logged-in-sig=$sig; logged-in-user=$user")
                        .build()
                ).execute().code == 200
            }.getOrDefault(false)
        }

    override fun downloadFile(
        url: String,
        sig: String,
        user: String,
        fileName: String
    ): Long {
        return buildRequest(url, fileName, sig, user)
    }

    override suspend fun stopDownload(downloadId: Long) {
        val entity = downloadDao.getById(downloadId) ?: return
        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.remove(downloadId)
        downloadDao.setStopped(downloadId)
    }

    override suspend fun retryDownload(downloadId: Long, sig: String, user: String): Long {
        val entity = downloadDao.getById(downloadId) ?: return -1L
        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.remove(downloadId)
        val newId = buildRequest(entity.url, entity.fileName, sig, user)
        downloadDao.delete(downloadId)
        downloadDao.insert(entity.copy(downloadId = newId, isStopped = false))
        return newId
    }

    override suspend fun deleteDownload(downloadId: Long) {
        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.remove(downloadId)
        downloadDao.delete(downloadId)
    }

    override fun getActiveDownloads(): Flow<List<DownloadItem>> {
        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        return downloadDao.getAllDownloads().flatMapLatest { entities ->
            flow {
                while (true) {
                    val items = entities.mapNotNull { entity ->
                        val query = DownloadManager.Query().setFilterById(entity.downloadId)
                        val cursor = manager.query(query) ?: return@mapNotNull null

                        cursor.use {
                            if (!it.moveToFirst()) {
                                return@mapNotNull DownloadItem(
                                    id = entity.downloadId,
                                    gameId = entity.gameId,
                                    gameName = entity.gameName,
                                    coverUrl = entity.coverUrl,
                                    fileName = entity.fileName,
                                    status = DownloadManager.STATUS_FAILED,
                                    downloadedMbs = 0,
                                    isStopped = entity.isStopped,
                                    url = entity.url
                                )
                            }

                            val downloaded = it.getLong(it.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                            val status = it.getInt(it.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))

                            DownloadItem(
                                id = entity.downloadId,
                                gameId = entity.gameId,
                                gameName = entity.gameName,
                                coverUrl = entity.coverUrl,
                                fileName = entity.fileName,
                                status = status,
                                downloadedMbs = downloaded / (1024 * 1024),
                                isStopped = entity.isStopped,
                                url = entity.url
                            )
                        }
                    }
                    emit(items)
                    delay(500L)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun saveDownload(downloadEntity: DownloadEntity) {
        downloadDao.insert(downloadEntity)
    }

    private fun buildRequest(url: String, filename: String, sig: String, user: String): Long {
        val request = DownloadManager.Request(url.toUri()).apply {
            setTitle(filename)
            setDescription("Downloading...")
            addRequestHeader("Cookie", "logged-in-sig=$sig; logged-in-user=$user")
            addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
        }
        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return manager.enqueue(request)
    }
}