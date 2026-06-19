package com.nntndscvtcvt.romsdownloader.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.repository.GameRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.SearchGameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class HomeViewModel(
    private val gameRepository: GameRepository,
    private val searchGameRepository: SearchGameRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _uiState = MutableStateFlow<HomeState>(HomeState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive = _isSearchActive.asStateFlow()

    init {
        viewModelScope.launch { observeGames() }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private suspend fun observeGames() {
        _query
            .debounce(300.milliseconds)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                val result = if (query.isBlank()) {
                    gameRepository.getAllGames()
                } else {
                    searchGameRepository.searchGame(query.trim())
                }
                result
                    .map { games ->
                        HomeState.Success(games.groupBy { it.platform })
                    }
                    .catch { exception ->
                        _uiState.value = HomeState.Error(exception)
                    }
            }
            .flowOn(Dispatchers.Default)
            .collect { success ->
                _uiState.value = success
            }
    }

    fun searchGame(query: String) {
        _query.update { query }
        if (query.isNotBlank() && !_isSearchActive.value) {
            _isSearchActive.update { true }
        }
    }

    fun clearSearch() {
        _query.update { "" }
    }

    fun toggleIsActive(value: Boolean) {
        _isSearchActive.update { value }
        if (!value) {
            clearSearch()
        }
    }
}