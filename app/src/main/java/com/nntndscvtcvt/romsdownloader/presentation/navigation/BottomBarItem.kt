package com.nntndscvtcvt.romsdownloader.presentation.navigation

import androidx.annotation.StringRes
import com.nntndscvtcvt.romsdownloader.R

data class BottomBarItem(
    val icon: Int,
    val selectedIcon: Int,
    @StringRes val title: Int
)

val DESTINATIONS = mapOf(
    AppRoutes.Home to BottomBarItem(
        icon = R.drawable.outline_home_24,
        selectedIcon = R.drawable.baseline_home_filled_24,
        title = R.string.nav_home
    ),
    AppRoutes.Downloads to BottomBarItem(
        icon = R.drawable.outline_download_24,
        selectedIcon = R.drawable.outline_download_24,
        title = R.string.nav_downloads
    ),
    AppRoutes.Favorites to BottomBarItem(
        icon = R.drawable.outline_favorite_24,
        selectedIcon = R.drawable.baseline_favorite_24,
        title = R.string.nav_favorites
    )
)
