package com.nntndscvtcvt.romsdownloader.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.nntndscvtcvt.romsdownloader.data.utils.Constants.COVER_URL
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens

@Composable
fun GamesGrid(
    modifier: Modifier,
    gamesData: List<Game>,
    navigateToGameInfo: (Int) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.PaddingLarge),
        columns = GridCells.Fixed(Dimens.GridColumns),
        horizontalArrangement = Arrangement.spacedBy(Dimens.GridSpacing),
        verticalArrangement = Arrangement.spacedBy(Dimens.GridSpacing),
        contentPadding = PaddingValues(bottom = Dimens.PaddingLarge)
    ) {
        items(
            items = gamesData,
            key = { it.databaseID }
        ) { game ->
            GameCard(
                game = game,
                navigateToGameInfo = navigateToGameInfo,
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
private fun GameCard(
    game: Game,
    navigateToGameInfo: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { navigateToGameInfo(game.databaseID) }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(COVER_URL + game.coverUrl)
                .size(300, 430)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(Dimens.ImageAspectRatio)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop,
            placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
            error = ColorPainter(MaterialTheme.colorScheme.errorContainer),
        )

//        Spacer(Modifier.size(Dimens.PaddingSmall))

        Text(
            text = game.name,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(Dimens.PaddingSmall)
        )
    }
}