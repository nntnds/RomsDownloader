package com.nntndscvtcvt.romsdownloader.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.nntndscvtcvt.romsdownloader.presentation.download.DownloadScreen
import com.nntndscvtcvt.romsdownloader.presentation.favorite.FavoriteScreen
import com.nntndscvtcvt.romsdownloader.presentation.game_info.GameInfoScreen
import com.nntndscvtcvt.romsdownloader.presentation.home.HomeScreen
import com.nntndscvtcvt.romsdownloader.presentation.login.LoginScreen

@Composable
fun Navigator() {
    val backStack = rememberNavBackStack(AppRoutes.Home)
    val currentRoute = backStack.lastOrNull()

    val shouldShowBottomBar = currentRoute is AppRoutes.Home ||
            currentRoute is AppRoutes.Downloads ||
            currentRoute is AppRoutes.Favorites

    val navigateToGameInfo = { id: String ->
        navigateToTab(backStack, AppRoutes.GameInfo(id))
    }
    val onBack = {
        if (backStack.size > 1) backStack.removeLastOrNull()
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { navigateToTab(backStack, it) }
                )
            }
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<AppRoutes.Home> {
                    HomeScreen(
                        onNavigate = navigateToGameInfo
                    )
                }
                entry<AppRoutes.Downloads> {
                    DownloadScreen(
                        onNavigate = { navigateToTab(backStack, AppRoutes.Login) },
                        onGameInfoScreen = navigateToGameInfo
                    )
                }
                entry<AppRoutes.Favorites> {
                    FavoriteScreen(onNavigate = navigateToGameInfo)
                }
                entry<AppRoutes.GameInfo> { key ->
                    GameInfoScreen(id = key.id, onBack = onBack)
                }
                entry<AppRoutes.Login> {
                    LoginScreen(onBack = onBack)
                }
            },
            transitionSpec = {
                fadeIn(tween(100)) togetherWith fadeOut(tween(100))
            },
            popTransitionSpec = {
                fadeIn(tween(100)) togetherWith fadeOut(tween(100))
            },
            predictivePopTransitionSpec = {
                slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
            }
        )
    }
}

@Composable
private fun BottomNavigationBar(
    currentRoute: NavKey?,
    onNavigate: (AppRoutes) -> Unit
) {
    NavigationBar {
        DESTINATIONS.forEach { (newRoute, item) ->
            val isSelected = currentRoute == newRoute

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(newRoute) },
                icon = {
                    Icon(
                        painter = painterResource(if (isSelected) item.selectedIcon else item.icon),
                        contentDescription = null
                    )
                },
                label = { Text(
                    text = (stringResource(item.title))
                ) },
            )
        }
    }
}

private fun navigateToTab(
    backStack: NavBackStack<NavKey>,
    newRoute: AppRoutes
) {
    if (backStack.lastOrNull() == newRoute) return
    backStack.remove(newRoute)
    backStack.add(newRoute)
}