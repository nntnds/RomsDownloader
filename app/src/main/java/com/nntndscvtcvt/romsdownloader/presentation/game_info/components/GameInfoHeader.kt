package com.nntndscvtcvt.romsdownloader.presentation.game_info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.nntndscvtcvt.romsdownloader.data.utils.Constants.COVER_URL
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens

@Composable
fun GameInfoHeader(state: Game) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingLarge),
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .data(COVER_URL + state.coverUrl)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build(),
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .aspectRatio(Dimens.ImageAspectRatio)
                .clip(MaterialTheme.shapes.medium),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
            error = ColorPainter(MaterialTheme.colorScheme.errorContainer),
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimens.ColumnVerticalArrangement)
        ) {
            Text(
                text = state.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = state.developer,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = state.platform,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall),
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)
            ) {
                state.genres.forEach {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainerLow,
                                shape = RoundedCornerShape(32.dp)
                            )
                            .padding(horizontal = Dimens.PaddingMedium, vertical = Dimens.PaddingSmall)
                    )
                }
            }
        }
    }
}