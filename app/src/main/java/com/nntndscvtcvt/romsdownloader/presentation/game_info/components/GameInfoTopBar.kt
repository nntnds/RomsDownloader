package com.nntndscvtcvt.romsdownloader.presentation.game_info.components

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
fun GameInfoTopBar(
    isFavorite: Boolean,
    onBack: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val backButton = painterResource(R.drawable.outline_keyboard_arrow_left_24)
    val outlineFavoriteButton = painterResource(R.drawable.outline_favorite_24)
    val filledFavoriteButton = painterResource(R.drawable.baseline_favorite_24)

    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(backButton, null, modifier = Modifier.size(32.dp))
            }
        },
        actions = {
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    painter = if (isFavorite) filledFavoriteButton
                    else outlineFavoriteButton, contentDescription = null
                )
            }
        },
        windowInsets = WindowInsets(top = 0.dp)
    )
}