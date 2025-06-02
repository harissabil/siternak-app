package com.siternak.app.ui.pmk_detector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siternak.app.domain.repository.FirestoreRepository
import kotlinx.coroutines.launch

class PmkDetectorViewModel(
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    init {
        loadUserName()
    }

    private fun loadUserName() {
        if (_userName.value != null) return // Skip if already loaded

        viewModelScope.launch {
            val userData = firestoreRepository.getUserData()
            userData.onSuccess {
                _userName.value = it?.nama
            }.onFailure {
                _message.value = "Gagal memuat data pengguna: ${it.message}"
            }
        }
    }
}