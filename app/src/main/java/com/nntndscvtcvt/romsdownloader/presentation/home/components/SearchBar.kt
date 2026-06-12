package com.nntndscvtcvt.romsdownloader.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.presentation.utils.Dimens

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchBar(
    onSearch: (String) -> Unit,
    query: String,
    onClear: () -> Unit,
    isSearchActive: Boolean,
    onSearchActiveChange: (Boolean) -> Unit
) {
    val backButton = painterResource(R.drawable.outline_keyboard_arrow_left_24)
    val searchButton = painterResource(R.drawable.outline_search_24)
    val clearButton = painterResource(R.drawable.outline_close_24)
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isSearchActive) {
        if (isSearchActive && query.isEmpty()) focusRequester.requestFocus()
    }

    DisposableEffect(Unit) {
        onDispose { focusManager.clearFocus() }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(TopAppBarDefaults.TopAppBarExpandedHeight)
            .padding(horizontal = Dimens.PaddingSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (isSearchActive || query.isNotEmpty()) {
            IconButton(onClick = {
                onSearchActiveChange(false)
                onClear()
            }) {
                Icon(
                    painter = backButton,
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.iconMediumHeight)
                )
            }

            BasicTextField(
                value = TextFieldValue(
                    text = query,
                    selection = TextRange(query.length)
                ),
                onValueChange = { onSearch(it.text) },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { focusManager.clearFocus() }
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                stringResource(R.string.search_games),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        innerTextField()
                    }
                }
            )
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(clearButton, null)
                }
            }
        } else {
            Text(
                text = stringResource(R.string.search_games),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = Dimens.PaddingLarge)
            )
            IconButton(
                onClick = { onSearchActiveChange(true) }
            ) {
                Icon(searchButton, null)
            }
        }
    }
}