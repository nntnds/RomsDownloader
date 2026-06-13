package com.nntndscvtcvt.romsdownloader.presentation.settings.components.console

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadGamesState
import com.nntndscvtcvt.romsdownloader.presentation.settings.Console
import com.nntndscvtcvt.romsdownloader.presentation.settings.components.common.IconBox

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ConsoleItem(
    item: Console,
    index: Int,
    totalCount: Int,
    downloadConsoleGame: (String) -> Unit,
    deletePlatformGames: (String) -> Unit,
    state: DownloadGamesState,
    isDownloaded: Boolean,
    gamesCount: Int
) {
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var showRedownloadDialog by rememberSaveable { mutableStateOf(false) }

    if (showDeleteDialog) {
        ConsoleDeleteDialog(
            consoleName = item.consoleName,
            onConfirm = {
                deletePlatformGames(item.platform)
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    if (showRedownloadDialog) {
        ConsoleRedownloadDialog(
            consoleName = item.consoleName,
            onConfirm = {
                downloadConsoleGame(item.consoleName)
                showRedownloadDialog = false
            },
            onDismiss = { showRedownloadDialog = false }
        )
    }

    SegmentedListItem(
        onClick = {
            if (!state.isDownloading) {
                if (isDownloaded) showRedownloadDialog = true
                else downloadConsoleGame(item.consoleName)
            }
        },
        shapes = ListItemDefaults.segmentedShapes(index, totalCount),
        colors = ListItemDefaults.segmentedColors(
            containerColor = if (state.isDownloading) MaterialTheme.colorScheme.surfaceContainerHigh
                else MaterialTheme.colorScheme.surfaceContainer,
            supportingContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            trailingContentColor = MaterialTheme.colorScheme.primary
        ),
        leadingContent = {
            IconBox(item.icon)
        },
        trailingContent = {
            ConsoleTrailingAction(
                state = state,
                onDelete = { showDeleteDialog = true },
                isDownloaded = isDownloaded
            )
        },
        supportingContent = {
            ConsoleSupportingContent(
                gamesCount = gamesCount,
                size = item.size,
            )
        },
        content = {
            Text(
                text = item.consoleName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}