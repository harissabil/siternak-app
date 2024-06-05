package com.example.ternakapp.ui.post.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ternakapp.data.response.PostResponse
import com.example.ternakapp.data.retrofit.ApiConfig
import okhttp3.ResponseBody
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

    fun loadPostDetails(postId: String) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.getPostById(postId)

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

    fun deletePost(postId: String) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.deletePost(postId)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _message.value = "Post berhasil dihapus"
                    _post.value = null
                } else {
                    _message.value = "Gagal menghapus post"
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal menghapus post: ${t.message}"
            }
        })
    }
}