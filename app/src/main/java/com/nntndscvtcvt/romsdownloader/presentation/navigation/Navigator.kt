package com.nntndscvtcvt.romsdownloader.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.nntndscvtcvt.romsdownloader.presentation.search_result.SearchResultScreen
import com.nntndscvtcvt.romsdownloader.presentation.download.DownloadScreen
import com.nntndscvtcvt.romsdownloader.presentation.favorite.FavoriteScreen
import com.nntndscvtcvt.romsdownloader.presentation.game_info.GameInfoScreen
import com.nntndscvtcvt.romsdownloader.presentation.home.HomeScreen
import com.nntndscvtcvt.romsdownloader.presentation.login.LoginScreen
import com.nntndscvtcvt.romsdownloader.presentation.settings.SettingsScreen

@Composable
fun Navigator() {
    val backStack = rememberNavBackStack(AppRoutes.Home)
    val currentRoute = backStack.lastOrNull()

    val navigator = remember { NavigatorState(backStack) }

    Scaffold(
        bottomBar = {
            if (currentRoute.showBottomBar()) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = navigator::navigate
                )
            }
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
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
                    GameInfoScreen(
                        id = key.id,
                        onBack = navigator::navigateBack
                    )
                }
                entry<AppRoutes.Login> {
                    LoginScreen(
                        onBack = navigator::navigateBack
                    )
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
                fadeIn(tween(250)) togetherWith fadeOut(tween(250))
            },
            popTransitionSpec = {
                fadeIn(tween(250)) togetherWith fadeOut(tween(250))
            },
            predictivePopTransitionSpec = {
                slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
            }
        )
    }
}