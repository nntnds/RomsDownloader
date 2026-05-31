package com.nntndscvtcvt.romsdownloader.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.presentation.components.MyLazyVerticalGrid
import com.nntndscvtcvt.romsdownloader.presentation.components.MyShowError
import com.nntndscvtcvt.romsdownloader.presentation.search.components.SearchBar
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(), onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Column {
        SearchBar(
            onSearch = { viewModel.loadData(it) }
        )

        when (val state = uiState) {
            is SearchState.Idle -> {}
            is SearchState.Success -> {
                if (state.games.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Not Found")
                    }
                } else {
                    MyLazyVerticalGrid(
                        gamesData = state.games,
                        onNavigate = onNavigate
                    )
                }
            }
            is SearchState.Error -> MyShowError(state.error)
        }
    }
}