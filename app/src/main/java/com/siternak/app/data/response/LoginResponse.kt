package com.siternak.app.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("data")
    val loginResult: loginResultResponse,
)

data class loginResultResponse(
    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("token")
    val token: String,
)

data class LoginDataClass(
    @field:SerializedName("noTelp")
    val noTelp: String,

    @field:SerializedName("password")
    val password: String,
)
