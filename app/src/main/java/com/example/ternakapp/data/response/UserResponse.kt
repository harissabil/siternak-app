package com.example.ternakapp.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// insert response for user profile
data class ApiResponse(
    val status: String,
    val message: String,
    val data: UserResponse
)

@Parcelize
data class UserResponse(
    val userId: String,
    val nama: String,
    val noTelp: String,
    val password: String,
    val provinsi: String,
    val kota: String,
    val kecamatan: String,
    val alamat: String,
) : Parcelable

data class RegisterDataClass(
    @field:SerializedName("no_telp")
    val noTelp: String,
    val password: String,
    val nama: String,
    val provinsi: String,
    val kota: String,
    val kecamatan: String,
    val alamat: String
)

data class LoginDataClass(
    @field:SerializedName("no_telp")
    val noTelp: String,
    val password: String
)