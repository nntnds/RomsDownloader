package com.nntndscvtcvt.romsdownloader.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.presentation.components.ErrorScreen
import com.nntndscvtcvt.romsdownloader.presentation.components.LoadingScreen
import com.nntndscvtcvt.romsdownloader.presentation.home.components.GameCarousel
import com.nntndscvtcvt.romsdownloader.presentation.home.components.PlatformSection
import com.nntndscvtcvt.romsdownloader.presentation.home.components.SearchBar
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navigateToGameInfo: (Int) -> Unit,
    navigateToSearchResult: (String, String) -> Unit,
    navigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val isSearchActive by viewModel.isSearchActive.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SearchBar(
                onSearch = viewModel::searchGame,
                query = query,
                onClear = viewModel::clearSearch,
                isSearchActive = isSearchActive,
                onSearchActiveChange = viewModel::toggleIsActive,
                navigateToSettings = navigateToSettings,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            HomeState.Loading -> {
                LoadingScreen(Modifier.padding(innerPadding))
            }
            is HomeState.Success -> {
                val platformList = remember(state.games) { state.games.entries.toList() }

                LazyColumn(
                    modifier = Modifier.padding(innerPadding),
                    contentPadding = PaddingValues(bottom = Dimens.PaddingLarge),
                    verticalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge),
                ) {
//                    if (query.isEmpty() || !isSearchActive) {
//                        item {
//                            GameCarousel(
//                                games = state.games.values.flatten().take(10),
//                                navigateToGameInfo = navigateToGameInfo,
//                            )
//                        }
//                    }

                    items(
                        items = platformList,
                        key = { it.key }
                    ) { (platform, games) ->
                        PlatformSection(
                            platform = platform,
                            games = games,
                            isSearch = query.isNotEmpty(),
                            query = query,
                            navigateToGameInfo = navigateToGameInfo,
                            navigateToSearchResult = navigateToSearchResult
                        )
                    }
                }
            }
            is HomeState.Error -> {
                ErrorScreen(Modifier.padding(innerPadding), state.error)
            }
        }
    }
}