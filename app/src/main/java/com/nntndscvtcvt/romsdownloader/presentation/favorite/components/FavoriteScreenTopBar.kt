package com.nntndscvtcvt.romsdownloader.presentation.favorite.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun FavoriteScreenTopBar() {
    TopAppBar(
        windowInsets = WindowInsets(0.dp),
        title = { Text("Favorites") },
    )
}