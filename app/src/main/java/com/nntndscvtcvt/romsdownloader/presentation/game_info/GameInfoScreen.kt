package com.nntndscvtcvt.romsdownloader.presentation.game_info

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.presentation.components.SectionHeader
import com.nntndscvtcvt.romsdownloader.presentation.components.ShowError
import com.nntndscvtcvt.romsdownloader.presentation.components.ShowLoading
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.GameInfoDownloads
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.GameInfoHeader
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.GameInfoOverview
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.GameInfoScreenshots
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.GameInfoTopBar
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GameInfoScreen(
    id: Int,
    onBack: () -> Unit,
    viewModel: GameInfoViewModel = koinViewModel(key = id.toString())
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val useExternalDownloader by viewModel.useExternalDownloader.collectAsStateWithLifecycle()
    val isFavorite = (state as? GameInfoState.Success)?.isFavorite ?: false
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val context = LocalContext.current

    LaunchedEffect(id) {
        viewModel.getInfo(id)
    }

    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collectLatest { message ->
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message)
        }
    }

    when (val state = state) {
        is GameInfoState.Loading -> ShowLoading(Modifier)
        is GameInfoState.Error -> ShowError(Modifier, e = state.error)
        is GameInfoState.Success -> {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    GameInfoTopBar(
                        isFavorite = isFavorite,
                        onBack = onBack,
                        onFavoriteClick = { viewModel.toggleFavorite() },
                        scrollBehavior = scrollBehavior
                    )
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
                    contentPadding = PaddingValues(bottom = Dimens.PaddingLarge),
                ) {
                    item { GameInfoHeader(state.games) }

                    item { GameInfoOverview(state.games.overview) }

                    item {
                        Spacer(Modifier.size(Dimens.PaddingLarge))
                        SectionHeader(stringResource(R.string.screenshots))
                        Spacer(Modifier.size(Dimens.PaddingLarge))
                    }

                    item {
                        GameInfoScreenshots(state.games)
                        Spacer(Modifier.size(Dimens.PaddingLarge))
                    }

                    item {
                        SectionHeader(stringResource(R.string.downloads))
                        Spacer(Modifier.size(Dimens.PaddingLarge))
                    }

                    itemsIndexed(
                        items = state.gameFileItem,
                        key = { _, item -> item.url }
                    ) { index, item ->
                        GameInfoDownloads(
                            downloads = state.gameFileItem,
                            item = item,
                            index = index,
                            startDownload = { url, fileName ->
                                if (useExternalDownloader) {
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        setDataAndType(url.toUri(), "application/octet-stream")
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                    try {
                                        context.startActivity(Intent.createChooser(intent, "Download with..."))
                                    } catch (e: Exception) { }
                                } else {
                                    viewModel.startDownload(url, fileName, state.games)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}