package com.siternak.app.ui.profile.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siternak.app.domain.model.UserData
import com.siternak.app.domain.repository.FirestoreRepository
import kotlinx.coroutines.launch

class ProfileDetailViewModel(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _userProfile = MutableLiveData<UserData>()
    val userProfile: LiveData<UserData> = _userProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun getUserProfile() = viewModelScope.launch {
        _isLoading.value = true

        val userData = firestoreRepository.getUserData()
        userData.onSuccess {
            _isLoading.value = false
            _userProfile.value = it
        }.onFailure {
            _isLoading.value = false
            _message.value = it.message
        }
    }
}