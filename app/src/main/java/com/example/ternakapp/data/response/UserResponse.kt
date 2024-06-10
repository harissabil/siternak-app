package com.example.ternakapp.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// insert response for user profile
data class ApiResponse(
    val status: String,
    val message: String,
    val dataUser: UserResponse
)

@Parcelize
data class UserResponse(
    val userId: String,
    val nama: String,
    val noTelp: String,
    val password: String,
    val alamat: String,
    val kecamatan: String,
    val kota: String,
    val provinsi: String
) : Parcelable

