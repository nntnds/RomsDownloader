package com.nntndscvtcvt.romsdownloader.presentation.search_result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchResultViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchResultState>(SearchResultState.Idle)
    val uiState = _uiState.asStateFlow()

    fun loadGames(platform: String, query: String) {
        _uiState.value = SearchResultState.Loading

        viewModelScope.launch {
            gameRepository.searchGamesByPlatform(platform, query)
                .collect { games ->
                    _uiState.value = SearchResultState.Success(games)
                }
        }
    }
}
