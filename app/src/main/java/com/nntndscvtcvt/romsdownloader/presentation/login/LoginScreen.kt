package com.nntndscvtcvt.romsdownloader.presentation.login

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.nntndscvtcvt.romsdownloader.data.utils.Constants.LOGIN_URL
import com.nntndscvtcvt.romsdownloader.presentation.components.ShowError
import com.nntndscvtcvt.romsdownloader.presentation.components.ShowLoading
import com.nntndscvtcvt.romsdownloader.presentation.login.components.LoginScreenTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    onBack: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val webViewBundle = rememberSaveable { Bundle() }
    val webViewRef = remember { mutableStateOf<WebView?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var canGoBack by remember { mutableStateOf(false) }

    BackHandler(enabled = canGoBack) { webViewRef.value?.goBack() }

    Scaffold(
        topBar = {
            LoginScreenTopBar(
                onBack = onBack,
                onRefreshClick = {
                    errorMessage = null
                    isLoading = true
                    webViewRef.value?.reload()
                },
                onClear = {
                    CookieManager.getInstance().removeAllCookies { _ ->
                        CookieManager.getInstance().flush()
                        webViewRef.value?.apply {
                            clearCache(true)
                            clearHistory()
                            reload()
                        }
                    }
                    webViewBundle.clear()
                    viewModel.clearCookie()
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    CreateLoginWebView(
                        context = ctx,
                        onLoadingChange = { isLoading = it },
                        onErrorChange = { errorMessage = it },
                        onCanGoBackChange = { canGoBack = it },
                        onCookiesExtracted = viewModel::saveCookie
                    ).also { webView ->
                        if (webViewBundle.isEmpty) {
                            webView.loadUrl(LOGIN_URL)
                        } else {
                            webView.restoreState(webViewBundle)
                        }
                        webViewRef.value = webView
                    }
                },
                onRelease = { webView ->
                    webView.saveState(webViewBundle)
                    (webView.parent as? ViewGroup)?.removeView(webView)
                    webView.stopLoading()
                    webView.webViewClient = WebViewClient()
                    webView.destroy()
                }
            )
            if (isLoading) {
                ShowLoading(Modifier.fillMaxSize())
            }
            if (errorMessage != null) {
                ShowError(
                    modifier = Modifier.fillMaxSize(),
                    e = Throwable(errorMessage)
                )
            }
        }
    }
}