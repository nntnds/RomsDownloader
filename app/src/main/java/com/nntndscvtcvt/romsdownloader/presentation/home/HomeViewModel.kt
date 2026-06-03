package com.nntndscvtcvt.romsdownloader.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.repository.GameRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.SearchGameRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val gameRepository: GameRepository,
    private val searchGameRepository: SearchGameRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = _query
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                gameRepository.getAllGames()
            } else searchGameRepository.searchGame(query)
        }
        .map { result ->
            result.fold(
                onSuccess = { HomeState.Success(it) },
                onFailure = { HomeState.Error(it) }
            )
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, HomeState.Loading)


    init {
        viewModelScope.launch {
            try {
                gameRepository.sync()
            } catch (e: Exception) {  }
        }
    }

    fun loadData(query: String) = _query.update { query }

    fun clearSearch() = _query.update { "" }
}