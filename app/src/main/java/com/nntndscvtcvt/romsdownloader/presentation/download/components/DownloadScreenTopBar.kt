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
fun DownloadScreenTopBar(onNavigate: () -> Unit, checkCookie: () -> Unit) {
    val loginButton = painterResource(R.drawable.outline_login_24)
    val cookieButton = painterResource(R.drawable.outline_cookie_24)
    TopAppBar(
        windowInsets = WindowInsets(0.dp),
        title = { Text(stringResource(R.string.download_topbar)) },
        actions = {
            IconButton(checkCookie) {
                Icon(cookieButton, null)
            }
            IconButton(onNavigate) {
                Icon(loginButton, null)
            }
        }
    )
}