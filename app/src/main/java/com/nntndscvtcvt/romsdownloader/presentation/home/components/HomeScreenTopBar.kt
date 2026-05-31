package com.nntndscvtcvt.romsdownloader.presentation.home.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreenTopBar() {
    TopAppBar(
        windowInsets = WindowInsets(0.dp),
        title = { Text("Home") },
    )
}