package com.nntndscvtcvt.romsdownloader.presentation.game_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity
import com.nntndscvtcvt.romsdownloader.presentation.components.MyShowError
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.GameInfoHeader
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.GameInfoOverview
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.GameInfoScreenshots
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.GameInfoTopBar
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.gameInfoDownloads
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameInfoScreen(
    id: String,
    onBack: () -> Unit,
    viewModel: GameInfoViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val isFavorite by viewModel.isFavorite.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

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
        is GameInfoState.Success -> {
            GameInfoContent(
                state = state.games,
                isFavorite = isFavorite,
                toggleFavorite = { viewModel.toggleFavorite(state.games.id) },
                onBack = onBack,
                downloads = state.gameFileItem,
                snackbarHostState = snackbarHostState,
                startDownload = { url, fileName ->
                    viewModel.startDownload(url, fileName, state.games)
                }
            )
        }

        is GameInfoState.Error -> {
            MyShowError(state.error)
        }

        GameInfoState.Idle -> {}
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GameInfoContent(
    state: GameEntity,
    isFavorite: Boolean,
    toggleFavorite: () -> Unit,
    onBack: () -> Unit,
    downloads: List<GameFileItem>,
    snackbarHostState: SnackbarHostState,
    startDownload: (String, String) -> Unit
) {
    val coverUrl = "https://images.launchbox-app.com//"
    Column {
        GameInfoTopBar(isFavorite, onBack, toggleFavorite)

        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
            ) {
                item {
                    GameInfoHeader(state, coverUrl)
                }

                item {
                    GameInfoOverview(state)
                }

                item {
                    GameInfoScreenshots(state, coverUrl)
                }
                gameInfoDownloads(downloads, startDownload)
                item { Spacer(Modifier.height(12.dp)) }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}