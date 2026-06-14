package com.nntndscvtcvt.romsdownloader.presentation.download.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nntndscvtcvt.romsdownloader.R

@Composable
fun SelectionTopBar(
    selectedCount: Int,
    onClearSelection: () -> Unit,
    onSelectAll: () -> Unit,
    onDelete: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val closeButton = painterResource(R.drawable.outline_close_24)
    val selectAllButton = painterResource(R.drawable.outline_select_all_24)
    val deleteButton = painterResource(R.drawable.outline_delete_24)

    TopAppBar(
        windowInsets = WindowInsets.statusBars,
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(R.string.selected_count, selectedCount),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = onClearSelection) {
                Icon(closeButton, null)
            }
        },
        actions = {
            IconButton(onClick = onSelectAll) {
                Icon(selectAllButton, null)
            }
            IconButton(onClick = onDelete) {
                Icon(deleteButton, null)
            }
        }
    )
}