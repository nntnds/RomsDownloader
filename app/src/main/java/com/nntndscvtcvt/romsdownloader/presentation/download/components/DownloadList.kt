package com.nntndscvtcvt.romsdownloader.presentation.download.components

import android.app.DownloadManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.data.utils.Constants.COVER_URL
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadItem
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DownloadsList(
    modifier: Modifier = Modifier,
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
    val checkCircle = painterResource(R.drawable.outline_check_circle_24)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.CardHeight)
            .padding(horizontal = Dimens.PaddingLarge)
            .clip(CardDefaults.shape)
            .combinedClickable(
                onClick = onTap,
                onLongClick = onLongPress
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        border = if (isSelected) {
            BorderStroke(
                width = Dimens.BorderStroke,
                color = MaterialTheme.colorScheme.primary
            )
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.PaddingLarge),
            horizontalArrangement = Arrangement.spacedBy(Dimens.GridSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(Dimens.ImageHeight)
                    .aspectRatio(Dimens.ImageAspectRatio)
                    .clip(MaterialTheme.shapes.medium),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(COVER_URL + item.coverUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                error = ColorPainter(MaterialTheme.colorScheme.errorContainer),
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimens.ColumnVerticalArrangement)
            ) {
                Text(
                    text = item.fileName,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                when {
                    item.isStopped -> Text(
                        text = stringResource(R.string.stopped),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )

                    item.status == DownloadManager.STATUS_FAILED -> Text(
                        text = stringResource(R.string.error),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )

                    item.status == DownloadManager.STATUS_SUCCESSFUL -> Text(
                        text = stringResource(R.string.download_finished),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall
                    )

                    item.status == DownloadManager.STATUS_RUNNING ||
                            item.status == DownloadManager.STATUS_PENDING -> Text(
                        text = stringResource(R.string.downloaded_mb, item.downloadedMbs),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isSelectedMode -> { }
                    item.status == DownloadManager.STATUS_SUCCESSFUL -> {
                        Icon(
                            painter = checkCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    else -> {
                        IconButton(
                            onClick = {
                                if (!isSelectedMode) {
                                    when {
                                        item.isStopped || item.status == DownloadManager.STATUS_FAILED -> onRefresh()
                                        else -> onStop()
                                    }
                                }
                            },
                            enabled = !isSelectedMode
                        ) {
                            Icon(
                                painter = when {
                                    item.isStopped || item.status == DownloadManager.STATUS_FAILED -> refreshButton
                                    else -> stopButton
                                },
                                contentDescription = null,
                                tint = if (isSelectedMode) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}