package com.siternak.app.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.siternak.app.data.local.AuthPreference

class ProfileViewModel : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    fun loadUserName(context: Context) {
        val preferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        _userName.value = preferences.getString("user_name", "Pengguna")
    }

    fun logoutUser(context: Context) {
        val authPreference = AuthPreference(context)
        authPreference.clearToken()
        val preferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        preferences.edit().clear().apply()
    }
}