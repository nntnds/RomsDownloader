package com.nntndscvtcvt.romsdownloader.presentation.navigation

import androidx.compose.foundation.layout.Column
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
import com.nntndscvtcvt.romsdownloader.presentation.search.SearchScreen

@Composable
fun Navigator() {
    val backStack = rememberNavBackStack(AppRoutes.Home)
    val currentRoute = backStack.lastOrNull()
    val shouldShowBottomBar = currentRoute !is AppRoutes.GameInfo && currentRoute !is AppRoutes.Login

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavDisplay(
                backStack = backStack,
                onBack = {
                    backStack.removeLastOrNull()
                },
                entryProvider = entryProvider {
                    entry<AppRoutes.Home> {
                        HomeScreen(
                            onNavigate = {
                                navigateToTab(backStack, AppRoutes.GameInfo(it))
                            }
                        )
                    }
                    entry<AppRoutes.Search> {
                        SearchScreen(
                            onNavigate = {
                                navigateToTab(backStack, AppRoutes.GameInfo(it))
                            }
                        )
                    }
                    entry<AppRoutes.Downloads> {
                        DownloadScreen(
                            onNavigate = {
                                navigateToTab(backStack, AppRoutes.Login)
                            },
                            onGameInfoScreen = { navigateToTab(backStack, AppRoutes.GameInfo(it)) }
                        )
                    }
                    entry<AppRoutes.Favorites> {
                        FavoriteScreen(
                            onNavigate = { navigateToTab(backStack, AppRoutes.GameInfo(it)) }
                        )
                    }
                    entry<AppRoutes.GameInfo> { key ->
                        GameInfoScreen(
                            id = key.id,
                            onBack = {
                                if (backStack.size > 1) backStack.removeLastOrNull()
                            }
                        )
                    }
                    entry<AppRoutes.Login> {
                        LoginScreen(
                            onBack = {
                                if (backStack.size > 1) backStack.removeLastOrNull()
                            }
                        )
                    }
                }
            )
        }
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
                label = { Text(item.title) },
            )
        }
    }
}

private fun navigateToTab(
    backStack: NavBackStack<NavKey>,
    newRoute: AppRoutes
) {
    when (newRoute) {
        is AppRoutes.Search -> {
            backStack.removeIf { it is AppRoutes.Search }
            backStack.add(newRoute)
        }

        else -> {
            if (backStack.lastOrNull() == newRoute) return
            backStack.remove(newRoute)
            backStack.add(newRoute)
        }
    }
}