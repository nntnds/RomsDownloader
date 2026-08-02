package com.nntndscvtcvt.romsdownloader.presentation.settings.components.login

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.presentation.settings.SettingsConnectionStatus
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ConnectionItem(
    onCheckConnection: () -> Unit,
    settingsConnectionStatus: SettingsConnectionStatus
) {
    val refreshIcon = ImageVector.vectorResource(R.drawable.outline_refresh_24)
    val errorIcon = ImageVector.vectorResource(R.drawable.outline_error_24)
    val checkCircleIcon = ImageVector.vectorResource(R.drawable.outline_check_circle_24)
    val accountOff = ImageVector.vectorResource(R.drawable.outline_account_circle_off_24)
    val wifiIcon = ImageVector.vectorResource(R.drawable.outline_android_wifi_3_bar_24)

    SegmentedListItem(
        onClick = onCheckConnection,
        shapes = ListItemDefaults.segmentedShapes(1, 2),
        colors = ListItemDefaults.segmentedColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            supportingContentColor = MaterialTheme.colorScheme.onSurface,
            trailingContentColor = MaterialTheme.colorScheme.primary
        ),
        leadingContent = {
            // TODO
        },
        trailingContent = {
            when (settingsConnectionStatus) {
                SettingsConnectionStatus.Checking -> CircularProgressIndicator(
                    modifier = Modifier.size(Dimens.iconDefaultSize),
                    strokeWidth = 2.dp
                )
                SettingsConnectionStatus.Success -> Icon(
                    imageVector = checkCircleIcon,
                    contentDescription = null
                )
                SettingsConnectionStatus.Error -> Icon(
                    imageVector = errorIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                SettingsConnectionStatus.NotLoggedIn -> Icon(
                    imageVector = accountOff,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                SettingsConnectionStatus.Idle -> Icon(
                    imageVector = refreshIcon,
                    contentDescription = null
                )
            }
        },
        supportingContent = {
            Text(
                text = when (settingsConnectionStatus) {
                    SettingsConnectionStatus.Checking -> stringResource(R.string.checking)
                    SettingsConnectionStatus.Success -> stringResource(R.string.archive_org_is_reachable)
                    SettingsConnectionStatus.Error -> stringResource(R.string.cannot_reach_archive_org)
                    SettingsConnectionStatus.NotLoggedIn -> stringResource(R.string.log_into_your_account)
                    SettingsConnectionStatus.Idle -> stringResource(R.string.tap_to_check)
                },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        content = {
            Text(
                text = stringResource(R.string.archive_org_connection),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}