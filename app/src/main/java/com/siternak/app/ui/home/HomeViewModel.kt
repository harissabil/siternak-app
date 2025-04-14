package com.siternak.app.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.siternak.app.data.response.ListPostLoc
import com.siternak.app.data.response.PostLoc
import com.siternak.app.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _posts = MutableLiveData<List<PostLoc>>()
    val posts: LiveData<List<PostLoc>> = _posts

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    // Fungsi untuk memuat nama pengguna dari SharedPreferences, nama akan ditampilkan di bagian greeting
    fun loadUserName(context: Context) {
        val preferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        _userName.value = preferences.getString("user_name", "Pengguna")
    }

    fun getAllPostsWithLoc(token: String) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.getAllPosts("Bearer $token")

        call.enqueue(object : Callback<ListPostLoc> {
            override fun onResponse(call: Call<ListPostLoc>, response: Response<ListPostLoc>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    _posts.value = apiResponse?.data
                } else {
                    _message.value = "Gagal memuat data: ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<ListPostLoc>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal memuat data: ${t.message}"
            }
        })
    }
}