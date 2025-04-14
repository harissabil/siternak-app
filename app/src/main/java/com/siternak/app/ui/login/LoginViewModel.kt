package com.siternak.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siternak.app.domain.repository.AuthRepository
import com.siternak.app.domain.repository.FirestoreRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFormAlreadyFilled = MutableLiveData<Boolean?>(null)
    val isFormAlreadyFilled: LiveData<Boolean?> = _isFormAlreadyFilled

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message


    fun loginWithEmail(email: String, password: String) {
        _isLoading.value = true

        viewModelScope.launch {
            val result = authRepository.signInWithEmail(email, password)
            result.fold(
                onSuccess = { handleLoginSuccess() },
                onFailure = { handleLoginFailure(it) }
            )
        }
    }

    fun loginWithGoogle(token: String) {
        _isLoading.value = true

        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(token)
            result.fold(
                onSuccess = { handleLoginSuccess() },
                onFailure = { handleLoginFailure(it) }
            )
        }
    }

    private suspend fun handleLoginSuccess() {
        val isFormFilled = firestoreRepository.getUserData()
        isFormFilled.fold(
            onSuccess = { userData ->
                _isFormAlreadyFilled.value = userData != null
                _isSuccess.value = true
            },
            onFailure = { handleLoginFailure(it) }
        )
        _isLoading.value = false
    }

    private fun handleLoginFailure(exception: Throwable) {
        _isLoading.value = false
        _isSuccess.value = false
        _message.value = "Gagal login: ${exception.message}"
    }
}