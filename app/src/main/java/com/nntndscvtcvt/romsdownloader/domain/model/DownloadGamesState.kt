package com.nntndscvtcvt.romsdownloader.domain.model

sealed class DownloadGamesState {
    data object Idle: DownloadGamesState()
    data object Started : DownloadGamesState()
    data object Success : DownloadGamesState()
    data object Failed : DownloadGamesState()

    val isDownloading: Boolean
        get() = this is Started
}
