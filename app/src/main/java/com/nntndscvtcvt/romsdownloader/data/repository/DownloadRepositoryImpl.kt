package com.nntndscvtcvt.romsdownloader.data.repository

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.nntndscvtcvt.romsdownloader.data.local.dto.DownloadDao
import com.nntndscvtcvt.romsdownloader.data.local.model.DownloadTaskEntity
import com.nntndscvtcvt.romsdownloader.data.mappers.toEntity
import com.nntndscvtcvt.romsdownloader.data.utils.Constants.CHECK_ACCESS_URL
import com.nntndscvtcvt.romsdownloader.data.utils.Constants.USER_AGENT
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadItem
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadTask
import com.nntndscvtcvt.romsdownloader.domain.repository.DownloadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.time.Duration.Companion.milliseconds

class DownloadRepositoryImpl(
    private val client: OkHttpClient,
    private val context: Application,
    private val downloadDao: DownloadDao
) : DownloadRepository {

    private val downloadManager: DownloadManager by lazy {
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    override suspend fun checkAccess(sig: String, user: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            runCatching {
                client.newCall(
                    Request.Builder()
                        .url(CHECK_ACCESS_URL)
                        .head()
                        .addHeader("Cookie", createCookieHeader(sig, user))
                        .build()
                ).execute().code == 200
            }
        }

    override fun downloadFile(url: String, sig: String, user: String, fileName: String): Long {
        return buildRequest(url, fileName, sig, user)
    }

    override fun getActiveDownloads(): Flow<List<DownloadItem>> = callbackFlow {
        val job = launch {
            while (isActive) {
                val entities = downloadDao.getAllDownloads().first()
                val items = entities.mapNotNull { entity ->
                    getDownloadItem(entity)
                }
                trySend(items)
                delay(1000L.milliseconds)
            }
        }

        awaitClose {
            job.cancel()
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun stopDownload(downloadId: Long) {
        runCatching {
            downloadManager.remove(downloadId)
            downloadDao.setStopped(downloadId)
        }.onFailure {  }
    }

    override suspend fun retryDownload(downloadId: Long, sig: String, user: String): Result<Long> =
        runCatching {
            val entity = downloadDao.getById(downloadId)
                ?: return Result.failure(Exception("$downloadId not found"))
            downloadManager.remove(downloadId)

            val newId = buildRequest(entity.url, entity.fileName, sig, user)

            if (newId != -1L) {
                downloadDao.replaceDownload(
                    oldId = downloadId,
                    newEntity = entity.copy(downloadId = newId, isStopped = false)
                )
            }
            newId
        }

    override suspend fun deleteDownload(downloadId: Long) {
        runCatching {
            downloadManager.remove(downloadId)
            downloadDao.delete(downloadId)
        }.onFailure {
            // TODO()
        }
    }

    override suspend fun deleteMultiple(ids: List<Long>) {
        ids.forEach { downloadManager.remove(it) }
        downloadDao.deleteMultiple(ids)
    }

    override suspend fun saveDownload(downloadTask: DownloadTask) {
        downloadDao.insert(downloadTask.toEntity())
    }

    private fun buildRequest(url: String, filename: String, sig: String, user: String): Long {
        return try {
            val request = DownloadManager.Request(url.toUri()).apply {
                setTitle(filename)
                setDescription("Downloading...")
                addRequestHeader("Cookie", createCookieHeader(sig, user))
                addRequestHeader("User-Agent", USER_AGENT)
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
            }
            downloadManager.enqueue(request)
        } catch (e: Exception) {
            -1L
        }
    }

    private fun getDownloadItem(entity: DownloadTaskEntity): DownloadItem? {
        val query = DownloadManager.Query().setFilterById(entity.downloadId)
        val cursor = downloadManager.query(query) ?: return null

        return cursor.use {
            if (it.isClosed || it.count == 0 || !it.moveToFirst()) { // If status is failed
                return@use createFailedDownloadItem(entity)
            }

            return@use try {
                val status = it.getInt(
                    it.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS)
                )
                val downloaded = it.getLong(
                    it.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                )
                createDownloadItem(entity, status, downloaded)
            } catch (e: Exception) {
                createFailedDownloadItem(entity)
            }
        }
    }

    private fun createCookieHeader(sig: String, user: String): String {
        return "logged-in-sig=$sig; logged-in-user=$user"
    }

    private fun createFailedDownloadItem(entity: DownloadTaskEntity): DownloadItem {
        return DownloadItem(
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

    private fun createDownloadItem(
        entity: DownloadTaskEntity,
        status: Int,
        downloaded: Long
    ): DownloadItem {
        return DownloadItem(
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