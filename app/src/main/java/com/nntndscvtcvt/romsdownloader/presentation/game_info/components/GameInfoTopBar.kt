package com.nntndscvtcvt.romsdownloader.presentation.game_info.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens

@Composable
fun GameInfoTopBar(
    isFavorite: Boolean,
    onBack: () -> Unit,
    onFavoriteClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val backButton = ImageVector.vectorResource(R.drawable.outline_keyboard_arrow_left_24)
    val outlineFavoriteButton = ImageVector.vectorResource(R.drawable.outline_favorite_24)
    val filledFavoriteButton = ImageVector.vectorResource(R.drawable.baseline_favorite_24)

    TopAppBar(
        windowInsets = WindowInsets.statusBars,
        scrollBehavior = scrollBehavior,
        title = {},
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = backButton,
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.iconMediumSize)
                )
            }
        },
        actions = {
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) filledFavoriteButton
                        else outlineFavoriteButton,
                    contentDescription = null
                )
            }
        }
    )
}