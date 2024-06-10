package com.example.ternakapp.data.retrofit

import com.example.ternakapp.data.response.ApiPostResponse
import com.example.ternakapp.data.response.ApiResponse
import com.example.ternakapp.data.response.PostResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("user/login")
    fun loginUser(
        @Field("noTelp") noTelp: String,
        @Field("password") password: String
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("user/register")
    fun registerUser(
        @Field("nama") nama: String,
        @Field("noTelp") noTelp: String,
        @Field("password") password: String,
        @Field("alamat") alamat: String,
        @Field("kecamatan") kecamatan: String,
        @Field("kota") kota: String,
        @Field("provinsi") provinsi: String
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("post")
    fun addPost(
        @Field("jenisTernak") jenisTernak: String,
        @Field("jenisAksi") jenisAksi: String,
        @Field("keterangan") keterangan: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double
    ): Call<PostResponse>

    @GET("post")
    fun getAllPostsWithLoc(
        @Field("location") location: Int
    ): Call<ApiPostResponse>

    @GET("post")
    fun getAllPosts(): Call<ApiPostResponse>

    @GET("post/{id}")
    fun getPostById(postId: String): Call<PostResponse>
    //fun getPostById(@Path("id") id: String): Call<PostResponse>

    @FormUrlEncoded
    @PUT("post/{id}")
    fun updatePost(
        @Path("id") id: String,
        @Field("jenisTernak") jenisTernak: String,
        @Field("jenisAksi") jenisAksi: String,
        @Field("keterangan") keterangan: String
    ): Call<PostResponse>

    @DELETE("post/{id}")
    fun deletePost(@Path("id") id: String): Call<ResponseBody>
}
