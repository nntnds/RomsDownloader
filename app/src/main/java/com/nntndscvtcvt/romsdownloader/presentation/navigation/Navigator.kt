package com.nntndscvtcvt.romsdownloader.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.nntndscvtcvt.romsdownloader.presentation.download.DownloadScreen
import com.nntndscvtcvt.romsdownloader.presentation.favorite.FavoriteScreen
import com.nntndscvtcvt.romsdownloader.presentation.game_info.GameInfoScreen
import com.nntndscvtcvt.romsdownloader.presentation.home.HomeScreen
import com.nntndscvtcvt.romsdownloader.presentation.login.LoginScreen
import com.nntndscvtcvt.romsdownloader.presentation.search_result.SearchResultScreen
import com.nntndscvtcvt.romsdownloader.presentation.settings.SettingsScreen
import com.nntndscvtcvt.romsdownloader.presentation.utils.showBottomBar
import com.nntndscvtcvt.romsdownloader.presentation.utils.toIntRoute

@Composable
fun Navigator() {
    val backStack = rememberNavBackStack(AppRoutes.Home)
    val currentRoute = backStack.lastOrNull()
    val navigator = remember { NavigatorState(backStack) }

    Box(modifier = Modifier.fillMaxSize()) {
        NavDisplay(
            modifier = Modifier.fillMaxSize(),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<AppRoutes.Home> {
                    HomeScreen(
                        navigateToGameInfo = navigator::navigateToGameInfo,
                        navigateToSettings = navigator::navigateToSettings,
                        navigateToSearchResult = navigator::navigateToSearchResult
                    )
                }
                entry<AppRoutes.Downloads> {
                    DownloadScreen(
                        navigateToSettings = navigator::navigateToSettings,
                        navigateToGameInfo = navigator::navigateToGameInfo
                    )
                }
                entry<AppRoutes.Favorites> {
                    FavoriteScreen(
                        navigateToGameInfo = navigator::navigateToGameInfo,
                        navigateToSettings = navigator::navigateToSettings
                    )
                }
                entry<AppRoutes.GameInfo> { key ->
                    GameInfoScreen(id = key.id, onBack = navigator::navigateBack)
                }
                entry<AppRoutes.Login> {
                    LoginScreen(onBack = navigator::navigateBack)
                }
                entry<AppRoutes.Settings> {
                    SettingsScreen(
                        navigateToLogin = navigator::navigateToLogin,
                        onBack = navigator::navigateBack
                    )
                }
                entry<AppRoutes.SearchResult> { key ->
                    SearchResultScreen(
                        platform = key.platform,
                        query = key.query,
                        onBack = navigator::navigateBack,
                        navigateToGameInfo = navigator::navigateToGameInfo
                    )
                }
            },
            transitionSpec = {
                // 1. Проверяем оба состояния
                val from = initialState.entries.lastOrNull()?.contentKey.toString().toIntRoute()
                val to = targetState.entries.lastOrNull()?.contentKey.toString().toIntRoute()

                if (from != null && to != null) {
                    if (to > from) {
                        slideInHorizontally(initialOffsetX = { it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { -it })
                    } else {
                        slideInHorizontally(initialOffsetX = { -it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { it })
                    }
                } else {
                    slideInHorizontally(initialOffsetX = { it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { -it })
                }
            },
            popTransitionSpec = {
                val from = initialState.entries.lastOrNull()?.contentKey.toString().toIntRoute()
                val to = targetState.entries.lastOrNull()?.contentKey.toString().toIntRoute()

                if (from != null && to != null) {
                    if (from > to) {
                        slideInHorizontally(initialOffsetX = { -it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { it })
                    } else {
                        slideInHorizontally(initialOffsetX = { it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { -it })
                    }
                } else {
                    slideInHorizontally(initialOffsetX = { -it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { it })
                }
            },
            predictivePopTransitionSpec = {
                val from = initialState.entries.lastOrNull()?.contentKey.toString().toIntRoute()
                val to = targetState.entries.lastOrNull()?.contentKey.toString().toIntRoute()

                if (from != null && to != null) {
                    if (from > to) {
                        slideInHorizontally(initialOffsetX = { -it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { it })
                    } else {
                        slideInHorizontally(initialOffsetX = { it }) togetherWith
                                slideOutHorizontally(targetOffsetX = { -it })
                    }
                } else {
                    slideInHorizontally(initialOffsetX = { -it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { it })
                }
            }
        )

        if (currentRoute.showBottomBar()) {
            BottomFloatingToolbar(
                currentRoute = currentRoute,
                onNavigate = navigator::navigate,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}