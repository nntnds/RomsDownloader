package com.nntndscvtcvt.romsdownloader.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.presentation.settings.components.ConsoleItem
import com.nntndscvtcvt.romsdownloader.presentation.settings.components.LoginSection
import com.nntndscvtcvt.romsdownloader.presentation.settings.components.SectionHeader
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    navigateToLogin: () -> Unit,
    onBack: () -> Unit
) {
    val installIcon = painterResource(R.drawable.outline_download_24)
    val backButton = painterResource(R.drawable.outline_keyboard_arrow_left_24)

    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text(stringResource(R.string.settings_topbar)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            painter = backButton,
                            contentDescription = null,
                            modifier = Modifier.size(Dimens.iconMediumHeight)
                        )
                    }
                },
                subtitle = { Text(stringResource(R.string.subtitle_topbar)) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding + PaddingValues(horizontal = Dimens.PaddingLarge)),
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
        ) {
            stickyHeader {
                SectionHeader(stringResource(R.string.log_in_header))
            }
            item {
                LoginSection(navigateToLogin)
            }

            item {
                Spacer(Modifier.height(Dimens.PaddingLarge))
            }

            stickyHeader {
                SectionHeader(stringResource(R.string.consoles_header))
            }
            itemsIndexed(
                items = consoles,
                key = { _, item -> item.consoleName }
            ) { index, item ->
                ConsoleItem(
                    item = item,
                    index = index,
                    totalCount = consoles.size,
                    installIcon = installIcon
                )
            }

            item {
                Spacer(Modifier.height(Dimens.PaddingLarge))
            }
        }
    }
}