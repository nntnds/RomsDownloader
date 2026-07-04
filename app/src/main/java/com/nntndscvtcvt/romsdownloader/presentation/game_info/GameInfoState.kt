package com.nntndscvtcvt.romsdownloader.presentation.game_info

import com.nntndscvtcvt.romsdownloader.domain.model.Game
import com.nntndscvtcvt.romsdownloader.domain.model.GameFileItem

sealed interface GameInfoState {
    data object Loading : GameInfoState
    data class Success(
        val games: Game,
        val gameFileItem: List<GameFileItem>,
        val isFavorite: Boolean
    ) : GameInfoState
    data class Error(val error: Throwable) : GameInfoState
}