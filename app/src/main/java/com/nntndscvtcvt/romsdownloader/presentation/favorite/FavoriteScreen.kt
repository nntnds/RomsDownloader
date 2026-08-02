package com.nntndscvtcvt.romsdownloader.presentation.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import com.nntndscvtcvt.romsdownloader.presentation.components.GameCard
import com.nntndscvtcvt.romsdownloader.presentation.components.ShowError
import com.nntndscvtcvt.romsdownloader.presentation.components.ShowLoading
import com.nntndscvtcvt.romsdownloader.presentation.favorite.components.FavoriteScreenTopBar
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteScreen(
    favoriteViewModel: FavoriteViewModel = koinViewModel(),
    navigateToGameInfo: (Int) -> Unit,
    navigateToSettings: () -> Unit
) {
    val uiState by favoriteViewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    FavoriteScreen(
        uiState = uiState,
        scrollBehavior = scrollBehavior,
        navigateToGameInfo = navigateToGameInfo,
        navigateToSettings = navigateToSettings
    )
}

@Composable
private fun FavoriteScreen(
    uiState: FavoriteState,
    scrollBehavior: TopAppBarScrollBehavior,
    navigateToGameInfo: (Int) -> Unit,
    navigateToSettings: () -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FavoriteScreenTopBar(
                navigateToSettings = navigateToSettings,
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is FavoriteState.Empty -> {
                FavoriteEmptyScreen(innerPadding)
            }

            is FavoriteState.Loading -> {
                ShowLoading(Modifier.padding(innerPadding))
            }

            is FavoriteState.Success -> {
                FavoriteContentScreen(
                    favorites = uiState.favorites,
                    innerPadding = innerPadding,
                    navigateToGameInfo = navigateToGameInfo
                )
            }

            is FavoriteState.Error -> {
                ShowError(Modifier.padding(innerPadding), uiState.error)
            }
        }
    }
}

@Composable
private fun FavoriteContentScreen(
    favorites: List<Game>,
    navigateToGameInfo: (Int) -> Unit,
    innerPadding: PaddingValues
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(Dimens.GridColumns),
        horizontalArrangement = Arrangement.spacedBy(Dimens.GridSpacing),
        verticalArrangement = Arrangement.spacedBy(Dimens.GridSpacing),
        contentPadding = PaddingValues(
            start = Dimens.PaddingLarge,
            end = Dimens.PaddingLarge,
            top = innerPadding.calculateTopPadding() + Dimens.PaddingLarge,
            bottom = innerPadding.calculateBottomPadding() + 100.dp
        )
    ) {
        items(
            items = favorites,
            key = { it.databaseID }
        ) { game ->
            GameCard(
                game = game,
                navigateToGameInfo = { navigateToGameInfo(it) },
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun FavoriteEmptyScreen(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_favorites),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}