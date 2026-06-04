package com.nntndscvtcvt.romsdownloader.presentation.game_info

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.presentation.components.MyLoadingIndicator
import com.nntndscvtcvt.romsdownloader.presentation.components.MyShowError
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.GameInfoContent
import com.nntndscvtcvt.romsdownloader.presentation.game_info.components.GameInfoTopBar
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameInfoScreen(
    id: String,
    onBack: () -> Unit,
    viewModel: GameInfoViewModel = koinViewModel(key = id)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val isFavorite = (state as? GameInfoState.Success)?.isFavorite ?: false

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
        is GameInfoState.Loading -> MyLoadingIndicator(Modifier)
        is GameInfoState.Error -> MyShowError(Modifier, e = state.error)
        is GameInfoState.Success -> {
            Scaffold(
                topBar = {
                    GameInfoTopBar(
                        isFavorite = state.isFavorite,
                        onBack = onBack,
                        onFavoriteClick = { viewModel.toggleFavorite() }
                    )
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }
            ) { innerPadding ->
                GameInfoContent(
                    modifier = Modifier.padding(innerPadding),
                    state = state.games,
                    downloads = state.gameFileItem,
                    startDownload = { url, fileName ->
                        viewModel.startDownload(url, fileName, state.games)
                    }
                )
            }
        }
    }
}