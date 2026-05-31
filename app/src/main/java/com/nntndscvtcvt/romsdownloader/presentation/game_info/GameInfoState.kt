package com.nntndscvtcvt.romsdownloader.presentation.game_info

import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity

sealed interface GameInfoState {
    data object Idle : GameInfoState
    data class Success(val games: GameEntity, val gameFileItem: List<GameFileItem>) : GameInfoState
    data class Error(val error: Throwable) : GameInfoState
}

data class GameFileItem(
    val title: String,
    val type: String,
    val url: String,
    val shortenUrl: String
)