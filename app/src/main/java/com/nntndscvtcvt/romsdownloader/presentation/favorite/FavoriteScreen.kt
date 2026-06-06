package com.nntndscvtcvt.romsdownloader.presentation.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.presentation.components.MyLazyVerticalGrid
import com.nntndscvtcvt.romsdownloader.presentation.components.MyLoadingIndicator
import com.nntndscvtcvt.romsdownloader.presentation.components.MyShowError
import com.nntndscvtcvt.romsdownloader.presentation.favorite.components.FavoriteScreenTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteScreen(
    favoriteViewModel: FavoriteViewModel = koinViewModel(),
    onNavigate: (String) -> Unit
) {
    val state by favoriteViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            FavoriteScreenTopBar()
        }
    ) { innerPadding ->
        when(val state = state) {
            is FavoriteState.Empty -> {
                Box(Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.no_favorites),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            is FavoriteState.Loading -> {
                MyLoadingIndicator(Modifier.padding(innerPadding))
            }
            is FavoriteState.Success -> {
                MyLazyVerticalGrid(
                    modifier = Modifier.padding(innerPadding),
                    gamesData = state.favorites,
                    onNavigate = onNavigate
                )
            }
            is FavoriteState.Error -> {
                MyShowError(Modifier.padding(innerPadding),state.error)
            }
        }
    }
}