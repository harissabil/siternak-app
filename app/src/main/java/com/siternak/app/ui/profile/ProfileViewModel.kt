package com.siternak.app.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siternak.app.domain.repository.AuthRepository
import com.siternak.app.domain.repository.FirestoreRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _photoUrl = MutableLiveData<String?>()
    val photoUrl: LiveData<String?> = _photoUrl

    fun loadUserName() = viewModelScope.launch {
        val userData = firestoreRepository.getUserData()
        userData.onSuccess {
            _userName.value = it?.nama
        }

        val photoData = authRepository.getUser()
        photoData.onSuccess {
            Log.d("ProfileViewModel", "Photo URL: ${it.profilePictureUrl}")
            _photoUrl.value = it.profilePictureUrl
        }
    }

    fun logoutUser() = viewModelScope.launch {
        authRepository.signOut()
    }
}