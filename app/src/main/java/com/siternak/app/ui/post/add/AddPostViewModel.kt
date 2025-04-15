package com.siternak.app.ui.post.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siternak.app.domain.model.Post
import com.siternak.app.domain.repository.FirestoreRepository
import kotlinx.coroutines.launch

class AddPostViewModel(
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {

    private val _post = MutableLiveData<Post?>()
    val post: LiveData<Post?> = _post

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun loadPostDetails(postId: String) = viewModelScope.launch {
        _isLoading.value = true

        val postDetailResult = firestoreRepository.getPostById(postId)
        postDetailResult.onSuccess {
            _isLoading.value = false
            _post.value = it
        }.onFailure { exception ->
            Log.e("AddPostViewModel", "Error fetching post details: ${exception.message}")
            _message.value = "Gagal memuat data dari Firestore"
            _isLoading.value = false
        }
    }

    fun addNewPost(
        jenisTernak: String,
        jumlahTernak: String,
        jenisAksi: String,
        keteranganAksi: String,
        alamatAksi: String,
        latitude: Double,
        longitude: Double,
    ) = viewModelScope.launch {
        _isLoading.value = true

        val addNewPostResult = firestoreRepository.addPost(
            post = Post(
                jenisTernak = jenisTernak,
                jumlahTernak = jumlahTernak,
                jenisAksi = jenisAksi,
                keteranganAksi = keteranganAksi,
                alamatAksi = alamatAksi,
                latitude = latitude,
                longitude = longitude,
            )
        )

        addNewPostResult.onSuccess {
            _isLoading.value = false
            _message.value = "Data berhasil ditambahkan"
        }.onFailure { exception ->
            Log.e("AddPostViewModel", "Error adding new post: ${exception.message}")
            _message.value = "Gagal menambahkan data ke Firestore"
            _isLoading.value = false
        }
    }

    fun updatePost(
        postId: String,
        jenisTernak: String,
        jumlahTernak: String,
        jenisAksi: String,
        keteranganAksi: String,
        alamatAksi: String,
    ) = viewModelScope.launch {
        _isLoading.value = true

        val updatePostResult = firestoreRepository.updatePost(
            post = Post(
                jenisTernak = jenisTernak,
                jumlahTernak = jumlahTernak,
                jenisAksi = jenisAksi,
                keteranganAksi = keteranganAksi,
                alamatAksi = alamatAksi,
                id = postId,
                uid = _post.value!!.uid,
                latitude = _post.value!!.latitude,
                longitude = _post.value!!.longitude,
                createdAt = _post.value!!.createdAt,
                updatedAt = _post.value!!.updatedAt,
                petugas = _post.value!!.petugas,
                status = _post.value!!.status,
            )
        )

        updatePostResult.onSuccess {
            _isLoading.value = false
            _message.value = "Data berhasil diperbarui"
        }.onFailure { exception ->
            Log.e("AddPostViewModel", "Error updating post: ${exception.message}")
            _message.value = "Gagal memperbarui data ke Firestore"
            _isLoading.value = false
        }
    }
}