package com.nntndscvtcvt.romsdownloader.data.repository

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.nntndscvtcvt.romsdownloader.data.local.dao.DownloadDao
import com.nntndscvtcvt.romsdownloader.data.local.model.DownloadTaskEntity
import com.nntndscvtcvt.romsdownloader.data.mappers.toEntity
import com.nntndscvtcvt.romsdownloader.data.utils.Constants.CHECK_ACCESS_URL
import com.nntndscvtcvt.romsdownloader.data.utils.Constants.USER_AGENT
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadItem
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadTask
import com.nntndscvtcvt.romsdownloader.domain.repository.DownloadFileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.time.Duration.Companion.milliseconds

class DownloadFileRepositoryImpl(
    private val client: OkHttpClient,
    private val context: Application,
    private val downloadDao: DownloadDao
) : DownloadFileRepository {

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

    override fun getActiveDownloads(): Flow<List<DownloadItem>> = flow {
        while (true) {
            val entities = downloadDao.getAllDownloads()
            val items = entities.map { getDownloadItem(it) }
            emit(items)
            delay(1000L.milliseconds)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun stopDownload(downloadId: Long) {
        downloadManager.remove(downloadId)
        downloadDao.setStopped(downloadId)
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
        downloadManager.remove(downloadId)
        downloadDao.delete(downloadId)
    }

    override suspend fun deleteMultiple(ids: List<Long>) {
        ids.forEach { downloadManager.remove(it) }
        downloadDao.deleteMultiple(ids)
    }

    override suspend fun saveDownload(downloadTask: DownloadTask) {
        downloadDao.insert(downloadTask.toEntity())
    }

    // Private functions

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
        } catch (e: Exception) { -1L }
    }

    private fun getDownloadItem(entity: DownloadTaskEntity): DownloadItem {
        val query = DownloadManager.Query().setFilterById(entity.downloadId)
        val cursor = downloadManager.query(query)

        return cursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                val downloaded = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))

                entity.toDownloadItem(status, downloaded)
            } else entity.toDownloadItem()
        } ?: entity.toDownloadItem()
    }

    private fun createCookieHeader(sig: String, user: String): String {
        return "logged-in-sig=$sig; logged-in-user=$user"
    }

    private fun DownloadTaskEntity.toDownloadItem(
        status: Int = DownloadManager.STATUS_FAILED,
        downloadedBytes: Long = 0L
    ): DownloadItem = DownloadItem(
        id = this.downloadId,
        gameId = this.gameId,
        gameName = this.gameName,
        coverUrl = this.coverUrl,
        fileName = this.fileName,
        status = status,
        downloadedMbs = downloadedBytes / (1024 * 1024),
        isStopped = this.isStopped,
        url = this.url
    )
}