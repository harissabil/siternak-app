package com.siternak.app.ui.user_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siternak.app.domain.model.UserData
import com.siternak.app.domain.repository.FirestoreRepository
import kotlinx.coroutines.launch

class UserFormViewModel(
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    private val _isSaveSuccess = MutableLiveData<Boolean>()
    val isSaveSuccess: LiveData<Boolean> = _isSaveSuccess

    fun saveUserData(
        nama: String,
        noTelp: String,
        provinsi: String,
        kota: String,
        kecamatan: String,
        alamat: String,
    ) {
        _isLoading.value = true

        viewModelScope.launch {
            val addUserDataResult = firestoreRepository.addUserData(
                UserData(
                    uid = "",
                    nama = nama,
                    nomorHp = noTelp,
                    provinsi = provinsi,
                    kota = kota,
                    kecamatan = kecamatan,
                    alamat = alamat,
                )
            )

            addUserDataResult.fold(
                onSuccess = {
                    _isSaveSuccess.value = true
                    _isLoading.value = false
                },
                onFailure = {
                    _message.value = it.message
                    _isLoading.value = false
                }
            )
        }
    }
}