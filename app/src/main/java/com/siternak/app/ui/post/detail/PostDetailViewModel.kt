package com.siternak.app.ui.post.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siternak.app.domain.model.Post
import com.siternak.app.domain.repository.FirestoreRepository
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {

    private val _post = MutableLiveData<Post?>()
    val post: LiveData<Post?> = _post

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun loadPostDetails(postId: String) = viewModelScope.launch {
        _isLoading.value = true

        val postDetailResult = firestoreRepository.getPostById(postId)
        postDetailResult.onSuccess {
            _isLoading.value = false
            _post.value = it
        }.onFailure { exception ->
            Log.e("AddPostViewModel", "Error fetching post details: ${exception.message}")
            _message.value = "Gagal memuat data dari Firestore"
            _isLoading.value = false
        }
    }

    fun deletePost(postId: String) = viewModelScope.launch {
        _isLoading.value = true

        val deletePostResult = firestoreRepository.deletePost(postId)
        deletePostResult.onSuccess {
            _isLoading.value = false
            _message.value = "Berhasil menghapus data"
        }.onFailure { exception ->
            Log.e("AddPostViewModel", "Error deleting post: ${exception.message}")
            _message.value = "Gagal menghapus data"
            _isLoading.value = false
        }
    }
}