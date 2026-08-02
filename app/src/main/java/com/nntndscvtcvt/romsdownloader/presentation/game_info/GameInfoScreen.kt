package com.nntndscvtcvt.romsdownloader.presentation.game_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import com.nntndscvtcvt.romsdownloader.domain.model.GameFileItem
import com.nntndscvtcvt.romsdownloader.presentation.components.SectionHeader
import com.nntndscvtcvt.romsdownloader.presentation.components.ShowError
import com.nntndscvtcvt.romsdownloader.presentation.components.ShowLoading
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.GameInfoDownloads
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.GameInfoHeader
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.GameInfoOverview
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.GameInfoScreenshots
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.GameInfoTopBar
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens
import com.nntndscvtcvt.romsdownloader.presentation.utils.launchExternalDownload
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameInfoScreen(
    id: Int,
    onBack: () -> Unit,
    viewModel: GameInfoViewModel = koinViewModel(key = id.toString())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val useExternalDownloader by viewModel.useExternalDownloader.collectAsStateWithLifecycle()
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

    GameInfoScreen(
        uiState = uiState,
        onBack = onBack,
        toggleFavorite = viewModel::toggleFavorite,
        notifyNoExternalDownloader = viewModel::notifyNoExternalDownloader,
        scrollBehavior = scrollBehavior,
        snackbarHostState = snackbarHostState,
        useExternalDownloader = useExternalDownloader

    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun GameInfoScreen(
    uiState: GameInfoState,
    onBack: () -> Unit,
    toggleFavorite: () -> Unit,
    notifyNoExternalDownloader: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    snackbarHostState: SnackbarHostState,
    useExternalDownloader: Boolean
) {
    when (uiState) {
        is GameInfoState.Loading -> ShowLoading()
        is GameInfoState.Error -> ShowError(e = uiState.error)
        is GameInfoState.Success -> {
            GameInfoContent(
                scrollBehavior = scrollBehavior,
                isFavorite = uiState.isFavorite,
                onBack = onBack,
                toggleFavorite = toggleFavorite,
                snackbarHostState = snackbarHostState,
                game = uiState.games,
                gameFileItem = uiState.gameFileItem,
                useExternalDownloader = useExternalDownloader,
                notifyNoExternalDownloader = notifyNoExternalDownloader
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun GameInfoContent(
    scrollBehavior: TopAppBarScrollBehavior,
    isFavorite: Boolean,
    onBack: () -> Unit,
    toggleFavorite: () -> Unit,
    snackbarHostState: SnackbarHostState,
    game: Game,
    gameFileItem: List<GameFileItem>,
    useExternalDownloader: Boolean,
    notifyNoExternalDownloader: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GameInfoTopBar(
                isFavorite = isFavorite,
                onBack = onBack,
                onFavoriteClick = toggleFavorite,
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
            item {
                GameInfoHeader(
                    coverUrl = game.coverUrl,
                    name = game.name,
                    developer = game.developer,
                    platform = game.platform,
                    genres = game.genres
                )
            }

            item { GameInfoOverview(game.overview) }

            item {
                Spacer(Modifier.size(Dimens.PaddingLarge))
                SectionHeader(stringResource(R.string.screenshots))
                Spacer(Modifier.size(Dimens.PaddingLarge))
            }

            item {
                GameInfoScreenshots(
                    screenshots = game.screenshots
                )
                Spacer(Modifier.size(Dimens.PaddingLarge))
            }

            item {
                SectionHeader(stringResource(R.string.downloads))
                Spacer(Modifier.size(Dimens.PaddingLarge))
            }

            itemsIndexed(
                items = gameFileItem,
                key = { _, item -> item.url }
            ) { index, item ->
                GameInfoDownloads(
                    totalCount = gameFileItem.size,
                    item = item,
                    index = index,
                    startDownload = { url, _ ->
                        if (useExternalDownloader) {
                            if (!launchExternalDownload(url, context)) {
                                notifyNoExternalDownloader()
                            }
                        } else {
                            /* TODO Добавить загрузчик */
                        }
                    }
                )
            }
        }
    }
}