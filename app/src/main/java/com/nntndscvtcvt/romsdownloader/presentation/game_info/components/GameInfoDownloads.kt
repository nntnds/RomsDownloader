package com.nntndscvtcvt.romsdownloader.presentation.game_info.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.presentation.game_info.GameFileItem
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun GameInfoDownloads(
    downloads: List<GameFileItem>,
    item: GameFileItem,
    index: Int,
    startDownload: (String, String) -> Unit
) {
    val leadingIcon = R.drawable.outline_download_24
    val trailingIcon = R.drawable.outline_keyboard_arrow_right_24

    SegmentedListItem(
        onClick = {
            startDownload(item.url, "${item.title}.${item.type}")
        },
        shapes = if (downloads.size == 1) ListItemDefaults.shapes(MaterialTheme.shapes.medium)
            else ListItemDefaults.segmentedShapes(index = index, count = downloads.size),
        modifier = Modifier.padding(horizontal = Dimens.PaddingLarge),
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

        colors = ListItemDefaults.segmentedColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        content = {
            Text(
                text = "${item.title}.${item.type}",
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 4
            )
        }
    )
}