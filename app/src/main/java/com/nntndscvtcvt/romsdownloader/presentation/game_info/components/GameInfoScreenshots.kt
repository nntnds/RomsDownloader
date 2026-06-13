package com.nntndscvtcvt.romsdownloader.presentation.game_info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.data.utils.Constants.COVER_URL
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens

@Composable
fun GameInfoScreenshots(state: Game) {
    var isFullScreen by rememberSaveable { mutableStateOf(false) }
    var imageUrl by rememberSaveable { mutableStateOf("") }
    val carouselState = rememberCarouselState { state.screenshots.size }

    if (state.screenshots.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ScreenshotImageHeight)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = MaterialTheme.shapes.medium
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_screenshots),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        HorizontalMultiBrowseCarousel(
            state = carouselState,
            preferredItemWidth = 360.dp,
            itemSpacing = Dimens.PaddingMedium,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = Dimens.PaddingLarge)
        ) { index ->
            val url = state.screenshots[index]

            AsyncImage(
                modifier = Modifier
                    .height(Dimens.ScreenshotImageHeight)
                    .maskClip(MaterialTheme.shapes.medium)
                    .clickable {
                        isFullScreen = !isFullScreen
                        imageUrl = url
                    },
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .crossfade(true)
                    .data(COVER_URL + url)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                error = ColorPainter(MaterialTheme.colorScheme.errorContainer),
            )
        }
        if (isFullScreen) {
            Dialog(
                onDismissRequest = { isFullScreen = false }, properties = DialogProperties(
                    usePlatformDefaultWidth = false
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            isFullScreen = !isFullScreen
                        }
                        .padding(Dimens.PaddingLarge),
                    contentAlignment = Alignment.Center) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.small)
                            .clickable { isFullScreen = !isFullScreen },
                        model = ImageRequest.Builder(LocalContext.current).crossfade(true)
                            .data(COVER_URL + imageUrl).memoryCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                        error = ColorPainter(MaterialTheme.colorScheme.errorContainer),
                    )
                }
            }
        }
    }
}