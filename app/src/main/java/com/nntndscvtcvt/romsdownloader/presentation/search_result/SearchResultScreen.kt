package com.nntndscvtcvt.romsdownloader.presentation.search_result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.presentation.components.GameCard
import com.nntndscvtcvt.romsdownloader.presentation.components.ShowLoading
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchResultScreen(
    platform: String,
    query: String,
    viewModel: SearchResultViewModel = koinViewModel(),
    onBack: () -> Unit,
    navigateToGameInfo: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val backButton = painterResource(R.drawable.outline_keyboard_arrow_left_24)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    LaunchedEffect(platform, query) {
        viewModel.loadGames(platform, query)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.statusBars,
                scrollBehavior = scrollBehavior,
                title = { Text(platform) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = backButton,
                            contentDescription = null,
                            modifier = Modifier.size(Dimens.iconMediumSize)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            SearchResultState.Loading -> {
                ShowLoading(Modifier.padding(innerPadding))
            }

            is SearchResultState.Success -> {
                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(horizontal = Dimens.PaddingLarge),
                    columns = GridCells.Fixed(Dimens.GridColumns),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.GridSpacing),
                    verticalArrangement = Arrangement.spacedBy(Dimens.GridSpacing),
                    contentPadding = PaddingValues(bottom = Dimens.PaddingLarge)
                ) {
                    items(
                        items = state.games,
                        key = { it.databaseID }
                    ) { game ->
                        GameCard(
                            game = game,
                            navigateToGameInfo = navigateToGameInfo,
                            modifier = Modifier
                        )
                    }
                }
            }

            else -> {}
        }
    }
}