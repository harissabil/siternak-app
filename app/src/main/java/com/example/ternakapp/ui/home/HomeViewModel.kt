package com.example.ternakapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ternakapp.data.response.ListPostLoc
import com.example.ternakapp.data.response.Post
import com.example.ternakapp.data.response.PostResponse
import com.example.ternakapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun getAllPostsWithLoc() {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.getAllPosts()

        call.enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    _posts.value = apiResponse?.toList()
                } else {
                    _message.value = "Gagal memuat data: ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal memuat data: ${t.message}"
            }
        })
    }
}