package com.nntndscvtcvt.romsdownloader.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.repository.CookieRepository
import kotlinx.coroutines.launch
import java.net.URLDecoder

class LoginViewModel(
    private val repository: CookieRepository
) : ViewModel() {
    fun saveCookie(sig: String, user: String) = viewModelScope.launch {
        repository.saveCookies(sig, user)
    }
    fun clearCookie() = viewModelScope.launch{ repository.clearCookies() }

    fun extractCookie(cookieString: String, cookieName: String): String? {
        if (cookieString.isNullOrBlank()) return null
        return Regex("(?:^|;\\s*)$cookieName=([^;]*)")
            .find(cookieString)
            ?.groups?.get(1)?.value
            ?.trim()
            ?.takeIf { it.isNotBlank() }
            ?.let { URLDecoder.decode(it, "UTF-8") }
    }
}