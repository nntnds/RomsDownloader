package com.nntndscvtcvt.romsdownloader.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

class NavigatorState(
    private val backStack: NavBackStack<NavKey>
) {
    fun navigate(route: AppRoutes) {
        if (backStack.lastOrNull() == route) return
        backStack.remove(route)
        backStack.add(route)
    }

    fun navigateBack() {
        if (backStack.size > 1) backStack.removeLastOrNull()
    }

    fun navigateToGameInfo(id: Int) = navigate(AppRoutes.GameInfo(id))
    fun navigateToSettings() = navigate(AppRoutes.Settings)
    fun navigateToLogin() = navigate(AppRoutes.Login)
}