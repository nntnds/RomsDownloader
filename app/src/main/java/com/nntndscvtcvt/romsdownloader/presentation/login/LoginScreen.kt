package com.nntndscvtcvt.romsdownloader.presentation.login

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nntndscvtcvt.romsdownloader.data.utils.Constants.ARCHIVE_URL
import com.nntndscvtcvt.romsdownloader.data.utils.Constants.LOGIN_URL
import com.nntndscvtcvt.romsdownloader.presentation.components.ShowError
import com.nntndscvtcvt.romsdownloader.presentation.components.ShowLoading
import com.nntndscvtcvt.romsdownloader.presentation.login.components.LoginScreenTopBar
import com.nntndscvtcvt.romsdownloader.presentation.utils.extractCookie
import org.koin.androidx.compose.koinViewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginScreen(
    onBack: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val isLoginSuccessful by viewModel.isLoginSuccessful.collectAsStateWithLifecycle()
    val webViewRef = remember { mutableStateOf<WebView?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            LoginScreenTopBar(
                onBack = onBack,
                onRefreshClick = {
                    hasError = false
                    isLoading = true
                    webViewRef.value?.reload()
                },
                onClear = {
                    CookieManager.getInstance().removeAllCookies(null)
                    CookieManager.getInstance().flush()
                    viewModel.clearCookie()
                    webViewRef.value?.clearCache(true)
                    webViewRef.value?.reload()
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.useWideViewPort = true
                        settings.loadWithOverviewMode = true
                        settings.setSupportZoom(true)
                        settings.builtInZoomControls = true
                        settings.displayZoomControls = false

                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView,
                                request: WebResourceRequest
                            ): Boolean = false

                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                isLoading = true
                                hasError = false
                            }

                            override fun onPageFinished(view: WebView, url: String) {
                                isLoading = false
                                val cookies = CookieManager.getInstance().getCookie(ARCHIVE_URL) ?: return
                                val loggedInSig = cookies.extractCookie("logged-in-sig")
                                val loggedInUser = cookies.extractCookie("logged-in-user")

                                if (loggedInSig != null && loggedInUser != null) {
                                    viewModel.saveCookie(loggedInSig, loggedInUser)
                                }
                            }
                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                if (request?.isForMainFrame == true) {
                                    isLoading = false
                                    hasError = true
                                }
                            }
                        }
                        loadUrl(LOGIN_URL)
                    }.also { webViewRef.value = it }
                },
                onRelease = { webView ->
                    webView.stopLoading()
                    webView.webViewClient = WebViewClient()
                    webView.destroy()
                }
            )
            if (isLoading) {
                ShowLoading(
                    Modifier
                        .padding(innerPadding)
                )
            }
            if (hasError) {
                ShowError(
                    modifier = Modifier.fillMaxSize(),
                    e = Throwable("No internet connection")
                )
            }
        }
    }
}