package com.example.ternakapp.data.response

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostResponse(
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    val postId: String,
    val jenisTernak: String,
    val jenisAksi: String,
    val keterangan: String,
    val longitude: Double? = null,
    val latitude: Double? = null,
    val createdAt: String,
    val updatedAt: String,
    val petugas: String,
    val status: String,
) : Parcelable

data class DetailResponse(
    val error: Boolean,
    val message: String,
)
