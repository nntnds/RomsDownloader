package com.nntndscvtcvt.romsdownloader.presentation.game_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.presentation.components.ErrorScreen
import com.nntndscvtcvt.romsdownloader.presentation.components.LoadingScreen
import com.nntndscvtcvt.romsdownloader.presentation.components.SectionHeader
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
    val isFavorite = (state as? GameInfoState.Success)?.isFavorite ?: false
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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
        is GameInfoState.Loading -> LoadingScreen(Modifier)
        is GameInfoState.Error -> ErrorScreen(Modifier, e = state.error)
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
                    contentPadding = PaddingValues(bottom = Dimens.PaddingLarge)
                ) {
                    item {
                        GameInfoHeader(state.games)
                    }

                    item {
                        GameInfoOverview(state.games.overview)
                    }

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
                        Column(
                            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
                        ) {
                            SectionHeader(stringResource(R.string.downloads))

                            Spacer(Modifier.size(Dimens.PaddingLarge))

                            state.gameFileItem.forEachIndexed { index, item ->
                                GameInfoDownloads(
                                    downloads = state.gameFileItem,
                                    item = item,
                                    index = index,
                                    startDownload = { url, fileName ->
                                        viewModel.startDownload(url, fileName, state.games)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}