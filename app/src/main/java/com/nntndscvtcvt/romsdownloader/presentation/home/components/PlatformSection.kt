package com.nntndscvtcvt.romsdownloader.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.domain.model.Game
import com.nntndscvtcvt.romsdownloader.presentation.components.GameCard
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PlatformSection(
    platform: String,
    games: List<Game>,
    isSearch: Boolean,
    query: String,
    navigateToGameInfo: (Int) -> Unit,
    navigateToSearchResult: (String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(Dimens.PaddingSmall)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ListItem(
                headlineContent = {
                    Text(
                        text = platform,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                modifier = Modifier.heightIn(max = 56.dp),
                trailingContent = {
                    if (isSearch) {
                        TextButton(
                            onClick = {
                                navigateToSearchResult(platform, query)
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.see_all),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                },
            )
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = Dimens.PaddingLarge),
            horizontalArrangement = Arrangement.spacedBy(Dimens.GridSpacing)
        ) {
            items(
                items = games.take(20),
                key = { it.databaseID }
            ) { game ->
                GameCard(
                    game = game,
                    navigateToGameInfo = navigateToGameInfo,
                    modifier = Modifier.width(100.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PlatformSectionPrev() {
    MaterialTheme {
        PlatformSection(
            platform = "PSP",
            games = listOf(
                Game(
                    databaseID = 1,
                    name = "Name",
                    overview = "",
                    platform = "",
                    genres = emptyList(),
                    developer = "",
                    alternateNames = emptyList(),
                    coverUrl = "",
                    screenshots = emptyList(),
                    downloads = emptyList()
                )
            ),
            isSearch = true,
            query = "Game",
            navigateToGameInfo = {},
            navigateToSearchResult = { _, _ -> },
        )
    }
}