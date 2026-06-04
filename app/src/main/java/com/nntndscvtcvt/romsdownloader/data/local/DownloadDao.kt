package com.nntndscvtcvt.romsdownloader.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(downloadEntity: DownloadEntity)

    @Query("SELECT * FROM downloads")
    fun getAllDownloads(): Flow<List<DownloadEntity>>

    @Transaction
    suspend fun replaceDownload(oldId: Long, newEntity: DownloadEntity) {
        delete(oldId)
        insert(newEntity)
    }

    @Query("DELETE FROM downloads WHERE downloadId = :downloadId")
    suspend fun delete(downloadId: Long)

    @Query("DELETE FROM downloads WHERE downloadId IN (:ids)")
    suspend fun deleteMultiple(ids: List<Long>)

    @Query("SELECT * FROM downloads WHERE downloadId = :downloadId")
    suspend fun getById(downloadId: Long): DownloadEntity?

    @Query("UPDATE downloads SET isStopped = 1 WHERE downloadId = :downloadId")
    suspend fun setStopped(downloadId: Long)
}