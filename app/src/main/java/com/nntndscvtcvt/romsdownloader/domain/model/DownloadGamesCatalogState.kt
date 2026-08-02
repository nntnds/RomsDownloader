package com.nntndscvtcvt.romsdownloader.domain.model

sealed interface DownloadGamesCatalogState {
    data object Idle: DownloadGamesCatalogState
    data object Started : DownloadGamesCatalogState
    data object Success : DownloadGamesCatalogState
    data object Failed : DownloadGamesCatalogState

    val isDownloading: Boolean
        get() = this is Started
}