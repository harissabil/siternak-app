package com.siternak.app.domain.model

import com.google.firebase.Timestamp

data class Post(
    val postId: String,
    val uid: String,
    val jenisTernak: String,
    val jumlahTernak: String,
    val jenisAksi: String,
    val keteranganAksi: String,
    val alamatAksi: String,
    val latitude: Double?,
    val longitude: Double?,
    val createdAt: Timestamp?,
    val updatedAt: Timestamp?,
    val petugas: String?,
    val status: String?,
)
