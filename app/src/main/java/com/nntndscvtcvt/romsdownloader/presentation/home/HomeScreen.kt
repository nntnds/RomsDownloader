package com.nntndscvtcvt.romsdownloader.presentation.home

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.presentation.components.MyLazyVerticalGrid
import com.nntndscvtcvt.romsdownloader.presentation.components.MyLoadingIndicator
import com.nntndscvtcvt.romsdownloader.presentation.components.MyShowError
import com.nntndscvtcvt.romsdownloader.presentation.home.components.SearchBar
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    var isSearchActive by rememberSaveable { mutableStateOf(false) }

    LifecycleResumeEffect(Unit) {
        viewModel.clearSearch()
        onPauseOrDispose { }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            SearchBar(
                onSearch = { viewModel.loadData(it) },
                query = query,
                onClear = { viewModel.clearSearch() },
                isSearchActive = isSearchActive,
                onSearchActiveChange = { isSearchActive = it }
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is HomeState.Loading -> MyLoadingIndicator()
            is HomeState.Success -> {
                MyLazyVerticalGrid(
                    modifier = Modifier.padding(innerPadding),
                    gamesData = state.games,
                    onNavigate = {
                        viewModel.clearSearch()
                        onNavigate(it)
                    },
                )
            }
            is HomeState.Error -> MyShowError(state.error)
        }
    }
}