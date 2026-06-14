package com.nntndscvtcvt.romsdownloader.presentation.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.presentation.components.ShowError
import com.nntndscvtcvt.romsdownloader.presentation.components.GameCard
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
    val state by favoriteViewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FavoriteScreenTopBar(
                navigateToSettings = navigateToSettings,
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        when (val state = state) {
            is FavoriteState.Empty -> {
                Box(
                    Modifier
                        .padding(innerPadding)
                        .fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_favorites),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            is FavoriteState.Loading -> {
                ShowLoading(Modifier.padding(innerPadding))
            }

            is FavoriteState.Success -> {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    columns = GridCells.Fixed(Dimens.GridColumns),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.GridSpacing),
                    verticalArrangement = Arrangement.spacedBy(Dimens.GridSpacing),
                    contentPadding = PaddingValues(Dimens.PaddingLarge)
                ) {
                    items(
                        items = state.favorites,
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

            is FavoriteState.Error -> {
                ShowError(Modifier.padding(innerPadding), state.error)
            }
        }
    }
}