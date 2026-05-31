package com.nntndscvtcvt.romsdownloader.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.presentation.components.MyLazyVerticalGrid
import com.nntndscvtcvt.romsdownloader.presentation.components.MyLoadingIndicator
import com.nntndscvtcvt.romsdownloader.presentation.components.MyShowError
import com.nntndscvtcvt.romsdownloader.presentation.home.components.HomeScreenTopBar
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column {
        HomeScreenTopBar()

        when(val state = uiState) {
            is HomeState.Loading -> {
                MyLoadingIndicator()
            }
            is HomeState.Success -> {
                MyLazyVerticalGrid(
                    gamesData = state.games,
                    onNavigate = onNavigate,
                )
            }
            is HomeState.Error -> {
                MyShowError(state.error)
            }
        }
    }

}