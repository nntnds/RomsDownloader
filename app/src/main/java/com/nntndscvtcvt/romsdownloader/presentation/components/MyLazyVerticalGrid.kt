package com.nntndscvtcvt.romsdownloader.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.nntndscvtcvt.romsdownloader.data.util.Constants.COVER_URL
import com.nntndscvtcvt.romsdownloader.domain.model.GameEntity

@Composable
fun MyLazyVerticalGrid(
    modifier: Modifier,
    gamesData: List<GameEntity>,
    onNavigate: (String) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 12.dp, end = 12.dp),
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = gamesData,
            key = { it.id },
            contentType = { it::class.java }
        ) { data ->
            GameCard(data, onNavigate)
        }
    }
}

@Composable
private fun GameCard(
    data: GameEntity,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        onClick = { onNavigate(data.id) }
    ) {
        Column {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f),
                model = ImageRequest.Builder(context)
                    .data(COVER_URL + data.coverUrl)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .size(300, 430)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                error = ColorPainter(MaterialTheme.colorScheme.errorContainer),
            )
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .heightIn(32.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = data.name,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}