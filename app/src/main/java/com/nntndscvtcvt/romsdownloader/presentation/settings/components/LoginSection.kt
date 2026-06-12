package com.nntndscvtcvt.romsdownloader.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nntndscvtcvt.romsdownloader.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoginSection(
    onNavigate: () -> Unit
) {
    val loginIcon = painterResource(R.drawable.outline_login_24)
    val archiveIcon = painterResource(R.drawable.outline_account_balance_24)

    SegmentedListItem(
        onClick = onNavigate,
        shapes = ListItemDefaults.shapes(MaterialTheme.shapes.large),
        colors = ListItemDefaults.segmentedColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            supportingContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            trailingContentColor = MaterialTheme.colorScheme.primary
        ),
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = archiveIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        trailingContent = {
            Icon(
                painter = loginIcon,
                contentDescription = null,
            )
        },
        supportingContent = {
            Text(stringResource(R.string.supporting_content))
        }
    ) {
        Text(stringResource(R.string.login_headline_text))
    }
}