package com.nntndscvtcvt.romsdownloader.presentation.download

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.presentation.download.components.DownloadScreenTopBar
import com.nntndscvtcvt.romsdownloader.presentation.download.components.DownloadsList
import com.nntndscvtcvt.romsdownloader.presentation.download.components.SelectionTopBar
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DownloadScreen(
    onNavigate: () -> Unit,
    viewModel: DownloadViewModel = koinViewModel(),
    onGameInfoScreen: (String) -> Unit
) {
    val downloads by viewModel.downloads.collectAsStateWithLifecycle()
    val selectedIds by viewModel.selectedIds.collectAsStateWithLifecycle()
    val isSelected = selectedIds.isNotEmpty()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collectLatest { message ->
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            if (isSelected) {
                SelectionTopBar(
                    selectedCount = selectedIds.size,
                    onClearSelection = { viewModel.clearSelection() },
                    onSelectAll = { viewModel.selectAll() },
                    onDelete = { viewModel.deleteSelected() }
                )
            } else {
                DownloadScreenTopBar(
                    onNavigate = onNavigate,
                    checkCookie = { viewModel.cookieCheck() }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { innerPadding ->
        if (downloads.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(
                    items = downloads,
                    key = { item -> item.url }
                ) { item ->
                    DownloadsList(
                        item = item,
                        onRefresh = { viewModel.retryDownload(item.id) },
                        onStop = { viewModel.stopDownload(item.id) },
                        onTap = {
                            if (isSelected) viewModel.toggleSelection(item.id)
                            else onGameInfoScreen(item.gameId)
                        },
                        onLongPress = { viewModel.toggleSelection(item.id) },
                        isSelected = item.id in selectedIds,
                        isSelectedMode = isSelected
                    )
                }
                item { Spacer(Modifier.size(12.dp)) }
            }
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No downloads yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}