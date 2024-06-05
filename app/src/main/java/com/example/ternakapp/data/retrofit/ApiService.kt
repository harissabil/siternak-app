package com.example.ternakapp.data.retrofit

import com.example.ternakapp.data.response.DetailResponse
import com.example.ternakapp.data.response.PostResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    /*
    @POST("auth/login")
    suspend fun login(
        @Body body: LoginBody
    ): LoginResponse

    @POST("auth/register")
    suspend fun register(
        @Body body: RegisterBody
    ): RegisterResponse

     */

    @FormUrlEncoded
    @POST("post")
    fun addPost(
        @Field("jenisTernak") jenisTernak: String,
        @Field("jenisAksi") jenisAksi: String,
        @Field("keterangan") keterangan: String
    ): Call<ResponseBody>

    @GET("post")
    fun getAllPostsWithLoc(
        @Field("location") location: Int
    ): Call<PostResponse>

    @GET("post")
    fun getAllPosts(): Call<List<PostResponse>>

    @GET("post/{id}")
    fun getPostById(@Path("id") id: String): Call<PostResponse>

    @FormUrlEncoded
    @PUT("post/{id}")
    fun updatePost(
        @Path("id") id: String,
        @Field("jenisTernak") jenisTernak: String,
        @Field("jenisAksi") jenisAksi: String,
        @Field("keterangan") keterangan: String
    ): Call<ResponseBody>

    @DELETE("post/{id}")
    fun deletePost(@Path("id") id: String): Call<ResponseBody>
}
