package com.nntndscvtcvt.romsdownloader.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.repository.SearchGameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: SearchGameRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow<SearchState>(SearchState.Idle)
    val uiState = _uiState.asStateFlow()

    fun loadData(query: String) = viewModelScope.launch {
        repository.searchGame(query).collect { result ->
            result.onSuccess { _uiState.value = SearchState.Success(it) }
            result.onFailure { _uiState.value = SearchState.Error(it) }
        }
    }
}