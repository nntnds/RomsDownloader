package com.nntndscvtcvt.romsdownloader.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.repository.GameFavoriteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoriteViewModel(
    private val favoriteRepository: GameFavoriteRepository
) : ViewModel() {

    val uiState: StateFlow<FavoriteState> = favoriteRepository.getAllFavorites()
        .map { games ->
            if (games.isEmpty()) FavoriteState.Empty
            else FavoriteState.Success(games)
        }
        .catch { FavoriteState.Error(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoriteState.Loading
        )
}