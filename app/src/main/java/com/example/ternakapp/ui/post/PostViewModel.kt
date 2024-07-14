package com.example.ternakapp.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ternakapp.data.response.ListPostItem
import com.example.ternakapp.data.response.PostItem
import com.example.ternakapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostViewModel : ViewModel() {
    private val _posts = MutableLiveData<List<PostItem>>()
    val posts: LiveData<List<PostItem>> = _posts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    // Fungsi untuk memuat data post dari API
    fun loadPosts(token: String) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.getAllPostsByUserId("Bearer $token")

        call.enqueue(object : Callback<ListPostItem> {
            override fun onResponse(call: Call<ListPostItem>, response: Response<ListPostItem>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        // Urutkan post berdasarkan createdAt dalam urutan menurun
                        val sortedPosts = it.data.posts.sortedByDescending { post -> post.createdAt }
                        _posts.value = sortedPosts
                    } ?: run {
                        _message.value = "Belum ada data"
                    }
                } else {
                    _message.value = "Gagal memuat data"
                }
            }

            override fun onFailure(call: Call<ListPostItem>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal memuat data: ${t.message}"
            }
        })
    }

    // reload data setelah penghapusan
    fun reloadPosts(token: String) {
        loadPosts(token)
    }
}
