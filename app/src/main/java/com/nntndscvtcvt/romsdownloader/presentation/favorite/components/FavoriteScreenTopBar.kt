package com.nntndscvtcvt.romsdownloader.presentation.favorite.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.nntndscvtcvt.romsdownloader.R

@Composable
fun FavoriteScreenTopBar(
    navigateToSettings: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val settingsIcon = ImageVector.vectorResource(R.drawable.outline_settings_24)

    TopAppBar(
        scrollBehavior = scrollBehavior,
        windowInsets = WindowInsets.statusBars,
        title = {
            Text(
                text = stringResource(R.string.favorites_topbar)
            )
        },
        actions = {
            IconButton(onClick = navigateToSettings) {
                Icon(settingsIcon, null)
            }
        }
    )
}