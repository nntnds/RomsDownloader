package com.nntndscvtcvt.romsdownloader.presentation.home

import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity

sealed interface HomeState {
    data object Loading: HomeState
    data class Success(val games: List<GameEntity>): HomeState
    data class Error(val error: Throwable): HomeState
}