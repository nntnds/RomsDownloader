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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.presentation.settings.ConnectionStatus
import com.nntndscvtcvt.romsdownloader.presentation.settings.components.common.IconBox
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ConnectionItem(
    onCheckConnection: () -> Unit,
    connectionStatus: ConnectionStatus
) {
    val refreshIcon = painterResource(R.drawable.outline_refresh_24)
    val errorIcon = painterResource(R.drawable.outline_error_24)
    val checkCircleIcon = painterResource(R.drawable.outline_check_circle_24)
    val accountOff = painterResource(R.drawable.outline_account_circle_off_24)
    val wifiIcon = R.drawable.outline_android_wifi_3_bar_24

    SegmentedListItem(
        onClick = onCheckConnection,
        shapes = ListItemDefaults.segmentedShapes(1, 2),
        colors = ListItemDefaults.segmentedColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            supportingContentColor = MaterialTheme.colorScheme.onSurface,
            trailingContentColor = MaterialTheme.colorScheme.primary
        ),
        leadingContent = {
            IconBox(wifiIcon)
        },
        trailingContent = {
            when (connectionStatus) {
                ConnectionStatus.Checking -> CircularProgressIndicator(
                    modifier = Modifier.size(Dimens.iconDefaultSize),
                    strokeWidth = 2.dp
                )
                ConnectionStatus.Success -> Icon(
                    painter = checkCircleIcon,
                    contentDescription = null
                )
                ConnectionStatus.Error -> Icon(
                    painter = errorIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                ConnectionStatus.NotLoggedIn -> Icon(
                    painter = accountOff,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                ConnectionStatus.Idle -> Icon(
                    painter = refreshIcon,
                    contentDescription = null
                )
            }
        },
        supportingContent = {
            Text(
                text = when (connectionStatus) {
                    ConnectionStatus.Checking -> stringResource(R.string.checking)
                    ConnectionStatus.Success -> stringResource(R.string.archive_org_is_reachable)
                    ConnectionStatus.Error -> stringResource(R.string.cannot_reach_archive_org)
                    ConnectionStatus.NotLoggedIn -> stringResource(R.string.log_into_your_account)
                    ConnectionStatus.Idle -> stringResource(R.string.tap_to_check)
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