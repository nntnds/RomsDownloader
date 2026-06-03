package com.nntndscvtcvt.romsdownloader.presentation.game_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadEntity
import com.nntndscvtcvt.romsdownloader.domain.model.Downloads
import com.nntndscvtcvt.romsdownloader.domain.model.FavoriteEntity
import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity
import com.nntndscvtcvt.romsdownloader.domain.repository.CookieRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.DownloadRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.GameFavoriteRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.GameInfoRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GameInfoViewModel(
    private val gameInfoRepository: GameInfoRepository,
    private val favoriteRepository: GameFavoriteRepository,
    private val cookieRepository: CookieRepository,
    private val downloadRepository: DownloadRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameInfoState>(GameInfoState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent = _snackbarEvent.asSharedFlow()

    fun getInfo(id: String) {
        observeFavorite(id)
        loadGame(id)
    }

    private fun observeFavorite(id: String) = viewModelScope.launch {
        favoriteRepository.isFavoriteExist(id).collect { _isFavorite.value = it }
    }

    fun startDownload(url: String, fileName: String, gameEntity: GameEntity) = viewModelScope.launch {
        val sig = cookieRepository.loggedInSig.first() ?: run {
            _snackbarEvent.emit("Log in to your account")
            return@launch
        }
        val user = cookieRepository.loggedInUser.first() ?: run {
            _snackbarEvent.emit("Log in to your account")
            return@launch
        }

        val hasAccess = downloadRepository.checkAccess(sig, user)
        if (!hasAccess) {
            _snackbarEvent.emit("Log in to your account")
            return@launch
        }

        val downloadId = downloadRepository.downloadFile(url, sig, user, fileName)
        downloadRepository.saveDownload(
            DownloadEntity(
                downloadId = downloadId,
                gameId = gameEntity.id,
                gameName = gameEntity.name,
                coverUrl = gameEntity.coverUrl,
                fileName = fileName,
                url = url
            )
        )
    }

    private fun loadGame(id: String) = viewModelScope.launch {
        _uiState.value = GameInfoState.Idle
        gameInfoRepository.getGameById(id)
            .catch { _uiState.value = GameInfoState.Error(it) }
            .collect { result ->
                result.onSuccess { game ->
                    _uiState.value = GameInfoState.Success(
                        games = game,
                        gameFileItem = mapDownloadItem(game.downloads)
                    )
                }
                result.onFailure {
                    _uiState.value = GameInfoState.Error(it)
                }
            }
    }

    fun toggleFavorite(id: String) = viewModelScope.launch {
        if (_isFavorite.value) {
            favoriteRepository.removeFromFavorite(FavoriteEntity(id))
            _snackbarEvent.emit("Removed from favorites")
        } else {
            favoriteRepository.addToFavorite(FavoriteEntity(id))
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

    private fun String.cut(startChars: Int = 15, endChars: Int = 15): String {
        if (length <= startChars + endChars) return this
        return "${take(startChars)}...${takeLast(endChars)}"
    }
}