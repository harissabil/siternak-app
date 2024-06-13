package com.example.ternakapp.data.response

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import retrofit2.http.Field
import java.util.Date

// response for addPost
data class PostResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: PostData
)

data class PostData(
    @SerializedName("postId")
    val postId: String,

    @SerializedName("jenisTernak")
    val jenisTernak: String,

    @SerializedName("jenisAksi")
    val jenisAksi: String,

    @SerializedName("keterangan")
    val keterangan: String,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String,

    @SerializedName("petugas")
    val petugas: String,

    @SerializedName("status")
    val status: String,
)

// input for addPost
data class PostDataClass(
    @SerializedName("jenisTernak")
    val jenisTernak: String,

    @SerializedName("jenisAksi")
    val jenisAksi: String,

    @SerializedName("keterangan")
    val keterangan: String,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double
)

// input for updatePost
data class UpdatePostDataClass(
    @SerializedName("jenisTernak")
    val jenisTernak: String,

    @SerializedName("jenisAksi")
    val jenisAksi: String,

    @SerializedName("keterangan")
    val keterangan: String
)

// respons for get all posts by user id
data class ListPostItem(
    @SerializedName("message")
    val message: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("data")
    val data: List<PostItem>
)

data class PostItem(
//    @SerializedName("jenisTernak")
//    val jenisTernak: String,

    @SerializedName("jenisAksi")
    val jenisAksi: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("postId")
    val postId: String
)

// response for get all posts with location
data class ListPostLoc(
    @SerializedName("message")
    val message: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("data")
    val data: List<PostLoc>
)

data class PostLoc(
    @SerializedName("jenisTernak")
    val jenisTernak: String,

    @SerializedName("jenisAksi")
    val jenisAksi: String,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("latitude")
    val latitude: Double,
)


// response for get all posts
data class ListAllPosts(
    @SerializedName("message")
    val message: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("data")
    val data: List<Post>
)

@Parcelize
data class Post(
    @SerializedName("postId")
    val postId: String,

    @SerializedName("jenisTernak")
    val jenisTernak: String,

    @SerializedName("jenisAksi")
    val jenisAksi: String,

    @SerializedName("keterangan")
    val keterangan: String,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("createdAt")
    val createdAt: Date,

    @SerializedName("updatedAt")
    val updatedAt: Date,

    @SerializedName("petugas")
    val petugas: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("userId")
    val userId: String
) : Parcelable

data class DeleteResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("status")
    val status: String,
)