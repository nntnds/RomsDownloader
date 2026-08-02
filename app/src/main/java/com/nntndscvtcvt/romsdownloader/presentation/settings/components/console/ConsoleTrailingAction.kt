package com.nntndscvtcvt.romsdownloader.presentation.settings.components.console

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadGamesCatalogState

@Composable
fun ConsoleTrailingAction(
    state: DownloadGamesCatalogState,
    onDelete: () -> Unit,
    isDownloaded: Boolean
) {
    val deleteIcon = ImageVector.vectorResource(R.drawable.outline_delete_24)
    val downloadIcon = ImageVector.vectorResource(R.drawable.outline_download_24)

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isDownloaded && !state.isDownloading) {
            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    imageVector = deleteIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        when (state) {
            is DownloadGamesCatalogState.Started -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.5.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            else -> {
                Icon(
                    imageVector = downloadIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}