package com.nntndscvtcvt.romsdownloader.presentation.game_info.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.presentation.game_info.GameFileItem

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun LazyListScope.gameInfoDownloads(
    downloads: List<GameFileItem>, startDownload: (String, String) -> Unit
) {
    val leadingIcon = R.drawable.outline_download_24
    val trailingIcon = R.drawable.outline_keyboard_arrow_right_24

    item {
        Text(
            text = "Downloads",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 8.dp),
        )
    }

    itemsIndexed(
        items = downloads, key = { _, item -> item.url }) { index, item ->
        SegmentedListItem(
            modifier = Modifier.padding(horizontal = 12.dp),
            onClick = {
                startDownload(item.url, "${item.title}.${item.type}")
            },
            leadingContent = {
                Icon(
                    painter = painterResource(leadingIcon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            trailingContent = {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(trailingIcon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            shapes = if (downloads.size == 1) ListItemDefaults.shapes(MaterialTheme.shapes.medium)
            else ListItemDefaults.segmentedShapes(index = index, count = downloads.size),

            colors = ListItemDefaults.segmentedColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            overlineContent = {
                Text(
                    text = "${item.title}.${item.type}",
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Text(
                    text = item.shortenUrl, style = MaterialTheme.typography.bodySmall, maxLines = 1
                )
            })
        { }
    }
}