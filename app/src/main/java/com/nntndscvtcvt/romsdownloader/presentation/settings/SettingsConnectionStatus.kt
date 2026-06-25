package com.nntndscvtcvt.romsdownloader.presentation.settings

sealed interface SettingsConnectionStatus {
    data object Idle : SettingsConnectionStatus
    data object NotLoggedIn : SettingsConnectionStatus
    data object Checking : SettingsConnectionStatus
    data object Success : SettingsConnectionStatus
    data object Error : SettingsConnectionStatus
}