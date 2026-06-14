package com.nntndscvtcvt.romsdownloader.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nntndscvtcvt.romsdownloader.domain.repository.CookieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: CookieRepository
) : ViewModel() {
    private val _isLoginSuccessful = MutableStateFlow(false)
    val isLoginSuccessful = _isLoginSuccessful.asStateFlow()

    fun saveCookie(sig: String, user: String) = viewModelScope.launch {
        repository.saveCookies(sig, user)
        _isLoginSuccessful.value = true
    }

    fun clearCookie() = viewModelScope.launch {
        repository.clearCookies()
        _isLoginSuccessful.value = false
    }
}