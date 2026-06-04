package com.nntndscvtcvt.romsdownloader.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.repository.GameFavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteRepository: GameFavoriteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<FavoriteState>(FavoriteState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() = viewModelScope.launch {
        favoriteRepository.getAllFavorites().collect { result ->
            result.onSuccess { games ->
                _uiState.value = if (games.isEmpty()) {
                    FavoriteState.Empty
                } else {
                    FavoriteState.Success(games)
                }
            }
            result.onFailure { _uiState.value = FavoriteState.Error(it) }
        }
    }
}