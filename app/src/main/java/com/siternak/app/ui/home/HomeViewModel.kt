package com.siternak.app.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siternak.app.data.response.ListPostLoc
import com.siternak.app.data.response.PostLoc
import com.siternak.app.data.retrofit.ApiConfig
import com.siternak.app.domain.repository.FirestoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {
    private val _posts = MutableLiveData<List<PostLoc>>()
    val posts: LiveData<List<PostLoc>> = _posts

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _isFormUserAlreadyFilled = MutableLiveData<Boolean>(true)
    val isFormUserAlreadyFilled: LiveData<Boolean> = _isFormUserAlreadyFilled

    init {
        loadUserName()
    }

    fun loadUserName() = viewModelScope.launch {
        val userData = firestoreRepository.getUserData()
        userData.onSuccess {
            if (it == null) {
                _isFormUserAlreadyFilled.value = false
                return@onSuccess
            } else {
                _userName.value = it.nama
            }
        }.onFailure {
            _message.value = "Gagal memuat data pengguna: ${it.message}"
        }
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