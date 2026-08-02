package com.nntndscvtcvt.romsdownloader.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import com.nntndscvtcvt.romsdownloader.presentation.components.ShowError
import com.nntndscvtcvt.romsdownloader.presentation.components.ShowLoading
import com.nntndscvtcvt.romsdownloader.presentation.home.components.PlatformSection
import com.nntndscvtcvt.romsdownloader.presentation.home.components.SearchBar
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens
import org.koin.androidx.compose.koinViewModel

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

    HomeScreen(
        query = query,
        uiState = uiState,
        isSearchActive = isSearchActive,
        scrollBehavior = scrollBehavior,
        onSearch = viewModel::searchGame,
        onClear = viewModel::clearSearch,
        onSearchActiveChange = viewModel::toggleIsActive,
        navigateToSettings = navigateToSettings,
        navigateToGameInfo = navigateToGameInfo,
        navigateToSearchResult = navigateToSearchResult,
    )
}

@Composable
private fun HomeScreen(
    query: String,
    uiState: HomeState,
    isSearchActive: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    onSearchActiveChange: (Boolean) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToGameInfo: (Int) -> Unit,
    navigateToSearchResult: (String, String) -> Unit
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SearchBar(
                onSearch = onSearch,
                query = query,
                onClear = onClear,
                isSearchActive = isSearchActive,
                onSearchActiveChange = onSearchActiveChange,
                navigateToSettings = navigateToSettings,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is HomeState.Loading -> {
                ShowLoading(Modifier.padding(innerPadding))
            }

            is HomeState.Success -> {
                val platformList = remember(uiState.games) {
                    uiState.games.entries.toList()
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding() + Dimens.PaddingLarge,
                        bottom = innerPadding.calculateBottomPadding() + 100.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge),
                ) {
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
                ShowError(Modifier.padding(innerPadding), uiState.error)
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenContentPrev() {
    HomeScreen(
        query = "",
        uiState = HomeState.Success(
            games = mapOf(
                "PSP" to listOf(
                    Game(
                        databaseID = 1,
                        name = "Name",
                        overview = "",
                        platform = "",
                        genres = emptyList(),
                        developer = "",
                        alternateNames = emptyList(),
                        coverUrl = "",
                        screenshots = emptyList(),
                        downloads = emptyList()
                    )
                ),
                "PS2" to listOf(
                    Game(
                        databaseID = 2,
                        name = "Name",
                        overview = "",
                        platform = "",
                        genres = emptyList(),
                        developer = "",
                        alternateNames = emptyList(),
                        coverUrl = "",
                        screenshots = emptyList(),
                        downloads = emptyList()
                    )
                ),
                "PS3" to listOf(
                    Game(
                        databaseID = 3,
                        name = "Name",
                        overview = "",
                        platform = "",
                        genres = emptyList(),
                        developer = "",
                        alternateNames = emptyList(),
                        coverUrl = "",
                        screenshots = emptyList(),
                        downloads = emptyList()
                    )
                ),
            ),
        ),
        isSearchActive = false,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        onSearch = {},
        onClear = {},
        onSearchActiveChange = {},
        navigateToSettings = {},
        navigateToGameInfo = {},
        navigateToSearchResult = { _, _ -> }
    )
}