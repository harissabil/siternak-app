package com.siternak.app.domain.model

import com.google.firebase.Timestamp

data class Post(
    val id: String? = null,
    val uid: String? = null,
    val jenisTernak: String,
    val jumlahTernak: String,
    val jenisAksi: String,
    val keteranganAksi: String,
    val alamatAksi: String,
    val latitude: Double?,
    val longitude: Double?,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
    val petugas: String? = null,
    val status: String? = null,
)
