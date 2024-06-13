package com.example.ternakapp.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String,
)

data class RegisterDataClass(
    @field:SerializedName("noTelp")
    val noTelp: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("provinsi")
    val provinsi: String,

    @field:SerializedName("kota")
    val kota: String,

    @field:SerializedName("kecamatan")
    val kecamatan: String,

    @field:SerializedName("alamat")
    val alamat: String,
)