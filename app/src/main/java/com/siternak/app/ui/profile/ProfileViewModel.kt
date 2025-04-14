package com.siternak.app.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siternak.app.data.local.AuthPreference
import com.siternak.app.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    fun loadUserName(context: Context) {
        val preferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        _userName.value = preferences.getString("user_name", "Pengguna")
    }

    fun logoutUser() = viewModelScope.launch {
        authRepository.signOut()
    }
}