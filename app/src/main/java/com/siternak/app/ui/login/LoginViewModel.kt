package com.siternak.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.siternak.app.data.response.LoginDataClass
import com.siternak.app.data.response.LoginResponse
import com.siternak.app.data.retrofit.ApiConfig
import com.google.gson.Gson
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
                _isLoading.value = false
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (!errorBody.isNullOrEmpty()) {
                        // Parse errorBody to get the message
                        try {
                            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                            errorResponse.message
                        } catch (e: Exception) {
                            "Gagal login: ${response.message()}"
                        }
                    } else {
                        "Gagal login: ${response.message()}"
                    }
                    _message.value = errorMessage
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal: ${t.message}"
            }
        })
    }
}