package com.siternak.app.data.response

// insert response for user profile
data class UserResponse(
    val status: String,
    val message: String,
    val data: UserDataClass
)

data class UserDataClass(
    val userId: String,
    val noTelp: String,
    val nama: String,
    val password: String,
    val provinsi: String,
    val kota: String,
    val kecamatan: String,
    val alamat: String,
)