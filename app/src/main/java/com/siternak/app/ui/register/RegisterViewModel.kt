package com.siternak.app.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.siternak.app.data.response.RegisterDataClass
import com.siternak.app.data.response.RegisterResponse
import com.siternak.app.data.retrofit.ApiConfig
import com.google.gson.Gson
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
                if ((response.isSuccessful && response.body()?.status == "success") || response.code() == 201) {
                    _message.value = "Registrasi berhasil"
                    _isRegisterSuccess.value = true
                } else {
                    // Ambil error message dari errorBody jika response tidak berhasil
                    val errorMessage = response.errorBody()?.let { errorBody ->
                        try {
                            val errorResponse = Gson().fromJson(errorBody.string(), RegisterResponse::class.java)
                            errorResponse.message
                        } catch (e: Exception) {
                            e.printStackTrace()
                            "Unknown error"
                        }
                    } ?: response.body()?.message ?: "Unknown error"

                    _message.value = errorMessage
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