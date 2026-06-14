package com.nntndscvtcvt.romsdownloader.presentation.download.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nntndscvtcvt.romsdownloader.R

@Composable
fun DownloadScreenTopBar(
    navigateToSettings: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val settingsButton = painterResource(R.drawable.outline_settings_24)

    TopAppBar(
        windowInsets = WindowInsets.statusBars,
        scrollBehavior = scrollBehavior,
        title = { Text(stringResource(R.string.download_topbar)) },
        actions = {
            IconButton(
                onClick = navigateToSettings
            ) {
                Icon(settingsButton, null)
            }
        }
    )
}