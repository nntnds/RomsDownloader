package com.nntndscvtcvt.romsdownloader.presentation.navigation

import com.nntndscvtcvt.romsdownloader.R

data class BottomBarItem(
    val icon: Int,
    val selectedIcon: Int,
    val title: String
)

val DESTINATIONS = mapOf(
    AppRoutes.Home to BottomBarItem(
        icon = R.drawable.outline_home_24,
        selectedIcon = R.drawable.baseline_home_filled_24,
        title = "Home"
    ),
    AppRoutes.Search to BottomBarItem(
        icon = R.drawable.outline_search_24,
        selectedIcon = R.drawable.outline_search_24,
        title = "Search"
    ),
    AppRoutes.Downloads to BottomBarItem(
        icon = R.drawable.outline_download_24,
        selectedIcon = R.drawable.outline_download_24,
        title = "Downloads"
    ),
    AppRoutes.Favorites to BottomBarItem(
        icon = R.drawable.outline_favorite_24,
        selectedIcon = R.drawable.baseline_favorite_24,
        title = "Favorites"
    ),
//    AppRoutes.Login to BottomBarItem(
//        icon = R.drawable.outline_login_24,
//        selectedIcon = R.drawable.outline_login_24,
//        title = "Login"
//    )
)
