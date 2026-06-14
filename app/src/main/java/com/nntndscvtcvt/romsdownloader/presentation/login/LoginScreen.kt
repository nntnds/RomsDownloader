package com.nntndscvtcvt.romsdownloader.presentation.login

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.nntndscvtcvt.romsdownloader.data.utils.Constants.ARCHIVE_URL
import com.nntndscvtcvt.romsdownloader.data.utils.Constants.LOGIN_URL
import com.nntndscvtcvt.romsdownloader.presentation.login.components.LoginScreenTopBar
import com.nntndscvtcvt.romsdownloader.presentation.utils.extractCookie
import org.koin.androidx.compose.koinViewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginScreen(
    onBack: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val webViewRef = remember { mutableStateOf<WebView?>(null) }

    Scaffold(
        topBar = {
            LoginScreenTopBar(
                onBack = onBack,
                onRefreshClick = { webViewRef.value?.reload() },
                onClear = {
                    CookieManager.getInstance().removeAllCookies(null)
                    CookieManager.getInstance().flush()
                    viewModel.clearCookie()
                    webViewRef.value?.reload()
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        AndroidView(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.useWideViewPort = true

                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView,
                            request: WebResourceRequest
                        ): Boolean = false

                        override fun onPageFinished(view: WebView, url: String) {
                            val cookies = CookieManager.getInstance().getCookie(ARCHIVE_URL) ?: return
                            val loggedInSig = cookies.extractCookie("logged-in-sig")
                            val loggedInUser = cookies.extractCookie("logged-in-user")
                            Log.d("COOKIE", "$loggedInSig")
                            Log.d("COOKIE", "$loggedInUser")

                            if (loggedInSig != null && loggedInUser != null) {
                                viewModel.saveCookie(loggedInSig, loggedInUser)
                            }
                        }
                    }
                    loadUrl(LOGIN_URL)
                }.also { webViewRef.value = it }
            }
        )
    }
}

