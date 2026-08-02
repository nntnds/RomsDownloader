package com.nntndscvtcvt.romsdownloader.presentation.game_info.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.domain.model.GameFileItem
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun GameInfoDownloads(
    totalCount: Int,
    item: GameFileItem,
    index: Int,
    startDownload: (String, String) -> Unit
) {
    val leadingIcon = ImageVector.vectorResource(R.drawable.outline_download_24)
    val trailingIcon = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_right_24)

    SegmentedListItem(
        onClick = {
            startDownload(item.url, "${item.title}.${item.type}")
        },
        shapes = if (totalCount == 1) ListItemDefaults.shapes(MaterialTheme.shapes.medium)
            else ListItemDefaults.segmentedShapes(index, totalCount),
        modifier = Modifier.padding(horizontal = Dimens.PaddingLarge),
        leadingContent = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = {
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        colors = ListItemDefaults.segmentedColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Text(
            text = "${item.title}.${item.type}",
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 4
        )
    }
}