package com.nntndscvtcvt.romsdownloader.presentation.download.components

//import android.app.DownloadManager
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.ListItemDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.SegmentedListItem
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import coil3.compose.AsyncImage
//import com.example.romsdownloader.domain.model.DownloadItem
//
//@OptIn(ExperimentalMaterial3ExpressiveApi::class)
//@Composable
//fun MySegmentedListItem(
//    index: Int,
//    downloadsSize: Int,
//    onNavigate: () -> Unit,
//    item: DownloadItem,
//) {
//    val coverUrl = "https://images.launchbox-app.com/"
//
//    SegmentedListItem(
//        onClick = onNavigate,
//        shapes = if (downloadsSize == 1) ListItemDefaults.shapes(MaterialTheme.shapes.large)
//                else ListItemDefaults.segmentedShapes(index = index, count = downloadsSize),
//        modifier = Modifier.padding(horizontal = 12.dp),
//        leadingContent = {
//            AsyncImage(
//                modifier = Modifier
//                    .width(64.dp)
//                    .aspectRatio(0.72f)
//                    .clip(MaterialTheme.shapes.small),
//                model = coverUrl + item.coverUrl,
//                contentDescription = null,
//                contentScale = ContentScale.Crop
//            )
//        },
//        supportingContent = {
//            when (item.status) {
//                DownloadManager.STATUS_RUNNING,
//                DownloadManager.STATUS_PENDING -> {
//                    Text(
//                        text = "Downloaded: ${item.downloadedMbs} MB",
//                        color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        style = MaterialTheme.typography.bodySmall,
//                    )
//                }
//
//                DownloadManager.STATUS_SUCCESSFUL -> {
//                    Text(
//                        text = "Download finished",
//                        color = MaterialTheme.colorScheme.primary,
//                        style = MaterialTheme.typography.bodySmall,
//                    )
//                }
//
//                DownloadManager.STATUS_FAILED -> {
//                    Text(
//                        text = "Error",
//                        color = MaterialTheme.colorScheme.error,
//                        style = MaterialTheme.typography.bodySmall,
//                    )
//                }
//
//                DownloadManager.STATUS_PAUSED -> {
//                    Text(
//                        text = "Paused",
//                        color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        style = MaterialTheme.typography.bodySmall,
//                    )
//                }
//            }
//        },
//        trailingContent = {
//            IconButton(onClick = {  }) {
//
//            }
//        },
//        colors = ListItemDefaults.segmentedColors(
//            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
//        ),
//    ) {
//        Text(
//            text = item.fileName,
//            style = MaterialTheme.typography.titleSmall,
//            maxLines = 3,
//            overflow = TextOverflow.Ellipsis
//        )
//    }
//}