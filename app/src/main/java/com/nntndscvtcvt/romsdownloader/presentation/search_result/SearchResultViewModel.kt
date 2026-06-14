package com.nntndscvtcvt.romsdownloader.presentation.search_result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.repository.SearchGameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchResultViewModel(
    private val searchGameRepository: SearchGameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchResultState>(SearchResultState.Idle)
    val uiState = _uiState.asStateFlow()

    fun loadGames(platform: String, query: String) = viewModelScope.launch {
        _uiState.value = SearchResultState.Loading

        searchGameRepository.searchGame(query)
            .map { games ->
                games.filter { it.platform == platform }
            }
            .collect { _uiState.value = SearchResultState.Success(it) }
    }
}
