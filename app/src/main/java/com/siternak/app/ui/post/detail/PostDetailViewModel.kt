package com.siternak.app.ui.post.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.siternak.app.data.response.DeleteResponse
import com.siternak.app.data.response.PostResponse
import com.siternak.app.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostDetailViewModel : ViewModel() {

    private val _post = MutableLiveData<PostResponse?>()
    val post: LiveData<PostResponse?> = _post

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun loadPostDetails(token: String, postId: String) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.getPostById("Bearer $token", postId)

        call.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _post.value = response.body()
                } else {
                    _message.value = "Gagal memuat post"
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal memuat post: ${t.message}"
            }
        })
    }

    fun deletePost(token: String, postId: String) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.deletePost("Bearer $token", postId)

        call.enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(call: Call<DeleteResponse>, response: Response<DeleteResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _message.value = "Post berhasil dihapus"
                    _post.value = null
                } else {
                    _message.value = "Gagal menghapus post"
                }
            }

            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal menghapus post: ${t.message}"
            }
        })
    }
}