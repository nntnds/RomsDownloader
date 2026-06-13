package com.nntndscvtcvt.romsdownloader.presentation.settings

sealed class ConnectionStatus {
    data object Idle : ConnectionStatus()
    data object NotLoggedIn : ConnectionStatus()
    data object Checking : ConnectionStatus()
    data object Success : ConnectionStatus()
    data object Error : ConnectionStatus()
}