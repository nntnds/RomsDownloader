package com.nntndscvtcvt.romsdownloader.presentation.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.presentation.components.MyLazyVerticalGrid
import com.nntndscvtcvt.romsdownloader.presentation.components.MyShowError
import com.nntndscvtcvt.romsdownloader.presentation.favorite.components.FavoriteScreenTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteScreen(
    favoriteViewModel: FavoriteViewModel = koinViewModel(),
    onNavigate: (String) -> Unit
) {
    val state by favoriteViewModel.uiState.collectAsStateWithLifecycle()

    Column {
        FavoriteScreenTopBar()

        when(val state = state) {
            is FavoriteState.Empty -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No favorite games yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            is FavoriteState.Success -> {
                MyLazyVerticalGrid(gamesData = state.favorites, onNavigate)
            }
            is FavoriteState.Error -> {
                MyShowError(state.error)
            }
        }
    }
}