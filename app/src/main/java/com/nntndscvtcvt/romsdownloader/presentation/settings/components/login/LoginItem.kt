package com.nntndscvtcvt.romsdownloader.presentation.settings.components.login

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.presentation.settings.components.common.IconBox

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoginItem(
    navigateToLogin: () -> Unit
) {
    val archiveIcon = painterResource(R.drawable.outline_account_balance_24)
    val loginIcon = painterResource(R.drawable.outline_login_24)

    SegmentedListItem(
        onClick = navigateToLogin,
        shapes = ListItemDefaults.segmentedShapes(0, 2),
        colors = ListItemDefaults.segmentedColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            supportingContentColor = MaterialTheme.colorScheme.onSurface,
            trailingContentColor = MaterialTheme.colorScheme.primary
        ),
        leadingContent = {
            IconBox(archiveIcon)
        },
        trailingContent = {
            Icon(
                painter = loginIcon,
                contentDescription = null,
            )
        },
        supportingContent = {
            Text(
                text = stringResource(R.string.supporting_content),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        content = {
            Text(
                text = stringResource(R.string.login_headline_text),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}