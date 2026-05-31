package com.nntndscvtcvt.romsdownloader.presentation.download.components

import android.app.DownloadManager
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadItem

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DownloadsList(
    item: DownloadItem,
    onRefresh: () -> Unit,
    onStop: () -> Unit,
    onTap: () -> Unit,
    onLongPress: () -> Unit,
    isSelected: Boolean,
    isSelectedMode: Boolean
) {
    val stopButton = painterResource(R.drawable.outline_stop_24)
    val refreshButton = painterResource(R.drawable.outline_refresh_24)
    val coverUrl = "https://images.launchbox-app.com/"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .combinedClickable(
                onClick = onTap,
                onLongClick = onLongPress
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (!isSelected) MaterialTheme.colorScheme.surfaceContainerLow
                else MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .weight(0.25f)
                    .aspectRatio(0.7f)
                    .clip(MaterialTheme.shapes.medium),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(coverUrl + item.coverUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.weight(0.75f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.fileName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                when {
                    item.isStopped -> Text(
                        text = "Stopped",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                    item.status == DownloadManager.STATUS_FAILED -> Text(
                        text = "Error",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                    item.status == DownloadManager.STATUS_SUCCESSFUL -> Text(
                        text = "Download finished",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )
                    item.status == DownloadManager.STATUS_RUNNING ||
                            item.status == DownloadManager.STATUS_PENDING -> Text(
                        text = "Downloaded: ${item.downloadedMbs} MB",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (!isSelectedMode) {
                IconButton(onClick = {
                    when {
                        item.isStopped || item.status == DownloadManager.STATUS_FAILED -> onRefresh()
                        else -> onStop()
                    }
                }) {
                    Icon(
                        painter = when {
                            item.isStopped || item.status == DownloadManager.STATUS_FAILED -> refreshButton
                            else -> stopButton
                        },
                        contentDescription = null
                    )
                }
            }
        }
    }
}