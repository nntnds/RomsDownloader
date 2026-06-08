package com.nntndscvtcvt.romsdownloader.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.repository.GameRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.SearchGameRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
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
        initializeData()
    }

    private fun initializeData() = viewModelScope.launch {
        try {
            gameRepository.sync()
            observeGames()
        } catch (e: Exception) {
            _uiState.value = HomeState.Error(e)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private suspend fun observeGames() {
        _query
            .debounce(300.milliseconds)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    gameRepository.getAllGames()
                } else {
                    searchGameRepository.searchGame(query.trim())
                }
            }
            .collect { result ->
                _uiState.value = result.fold(
                    onSuccess = { HomeState.Success(it) },
                    onFailure = { HomeState.Error(it) }
                )
            }
    }

    fun searchGame(query: String) = _query.update { query }

    fun clearSearch() = _query.update { "" }

    fun toggleIsActive(value: Boolean) {
        _isSearchActive.update { value }
        if (!value) _query.update { "" }
    }
}