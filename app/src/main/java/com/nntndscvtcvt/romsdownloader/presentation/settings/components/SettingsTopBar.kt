package com.nntndscvtcvt.romsdownloader.presentation.settings.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsTopBar(
    onBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val backButton = painterResource(R.drawable.outline_keyboard_arrow_left_24)

    TopAppBar(
        windowInsets = WindowInsets.statusBars,
        scrollBehavior = scrollBehavior,
        title = {
            Text(stringResource(R.string.settings_topbar))
        },
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    painter = backButton,
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.iconMediumSize)
                )
            }
        }
    )
}