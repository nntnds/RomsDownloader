package com.nntndscvtcvt.romsdownloader.presentation.download.components

import android.app.DownloadManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadItem

@Composable
fun getStatusText(item: DownloadItem): String {
    return when {
        item.isStopped -> stringResource(R.string.stopped)
        item.status == DownloadManager.STATUS_FAILED -> stringResource(R.string.error)
        item.status == DownloadManager.STATUS_SUCCESSFUL -> stringResource(R.string.download_finished)
        item.status == DownloadManager.STATUS_RUNNING || item.status == DownloadManager.STATUS_PENDING ->
            stringResource(R.string.downloaded_mb, item.downloadedMbs)
        else -> ""
    }
}