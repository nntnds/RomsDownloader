package com.nntndscvtcvt.romsdownloader.presentation.login

import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.nntndscvtcvt.romsdownloader.R
import com.nntndscvtcvt.romsdownloader.data.utils.Constants.ARCHIVE_URL
import com.nntndscvtcvt.romsdownloader.presentation.utils.extractCookie

fun CreateLoginWebView(
    context: Context,
    onLoadingChange: (Boolean) -> Unit,
    onErrorChange: (String?) -> Unit,
    onCanGoBackChange: (Boolean) -> Unit,
    onCookiesExtracted: (sig: String, user: String) -> Unit
): WebView {
    return WebView(context).apply {
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            builtInZoomControls = true
            displayZoomControls = false

            allowFileAccess = false
            allowContentAccess = false

            setSupportZoom(true)
        }

        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                onLoadingChange(true)
                onErrorChange(null)
            }

            override fun onPageFinished(view: WebView, url: String) {
                onLoadingChange(false)
                onCanGoBackChange(view.canGoBack())

                val cookies = CookieManager.getInstance().getCookie(ARCHIVE_URL) ?: return
                val loggedInSig = cookies.extractCookie("logged-in-sig")
                val loggedInUser = cookies.extractCookie("logged-in-user")

                if (loggedInSig != null && loggedInUser != null) {
                    onCookiesExtracted(loggedInSig, loggedInUser)
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                if (request?.isForMainFrame == true) {
                    onLoadingChange(false)
                    onErrorChange(error?.description?.toString() ?: context.getString(R.string.unknown_error))
                }
            }
        }
    }
}