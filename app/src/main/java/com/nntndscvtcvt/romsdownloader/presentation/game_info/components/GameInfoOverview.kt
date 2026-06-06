package com.nntndscvtcvt.romsdownloader.presentation.game_info.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity

@Composable
fun GameInfoOverview(state: GameEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, top = 12.dp, end = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var isExpanded by rememberSaveable { mutableStateOf(false) }
        val cleanOverview = state.overview.replace(Regex("\\s+"), " ")

        Text(
            text = cleanOverview,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = if (!isExpanded) 6 else Int.MAX_VALUE,
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        )
        TextButton(
            onClick = { isExpanded = !isExpanded },
        ) {
            Text(
                text = stringResource(
                    id = if (!isExpanded) R.string.read_more else R.string.hide
                )
            )
        }
    }
}