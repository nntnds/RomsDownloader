package com.nntndscvtcvt.romsdownloader.presentation.download.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nntndscvtcvt.romsdownloader.R

@Composable
fun DownloadScreenTopBar(
    navigateToSettings: () -> Unit,
    checkCookie: () -> Unit
) {
    val cookieButton = painterResource(R.drawable.outline_cookie_24)
    val settingsButton = painterResource(R.drawable.outline_settings_24)

    TopAppBar(
        windowInsets = WindowInsets(0.dp),
        title = { Text(stringResource(R.string.download_topbar)) },
        actions = {
            IconButton(
                onClick = checkCookie
            ) {
                Icon(cookieButton, null)
            } /* TODO (Убрать кнопку) */
            IconButton(
                onClick = navigateToSettings
            ) {
                Icon(settingsButton, null)
            }
        }
    )
}