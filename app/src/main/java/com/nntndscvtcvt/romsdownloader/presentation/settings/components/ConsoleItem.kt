package com.nntndscvtcvt.romsdownloader.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.presentation.settings.Console
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ConsoleItem(
    item: Console,
    index: Int,
    totalCount: Int,
    downloadConsoleGame: (String) -> Unit
) {
    val installIcon = painterResource(R.drawable.outline_download_24)

    SegmentedListItem(
        onClick = {
            downloadConsoleGame(item.consoleName)
        },
        shapes = ListItemDefaults.segmentedShapes(index, totalCount),
        colors = ListItemDefaults.segmentedColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            supportingContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            trailingContentColor = MaterialTheme.colorScheme.primary
        ),
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(0.4f),
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(item.icon),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        trailingContent = {
            Icon(
                painter = installIcon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        supportingContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
            ) {
                Text(item.gamesCount)
                Text("•")
                Text(item.size)
            }
        }
    ) { Text(item.consoleName) }
}