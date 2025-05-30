package com.siternak.app.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siternak.app.domain.model.Post
import com.siternak.app.domain.repository.FirestoreRepository
import kotlinx.coroutines.launch

class PostViewModel(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    // Fungsi untuk memuat data post
    fun loadPosts() {
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

    // reload data setelah penghapusan
    fun reloadPosts() {
        loadPosts()
    }
}
