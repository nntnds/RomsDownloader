package com.nntndscvtcvt.romsdownloader.presentation.game_info.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity
import com.nntndscvtcvt.romsdownloader.presentation.game_info.GameFileItem

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GameInfoContent(
    modifier: Modifier,
    state: GameEntity,
    downloads: List<GameFileItem>,
    startDownload: (String, String) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
    ) {
        item {
            GameInfoHeader(state)
        }

        item {
            GameInfoOverview(state)
        }

        item {
            GameInfoScreenshots(state)
        }
        gameInfoDownloads(downloads, startDownload)
        item { Spacer(Modifier.height(12.dp)) }
    }

}