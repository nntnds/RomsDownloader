package com.nntndscvtcvt.romsdownloader.presentation.login.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nntndscvtcvt.romsdownloader.R

@Composable
fun LoginScreenTopBar(onBack: () -> Unit, onRefreshClick: () -> Unit, onClear: () -> Unit) {
    val backButton = painterResource(R.drawable.outline_keyboard_arrow_left_24)
    val refreshButton = painterResource(R.drawable.outline_refresh_24)
    val cookieButton = painterResource(R.drawable.outline_cookie_off_24)

    TopAppBar(
        windowInsets = WindowInsets(0.dp),
        title = {},
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(backButton, null, modifier = Modifier.size(32.dp))
            }
        },
        actions = {
            IconButton(onClear) {
                Icon(cookieButton, null)
            }
            IconButton(onRefreshClick) {
                Icon(refreshButton, null)
            }
        }
    )
}