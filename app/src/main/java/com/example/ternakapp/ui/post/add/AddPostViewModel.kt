package com.example.ternakapp.ui.post.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ternakapp.data.response.PostDataClass
import com.example.ternakapp.data.response.PostResponse
import com.example.ternakapp.data.response.UpdatePostDataClass
import com.example.ternakapp.data.retrofit.ApiConfig
import com.google.gson.Gson
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
                    _message.value = "Gagal memuat data"
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal memuat data: ${t.message}"
            }
        })
    }

    fun addNewPost(token: String, jenisTernak: String, jumlahTernak: String, jenisAksi: String, keteranganAksi: String, alamatAksi: String, latitude: String, longitude: String) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val postData = PostDataClass(jenisTernak, jumlahTernak, jenisAksi, keteranganAksi, alamatAksi, latitude, longitude)
        Log.d("AddPostViewModel", "Preparing to add new post: $postData")

        val call = apiService.addPost("Bearer $token", postData)

        call.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _message.value = "Data berhasil ditambahkan"
                } else {
                    response.errorBody()?.let { errorBody ->
                        try {
                            val errorResponse = Gson().fromJson(errorBody.string(), PostResponse::class.java)
                            _message.value = "Gagal menambahkan data: ${errorResponse.message}"
                        } catch (e: Exception) {
                            e.printStackTrace()
                            _message.value = "Gagal menambahkan data"
                        }
                    } ?: run {
                        _message.value = "Gagal menambahkan data"
                    }
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal menambahkan data: ${t.message}"
            }
        })
    }

    fun updatePost(token: String, postId: String, jenisTernak: String, jumlahTernak: String, jenisAksi: String, keteranganAksi: String, alamatAksi: String) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.updatePost("Bearer $token", postId, UpdatePostDataClass(jenisTernak, jumlahTernak, jenisAksi, keteranganAksi, alamatAksi))

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