package com.example.ternakapp.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ternakapp.data.response.PostResponse
import com.example.ternakapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostViewModel : ViewModel() {
    private val _posts = MutableLiveData<List<PostResponse>>()
    val posts: LiveData<List<PostResponse>> = _posts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun loadPosts() {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.getAllPosts()

        call.enqueue(object : Callback<List<PostResponse>> {
            override fun onResponse(call: Call<List<PostResponse>>, response: Response<List<PostResponse>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _posts.value = response.body()
                } else {
                    _message.value = "Gagal memuat data"
                }
            }

            override fun onFailure(call: Call<List<PostResponse>>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal memuat data: ${t.message}"
            }
        })
    }
}