package com.nntndscvtcvt.romsdownloader.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadGamesCatalogState
import com.nntndscvtcvt.romsdownloader.domain.repository.GameRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.SettingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val gameRepository: GameRepository,
    private val settingsRepository: SettingsRepository
//    private val cookieRepository: CookieRepository,
) : ViewModel() {

    private val _connectionStatus = MutableStateFlow<SettingsConnectionStatus>(SettingsConnectionStatus.Idle)
    val connectionStatus = _connectionStatus.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val snackbarEvent = _snackbarEvent.asSharedFlow()

    private val _progress = MutableStateFlow<Map<String, DownloadGamesCatalogState>>(emptyMap())
    val progress: StateFlow<Map<String, DownloadGamesCatalogState>> = _progress.asStateFlow()

    private val _gamesCount = MutableStateFlow<Map<String, Int>>(emptyMap())
    val gamesCount = _gamesCount.asStateFlow()

    val useExternalDownloader = settingsRepository.getUseExternalDownloader()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val downloadJob = mutableMapOf<String, Job>()

    init {
        consoles.forEach { console ->
            viewModelScope.launch {
                gameRepository.getGamesCount(platform = console.platform).collect { count ->
                    _gamesCount.update { it + (console.platform to count) }
                }
            }
        }
    }

    fun toggleExternalDownloader(value: Boolean) = viewModelScope.launch {
        settingsRepository.setUseExternalDownloader(value)
    }

    fun downloadConsoleGames(consoleName: String) {
        if (downloadJob[consoleName]?.isActive == true) return

        downloadJob[consoleName] = viewModelScope.launch {
            gameRepository.downloadConsoleGames(consoleName).collect { state ->
                when (state) {
                    is DownloadGamesCatalogState.Idle -> {}

                    is DownloadGamesCatalogState.Started -> {
                        _progress.update { it + (consoleName to DownloadGamesCatalogState.Started) }
                    }

                    is DownloadGamesCatalogState.Success -> {
                        _progress.update { it - consoleName }
                        downloadJob.remove(consoleName)
                        _snackbarEvent.emit("$consoleName successfully downloaded!")
                    }

                    is DownloadGamesCatalogState.Failed -> {
                        _progress.update { it - consoleName }
                        downloadJob.remove(consoleName)
                        _snackbarEvent.emit("Failed to download $consoleName. Check internet connection.")
                    }
                }
            }
        }
    }

//    fun checkConnection() {
//        if (_Settings_connectionStatus.value == SettingsConnectionStatus.Checking) return
//
//        viewModelScope.launch {
//            val sig = cookieRepository.loggedInSig.firstOrNull()
//            val user = cookieRepository.loggedInUser.firstOrNull()
//
//            if (sig == null || user == null) {
//                _Settings_connectionStatus.value = SettingsConnectionStatus.NotLoggedIn
//                _snackbarEvent.emit("Please log in to Archive.org first.")
//                return@launch
//            }
//
//            _Settings_connectionStatus.value = SettingsConnectionStatus.Checking
//            val result = downloadFileRepository.checkAccess(sig, user)
//
//            _Settings_connectionStatus.value = if (result.isSuccess) SettingsConnectionStatus.Success
//            else SettingsConnectionStatus.Error
//        }
//    }

    fun deletePlatformGames(platform: String) = viewModelScope.launch {
        gameRepository.deletePlatformGames(platform).fold(
            onSuccess = { _snackbarEvent.emit("All games for this platform deleted") },
            onFailure = { _snackbarEvent.emit("Failed to delete games") }
        )
    }

    override fun onCleared() {
        super.onCleared()
        downloadJob.values.forEach { it.cancel() }
        downloadJob.clear()
    }
}