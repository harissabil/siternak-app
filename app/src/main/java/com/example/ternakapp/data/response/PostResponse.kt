package com.example.ternakapp.data.response

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostResponse(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("user_id") val userId: String?,
    @SerializedName("post_id") val postId: String,
    @SerializedName("jenis_ternak") val jenisTernak: String,
    @SerializedName("jenis_aksi") val jenisAksi: String,
    val keterangan: String,
    val longitude: Double? = null,
    val latitude: Double? = null,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    val petugas: String,
    val status: String,
) : Parcelable

data class ApiPostResponse(
    val status: String,
    val message: String,
    val dataPost: PostData
)

data class PostData(
    val post: PostResponse
)
