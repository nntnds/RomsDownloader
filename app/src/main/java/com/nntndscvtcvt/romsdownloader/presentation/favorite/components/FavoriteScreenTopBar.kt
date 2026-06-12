package com.nntndscvtcvt.romsdownloader.presentation.favorite.components

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
fun FavoriteScreenTopBar(
    navigateToSettings: () -> Unit
) {
    val settingsButton = painterResource(R.drawable.outline_settings_24)

    TopAppBar(
        windowInsets = WindowInsets(0.dp),
        title = {
            Text(
                text = stringResource(R.string.favorites_topbar)
            )
        },
        actions = {
            IconButton(
                onClick = navigateToSettings
            ) {
                Icon(settingsButton, null)
            }
        }
    )
}