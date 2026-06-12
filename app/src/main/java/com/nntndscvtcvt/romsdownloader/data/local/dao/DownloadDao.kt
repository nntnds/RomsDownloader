package com.nntndscvtcvt.romsdownloader.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nntndscvtcvt.romsdownloader.data.local.model.DownloadTaskEntity

@Dao
interface DownloadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(downloadTaskEntity: DownloadTaskEntity)

    @Query("SELECT * FROM downloads")
    fun getAllDownloads(): List<DownloadTaskEntity>

    @Transaction
    suspend fun replaceDownload(oldId: Long, newEntity: DownloadTaskEntity) {
        delete(oldId)
        insert(newEntity)
    }

    @Query("DELETE FROM downloads WHERE downloadId = :downloadId")
    suspend fun delete(downloadId: Long)

    @Query("DELETE FROM downloads WHERE downloadId IN (:ids)")
    suspend fun deleteMultiple(ids: List<Long>)

    @Query("SELECT * FROM downloads WHERE downloadId = :downloadId")
    suspend fun getById(downloadId: Long): DownloadTaskEntity?

    @Query("UPDATE downloads SET isStopped = 1 WHERE downloadId = :downloadId")
    suspend fun setStopped(downloadId: Long)
}