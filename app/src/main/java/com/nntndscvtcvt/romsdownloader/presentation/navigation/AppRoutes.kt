package com.nntndscvtcvt.romsdownloader.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoutes : NavKey {
    @Serializable
    data object Home : AppRoutes()

    @Serializable
    data object Favorites : AppRoutes()

    @Serializable
    data object Login : AppRoutes()

    @Serializable
    data object Settings : AppRoutes()

    @Serializable
    data class GameInfo(val id: Int) : AppRoutes()

    @Serializable
    data class SearchResult(val platform: String, val query: String) : AppRoutes()
}