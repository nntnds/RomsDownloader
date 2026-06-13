package com.nntndscvtcvt.romsdownloader.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.domain.model.DownloadGamesState
import com.nntndscvtcvt.romsdownloader.presentation.settings.components.console.ConsoleItem
import com.nntndscvtcvt.romsdownloader.presentation.settings.components.LoginSection
import com.nntndscvtcvt.romsdownloader.presentation.components.SectionHeader
import com.nntndscvtcvt.romsdownloader.presentation.settings.components.SettingsTopBar
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    navigateToLogin: () -> Unit,
    onBack: () -> Unit
) {
    val progress by viewModel.progress.collectAsStateWithLifecycle()
    val connectionStatus by viewModel.connectionStatus.collectAsStateWithLifecycle()
    val gamesCount by viewModel.gamesCount.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collectLatest { message ->
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            SettingsTopBar(onBack)
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
                LoginSection(
                    navigateToLogin = navigateToLogin,
                    onCheckConnection = viewModel::checkConnection,
                    connectionStatus = connectionStatus
                )
            }

            item {
                Spacer(Modifier.size(Dimens.PaddingLarge))
            }

            stickyHeader {
                SectionHeader(stringResource(R.string.consoles_header))
            }
            itemsIndexed(
                items = consoles,
                key = { _, item -> item.consoleName }
            ) { index, item ->
                val consoleState = progress[item.consoleName] ?: DownloadGamesState.Idle

                ConsoleItem(
                    item = item,
                    index = index,
                    totalCount = consoles.size,
                    downloadConsoleGame = viewModel::downloadConsoleGames,
                    state = consoleState,
                    deletePlatformGames = viewModel::deletePlatformGames,
                    isDownloaded = (gamesCount[item.platform] ?: 0) > 0,
                    gamesCount = gamesCount[item.platform] ?: 0
                )
            }

            item {
                Spacer(Modifier.size(Dimens.PaddingLarge))
            }
        }
    }
}