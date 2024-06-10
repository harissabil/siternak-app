package com.example.ternakapp.ui.post.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ternakapp.data.response.PostResponse
import com.example.ternakapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPostViewModel : ViewModel() {

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
                    _message.value = "Gagal memuat data"
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal memuat data: ${t.message}"
            }
        })
    }

    fun addNewPost(jenisTernak: String, jenisAksi: String, keterangan: String, latitude: Double, longitude: Double) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.addPost(jenisTernak, jenisAksi, keterangan, latitude, longitude)

        call.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _message.value = "Data berhasil ditambahkan"
                } else {
                    _message.value = "Gagal menambahkan data: ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal menambahkan data: ${t.message}"
            }
        })
    }

    fun updatePost(postId: String, jenisTernak: String, jenisAksi: String, keterangan: String) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.updatePost(postId, jenisTernak, jenisAksi, keterangan)

        call.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _message.value = "Data berhasil diperbarui"
                } else {
                    _message.value = "Gagal memperbarui data"
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal memperbarui data: ${t.message}"
            }
        })
    }
}