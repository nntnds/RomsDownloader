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
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private val downloadManager: DownloadManager by lazy {
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    private companion object {
        const val CHECK_ACCESS_URL = "https://archive.org/download/sony_playstation2_numberssymbols/_Sony%20PlayStation%202_thumb.jpg"
        const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
    }

    override suspend fun checkAccess(sig: String, user: String): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            client.newCall(
                Request.Builder()
                    .url(CHECK_ACCESS_URL)
                    .head()
                    .addHeader("Cookie", createCookieHeader(sig, user))
                    .build()
            ).execute().code == 200
        }.getOrDefault(false)
    }

    override fun downloadFile(url: String, sig: String, user: String, fileName: String): Long {
        return buildRequest(url, fileName, sig, user)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getActiveDownloads(): Flow<List<DownloadItem>> {
        return downloadDao.getAllDownloads().flatMapLatest { entities ->
            flow {
                while (true) {
                    val items = entities.mapNotNull { downloadEntity ->
                        getDownloadItem(downloadEntity)
                    }
                    emit(items)
                    delay(1000L)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun stopDownload(downloadId: Long) {
        runCatching {
            downloadManager.remove(downloadId)
            downloadDao.setStopped(downloadId)
        }.onFailure {
            // TODO()
        }
    }

    override suspend fun retryDownload(downloadId: Long, sig: String, user: String): Long {
        val entity = downloadDao.getById(downloadId) ?: return -1L

        return runCatching {
            downloadManager.remove(downloadId)
            val newId = buildRequest(entity.url, entity.fileName, sig, user)

            if(newId != -1L) {
                downloadDao.delete(downloadId)
                downloadDao.insert(entity.copy(downloadId = newId, isStopped = false))
            }
            newId
        }.getOrElse { -1L }
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

    override suspend fun saveDownload(downloadEntity: DownloadEntity) {
        downloadDao.insert(downloadEntity)
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
        } catch (e: Exception) { -1L }
    }

    private fun getDownloadItem(entity: DownloadEntity): DownloadItem? {
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
            } catch (e: Exception) { createFailedDownloadItem(entity) }
        }
    }

    private fun createCookieHeader(sig: String, user: String): String {
        return "logged-in-sig=$sig; logged-in-user=$user"
    }

    private fun createFailedDownloadItem(entity: DownloadEntity): DownloadItem {
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

    private fun createDownloadItem(entity: DownloadEntity, status: Int, downloaded: Long): DownloadItem {
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