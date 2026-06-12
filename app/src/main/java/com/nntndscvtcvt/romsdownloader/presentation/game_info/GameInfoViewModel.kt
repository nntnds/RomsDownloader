package com.nntndscvtcvt.romsdownloader.presentation.game_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadTask
import com.nntndscvtcvt.romsdownloader.domain.model.Downloads
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import com.nntndscvtcvt.romsdownloader.domain.repository.CookieRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.DownloadRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.GameFavoriteRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.GameInfoRepository
import com.nntndscvtcvt.romsdownloader.presentation.utils.cut
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class GameInfoViewModel(
    private val gameInfoRepository: GameInfoRepository,
    private val favoriteRepository: GameFavoriteRepository,
    private val cookieRepository: CookieRepository,
    private val downloadRepository: DownloadRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameInfoState>(GameInfoState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent = _snackbarEvent.asSharedFlow()

    fun getInfo(id: String) {
        _uiState.value = GameInfoState.Loading
        loadGameAndFavorite(id)
    }

    fun startDownload(url: String, fileName: String, game: Game) = viewModelScope.launch {
        val sig = cookieRepository.loggedInSig.firstOrNull()
        val user = cookieRepository.loggedInUser.firstOrNull()

        if (sig == null || user == null) {
            _snackbarEvent.emit("Log in to your account")
            return@launch
        }

        val downloadId = downloadRepository.downloadFile(url, sig, user, fileName)

        if (downloadId != -1L) {
            downloadRepository.saveDownload(
                DownloadTask(
                    downloadId = downloadId,
                    gameId = game.id,
                    gameName = game.name,
                    coverUrl = game.coverUrl,
                    fileName = fileName,
                    url = url
                )
            )
            _snackbarEvent.emit("Download started")
        } else {
            _snackbarEvent.emit("Failed to start download")
        }
    }

    private fun loadGameAndFavorite(id: String) = viewModelScope.launch {
        _uiState.value = GameInfoState.Loading

        gameInfoRepository.getGameById(id).fold(
            onFailure = { _uiState.value = GameInfoState.Error(it) },
            onSuccess = { game ->
                favoriteRepository.isFavoriteExist(game.id)
                    .catch { _uiState.value = GameInfoState.Error(it) }
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

    fun toggleFavorite() = viewModelScope.launch {
        val currentState = _uiState.value as? GameInfoState.Success ?: return@launch
        val gameId = currentState.games.id

        if (currentState.isFavorite) {
            favoriteRepository.removeFromFavorite(gameId)
            _snackbarEvent.emit("Removed from favorites")
        } else {
            favoriteRepository.addToFavorite(gameId)
            _snackbarEvent.emit("Added to favorites")
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


}