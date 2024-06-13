package com.example.ternakapp.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ternakapp.data.response.ApiResponse
import com.example.ternakapp.data.response.LoginDataClass
import com.example.ternakapp.data.response.LoginResponse
import com.example.ternakapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(): ViewModel() {
    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message


    fun loginUser(noTelp: String, password: String) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.loginUser(LoginDataClass(noTelp, password))

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                //Log.d("LoginViewModel", "onResponse: ${response.body()}")
                _isLoading.value = false
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                } else {
                    _message.value = response.body()?.message ?: "Gagal login: ${response.body()?.message}"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal: ${t.message}"
            }
        })
    }
}