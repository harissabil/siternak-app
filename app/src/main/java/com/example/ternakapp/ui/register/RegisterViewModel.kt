package com.example.ternakapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ternakapp.data.response.RegisterDataClass
import com.example.ternakapp.data.response.RegisterResponse
import com.example.ternakapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _isRegisterSuccess = MutableLiveData<Boolean>()
    val isRegisterSuccess: LiveData<Boolean> = _isRegisterSuccess

    fun registerUser(
        noTelp: String,
        password: String,
        nama: String,
        provinsi: String,
        kota: String,
        kecamatan: String,
        alamat: String,
    ) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.registerUser(RegisterDataClass(noTelp, password, nama, provinsi, kota, kecamatan, alamat))

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _isLoading.value = false
                if (response.isSuccessful && response.body()?.status == "success") {
                    _message.value = "Registrasi berhasil"
                    _isRegisterSuccess.value = true
                } else {
                    _message.value = response.body()?.message ?: "Registrasi gagal"
                    _isRegisterSuccess.value = false
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = "Gagal: ${t.message}"
                _isRegisterSuccess.value = false
            }
        })
    }
}