package com.example.ternakapp.ui.profile.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ternakapp.data.response.UserDataClass
import com.example.ternakapp.data.response.UserResponse
import com.example.ternakapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileDetailViewModel : ViewModel() {

    private val _userProfile = MutableLiveData<UserDataClass>()
    val userProfile: LiveData<UserDataClass> = _userProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun getUserProfile(token: String) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.getUserProfile("Bearer $token")

        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        _userProfile.value = it
                    } ?: run {
                        _message.value = "Data pengguna tidak ditemukan"
                    }
                } else {
                    _message.value = "Gagal memuat data: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal memuat data: ${t.message}"
            }
        })
    }
}