package com.nntndscvtcvt.romsdownloader.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: GameRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeState>(HomeState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        initializeData()
    }

    private fun initializeData() = viewModelScope.launch {
        try {
            repository.sync()
            repository.getAllGames().collect { result ->
                result.onSuccess { _uiState.value = HomeState.Success(it) }
                result.onFailure { _uiState.value = HomeState.Error(it) }
            }
        } catch (e: Exception) {
            _uiState.value = HomeState.Error(e)
        }
    }
}