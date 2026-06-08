package com.nntndscvtcvt.romsdownloader.presentation.favorite

import com.nntndscvtcvt.romsdownloader.domain.model.Game

sealed interface FavoriteState {
    data object Empty : FavoriteState
    data object Loading: FavoriteState
    data class Success(val favorites: List<Game>) : FavoriteState
    data class Error(val error: Throwable) : FavoriteState
}
