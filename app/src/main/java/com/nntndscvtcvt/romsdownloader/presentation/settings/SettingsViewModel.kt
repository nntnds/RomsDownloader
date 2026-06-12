package com.nntndscvtcvt.romsdownloader.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.repository.CookieRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.GameRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val gameRepository: GameRepository,
    private val cookieRepository: CookieRepository
) : ViewModel() {

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent = _snackbarEvent.asSharedFlow()

    fun downloadConsoleGames(consoleName: String) = viewModelScope.launch {
        gameRepository.downloadConsoleGames(consoleName).fold(
            onSuccess = { _snackbarEvent.emit("$consoleName games downloaded ") },
            onFailure = { _snackbarEvent.emit("Failed to download $consoleName games ") }
        )
    }

    fun cookieCheck() = viewModelScope.launch {
        val sig = cookieRepository.loggedInSig.firstOrNull()
        val user = cookieRepository.loggedInUser.firstOrNull()

        if (sig == null || user == null) {
            return@launch
        }
    }
}