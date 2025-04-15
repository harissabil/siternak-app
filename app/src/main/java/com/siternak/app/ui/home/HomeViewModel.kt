package com.siternak.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siternak.app.domain.model.Post
import com.siternak.app.domain.repository.FirestoreRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _isFormUserAlreadyFilled = MutableLiveData<Boolean>(true)
    val isFormUserAlreadyFilled: LiveData<Boolean> = _isFormUserAlreadyFilled

    private var isDataInitialized = false

    fun loadUserName() {
        if (_userName.value != null) return // Skip if already loaded

        viewModelScope.launch {
            val userData = firestoreRepository.getUserData()
            userData.onSuccess {
                if (it == null) {
                    _isFormUserAlreadyFilled.value = false
                } else {
                    _userName.value = it.nama
                    if (!isDataInitialized) {
                        getAllPostsWithLoc()
                    }
                }
            }.onFailure {
                _message.value = "Gagal memuat data pengguna: ${it.message}"
            }
        }
    }

    fun getAllPostsWithLoc() {
        // Skip loading if already fetching or already have data
        if (_isLoading.value == true || (!_posts.value.isNullOrEmpty() && isDataInitialized)) return

        isDataInitialized = true
        _isLoading.value = true

        viewModelScope.launch {
            firestoreRepository.getAllPosts().collect { response ->
                response.onSuccess {
                    _posts.value = it
                    _isLoading.value = false
                }.onFailure {
                    _message.value = "Gagal memuat data: ${it.message}"
                    _isLoading.value = false
                }
            }
        }
    }
}