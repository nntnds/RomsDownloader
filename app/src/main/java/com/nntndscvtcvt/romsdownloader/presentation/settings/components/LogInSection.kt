package com.nntndscvtcvt.romsdownloader.presentation.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import com.nntndscvtcvt.romsdownloader.presentation.settings.ConnectionStatus
import com.nntndscvtcvt.romsdownloader.presentation.settings.components.login.ConnectionItem
import com.nntndscvtcvt.romsdownloader.presentation.settings.components.login.LoginItem

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LogInSection(
    navigateToLogin: () -> Unit,
    onCheckConnection: () -> Unit,
    connectionStatus: ConnectionStatus
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
    ) {
        LoginItem(navigateToLogin)
        ConnectionItem(onCheckConnection, connectionStatus)
    }
}