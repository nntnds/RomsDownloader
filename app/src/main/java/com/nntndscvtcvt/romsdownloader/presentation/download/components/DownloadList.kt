package com.nntndscvtcvt.romsdownloader.presentation.download.components

import android.app.DownloadManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.data.utils.Constants.COVER_URL
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadItem
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens

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

    val isSuccess = item.status == DownloadManager.STATUS_SUCCESSFUL
    val isRetryAction = item.isStopped || item.status == DownloadManager.STATUS_FAILED
    val isActionEnabled = !isSelectedMode && !isSuccess


    Card(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(Dimens.CardHeight)
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
                model = COVER_URL + item.coverUrl,
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
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = getStatusText(item),
                    color = getStatusColor(item),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isSelectedMode -> {
                        // TODO
                    }
                    isSuccess -> {
                        Icon(
                            painter = checkCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    else -> {
                        IconButton(
                            onClick = { if (isRetryAction) onRefresh() else onStop() },
                            enabled = isActionEnabled
                        ) {
                            Icon(
                                painter = if (isRetryAction) refreshButton else stopButton,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}