package com.nntndscvtcvt.romsdownloader.presentation.download

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.presentation.components.ShowLoading
import com.nntndscvtcvt.romsdownloader.presentation.download.components.DownloadScreenTopBar
import com.nntndscvtcvt.romsdownloader.presentation.download.components.DownloadsList
import com.nntndscvtcvt.romsdownloader.presentation.download.components.SelectionTopBar
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DownloadScreen(
    navigateToSettings: () -> Unit,
    viewModel: DownloadViewModel = koinViewModel(),
    navigateToGameInfo: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedIds by viewModel.selectedIds.collectAsStateWithLifecycle()

    val isSelectionMode by remember {
        derivedStateOf { selectedIds.isNotEmpty() }
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (isSelectionMode) {
                SelectionTopBar(
                    onClearSelection = viewModel::clearSelection,
                    onSelectAll = viewModel::selectAll,
                    onDelete = viewModel::deleteSelected,
                    selectedCount = selectedIds.size,
                    scrollBehavior = scrollBehavior
                )
            } else {
                DownloadScreenTopBar(navigateToSettings, scrollBehavior)
            }
        }
    ) { innerPadding ->
        when(val state = uiState) {
            is DownloadState.Loading -> { ShowLoading(Modifier.padding(innerPadding)) }
            is DownloadState.Empty -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_downloads),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            is DownloadState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(Dimens.ColumnVerticalArrangement),
                    contentPadding = PaddingValues(
                        start = Dimens.PaddingLarge,
                        end = Dimens.PaddingLarge,
                        top = innerPadding.calculateTopPadding() + Dimens.PaddingLarge,
                        bottom = innerPadding.calculateBottomPadding() + 100.dp
                    )
                ) {
                    items(
                        items = state.downloads,
                        key = { it.id }
                    ) { item ->
                        DownloadsList(
                            modifier = Modifier.animateItem(),
                            item = item,
                            onRefresh = { viewModel.retryDownload(item.id) },
                            onStop = { viewModel.stopDownload(item.id) },
                            onTap = {
                                if (isSelectionMode) viewModel.toggleSelection(item.id)
                                else navigateToGameInfo(item.gameId)
                            },
                            onLongPress = { viewModel.toggleSelection(item.id) },
                            isSelected = item.id in selectedIds,
                            isSelectedMode = isSelectionMode
                        )
                    }
                }
            }
        }
    }
}