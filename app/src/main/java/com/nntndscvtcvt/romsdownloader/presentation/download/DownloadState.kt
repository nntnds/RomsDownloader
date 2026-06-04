package com.nntndscvtcvt.romsdownloader.presentation.download

import com.nntndscvtcvt.romsdownloader.domain.model.DownloadItem

sealed interface DownloadState {
    data object Loading : DownloadState
    data object Empty : DownloadState
    data class Success(val downloads: List<DownloadItem>) : DownloadState
}
