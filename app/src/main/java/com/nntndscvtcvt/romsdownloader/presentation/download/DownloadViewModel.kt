package com.nntndscvtcvt.romsdownloader.presentation.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.repository.CookieRepository
import com.nntndscvtcvt.romsdownloader.domain.repository.DownloadFileRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DownloadViewModel(
    private val cookieRepository: CookieRepository,
    private val downloadFileRepository: DownloadFileRepository,
) : ViewModel() {

    private val _selectedIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedIds = _selectedIds.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent = _snackbarEvent.asSharedFlow()

    val uiState: StateFlow<DownloadState> = downloadFileRepository.getActiveDownloads()
        .map { items ->
            if (items.isEmpty()) DownloadState.Empty
            else DownloadState.Success(items)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DownloadState.Loading)

    fun retryDownload(downloadId: Long) = viewModelScope.launch {
        val sig = cookieRepository.loggedInSig.firstOrNull()
        val user = cookieRepository.loggedInUser.firstOrNull()

        if (sig == null || user == null) {
            _snackbarEvent.emit("Log in to your account")
            return@launch
        }

        downloadFileRepository.retryDownload(downloadId, sig, user).fold(
            onSuccess = { _snackbarEvent.emit("Download restarted") },
            onFailure = { _snackbarEvent.emit("Failed to retry download") }
        )
    }

    fun stopDownload(downloadId: Long) = viewModelScope.launch {
        downloadFileRepository.stopDownload(downloadId)
    }

    fun toggleSelection(downloadId: Long) {
        _selectedIds.update { currentSelected ->
            if (downloadId in currentSelected) currentSelected - downloadId
            else currentSelected + downloadId
        }
    }

    fun clearSelection() {
        _selectedIds.update { emptySet() }
    }

    fun selectAll() {
        val currentState = uiState.value as? DownloadState.Success ?: return
        _selectedIds.value = currentState.downloads.map { it.id }.toSet()
    }

    fun deleteSelected() = viewModelScope.launch {
        val idsToDelete = _selectedIds.value.toList()
        if (idsToDelete.isEmpty()) return@launch
        downloadFileRepository.deleteMultiple(idsToDelete)
        clearSelection()
    }
}