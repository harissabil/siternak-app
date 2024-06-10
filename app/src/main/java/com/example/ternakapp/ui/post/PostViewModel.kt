package com.example.ternakapp.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ternakapp.data.response.ApiPostResponse
import com.example.ternakapp.data.response.PostResponse
import com.example.ternakapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostViewModel : ViewModel() {
    private val _posts = MutableLiveData<PostResponse>()
    val posts: LiveData<PostResponse> = _posts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun loadPosts() {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.getAllPosts()

        call.enqueue(object : Callback<ApiPostResponse> {
            override fun onResponse(call: Call<ApiPostResponse>, response: Response<ApiPostResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.dataPost?.post?.let {
                        _posts.value = it
                    } ?: run {
                        _message.value = "Belum ada data"
                    }
                } else {
                    _message.value = "Gagal memuat data"
                }
            }

            override fun onFailure(call: Call<ApiPostResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal memuat data: ${t.message}"
            }
        })
    }
}