package com.nntndscvtcvt.romsdownloader.presentation.download.components

import android.app.DownloadManager
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadItem

@Composable
fun getStatusColor(item: DownloadItem): Color {
    return when {
        item.status == DownloadManager.STATUS_FAILED -> MaterialTheme.colorScheme.error
        item.status == DownloadManager.STATUS_SUCCESSFUL -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}