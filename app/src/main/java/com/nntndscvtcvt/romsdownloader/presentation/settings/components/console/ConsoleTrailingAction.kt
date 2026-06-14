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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadGamesState

@Composable
fun ConsoleTrailingAction(
    state: DownloadGamesState,
    onDelete: () -> Unit,
    isDownloaded: Boolean
) {
    val deleteIcon = painterResource(R.drawable.outline_delete_24)
    val downloadIcon = painterResource(R.drawable.outline_download_24)

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isDownloaded && !state.isDownloading) {
            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    painter = deleteIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        when (state) {
            is DownloadGamesState.Started -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.5.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            else -> {
                Icon(
                    painter = downloadIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}