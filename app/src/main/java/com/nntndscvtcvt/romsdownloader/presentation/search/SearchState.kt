package com.nntndscvtcvt.romsdownloader.presentation.search

import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity

sealed interface SearchState {
    data object Idle: SearchState
    data class Success(val games: List<GameEntity>): SearchState
    data class Error(val error: Throwable): SearchState
}