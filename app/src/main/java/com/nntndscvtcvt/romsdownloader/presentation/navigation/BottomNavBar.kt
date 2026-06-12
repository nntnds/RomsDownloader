package com.nntndscvtcvt.romsdownloader.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey

@Composable
fun BottomNavigationBar(
    currentRoute: NavKey?,
    onNavigate: (AppRoutes) -> Unit
) {
    NavigationBar {
        DESTINATIONS.forEach { (route, destination) ->
            val isSelected = currentRoute == route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(route) },
                icon = {
                    Icon(
                        painter = painterResource(if (isSelected) destination.selectedIcon else destination.icon),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = (stringResource(destination.title))
                    )
                },
            )
        }
    }
}