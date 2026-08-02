package com.nntndscvtcvt.romsdownloader.presentation.game_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.model.Downloads
import com.nntndscvtcvt.romsdownloader.domain.model.GameFileItem
import com.nntndscvtcvt.romsdownloader.domain.repository.CookieRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.GameFavoriteRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.GameRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.SettingsRepository
import com.nntndscvtcvt.romsdownloader.presentation.utils.cut
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameInfoViewModel(
    private val favoriteRepository: GameFavoriteRepository,
    private val cookieRepository: CookieRepository,
    private val settingsRepository: SettingsRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameInfoState>(GameInfoState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent = _snackbarEvent.asSharedFlow()

    val useExternalDownloader = settingsRepository.getUseExternalDownloader()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun getInfo(id: Int) {
        _uiState.value = GameInfoState.Loading
        loadGameAndFavorite(id)
    }

    private fun loadGameAndFavorite(id: Int) {
        _uiState.value = GameInfoState.Loading

        viewModelScope.launch {
            gameRepository.getGameById(id).fold(
                onFailure = {
                    _uiState.value = GameInfoState.Error(it)
                },
                onSuccess = { game ->
                    favoriteRepository.isFavoriteExist(game.databaseID)
                        .catch {
                            _uiState.value = GameInfoState.Error(it)
                        }
                        .collect { isFavorite ->
                            _uiState.value = GameInfoState.Success(
                                games = game,
                                gameFileItem = mapDownloadItem(game.downloads),
                                isFavorite = isFavorite
                            )
                        }
                }
            )
        }
    }

    fun toggleFavorite() {
        val currentState = _uiState.value as? GameInfoState.Success ?: return
        val gameId = currentState.games.databaseID

        viewModelScope.launch {
            if (currentState.isFavorite) {
                favoriteRepository.removeFromFavorite(gameId)
                _snackbarEvent.emit("Removed from favorites")
            } else {
                favoriteRepository.addToFavorite(gameId)
                _snackbarEvent.emit("Added to favorites")
            }
        }
    }

    private fun mapDownloadItem(downloads: List<Downloads>): List<GameFileItem> {
        return downloads.flatMap { download ->
            download.files.map { file ->
                GameFileItem(
                    title = download.title,
                    type = file.type,
                    url = file.url,
                    shortenUrl = file.url.cut()
                )
            }
        }
    }

    fun notifyNoExternalDownloader() = viewModelScope.launch {
        _snackbarEvent.emit("External downloader not found")
    }
}