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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.domain.model.Game
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

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val backButton = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_left_24)

    LaunchedEffect(platform, query) {
        viewModel.loadGames(platform, query)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.statusBars,
                scrollBehavior = scrollBehavior,
                title = { Text(platform) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = backButton,
                            contentDescription = null,
                            modifier = Modifier.size(Dimens.iconMediumSize)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is SearchResultState.Idle -> {
                Text(" =) ")
            }

            is SearchResultState.Loading -> {
                ShowLoading(Modifier.padding(innerPadding))
            }

            is SearchResultState.Success -> {
                SearchResultContent(
                    games = (uiState as SearchResultState.Success).games,
                    navigateToGameInfo = navigateToGameInfo,
                    innerPadding = innerPadding
                )
            }
        }
    }
}

@Composable
private fun SearchResultContent(
    games: List<Game>,
    navigateToGameInfo: (Int) -> Unit,
    innerPadding: PaddingValues
) {
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
            items = games,
            key = { it.databaseID }
        ) { game ->
            GameCard(
                game = game,
                navigateToGameInfo = navigateToGameInfo,
            )
        }
    }
}