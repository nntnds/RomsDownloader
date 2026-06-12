package com.nntndscvtcvt.romsdownloader.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.repository.CookieRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: CookieRepository
) : ViewModel() {
    fun saveCookie(sig: String, user: String) = viewModelScope.launch {
        repository.saveCookies(sig, user)
    }

    fun clearCookie() = viewModelScope.launch { repository.clearCookies() }
}