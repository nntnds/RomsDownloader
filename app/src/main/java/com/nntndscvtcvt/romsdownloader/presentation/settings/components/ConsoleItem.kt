package com.nntndscvtcvt.romsdownloader.presentation.settings.components

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadGamesState
import com.nntndscvtcvt.romsdownloader.presentation.settings.Console
import com.nntndscvtcvt.romsdownloader.presentation.settings.components.common.IconBox
import com.nntndscvtcvt.romsdownloader.presentation.settings.components.console.ConsoleDeleteDialog
import com.nntndscvtcvt.romsdownloader.presentation.settings.components.console.ConsoleRedownloadDialog
import com.nntndscvtcvt.romsdownloader.presentation.settings.components.console.ConsoleSupportingContent
import com.nntndscvtcvt.romsdownloader.presentation.settings.components.console.ConsoleTrailingAction

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
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    )
}