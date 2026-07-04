package com.nntndscvtcvt.romsdownloader.presentation.search_result

import com.nntndscvtcvt.romsdownloader.domain.model.Game

sealed interface SearchResultState {
    data object Idle: SearchResultState
    data object Loading: SearchResultState
    data class Success(val games: List<Game>): SearchResultState
}