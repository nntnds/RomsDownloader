package com.nntndscvtcvt.romsdownloader.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nntndscvtcvt.romsdownloader.presentation.utils.toUserMessage

@Composable
fun ErrorScreen(modifier: Modifier, e: Throwable) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = e.toUserMessage())
    }
}