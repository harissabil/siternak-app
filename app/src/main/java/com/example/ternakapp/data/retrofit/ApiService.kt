package com.example.ternakapp.data.retrofit

import com.example.ternakapp.data.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("api/v1/auth/login")
    fun loginUser(
        @Body loginResponse: LoginDataClass
    ): Call<LoginResponse>

    @POST("api/v1/auth/register")
    fun registerUser(
        @Body registerResponse: RegisterDataClass
    ): Call<RegisterResponse>

    @POST("api/v1/post")
    fun addPost(
        @Header("Authorization") token: String,
        @Body postResponse: PostDataClass
    ): Call<PostResponse>

    @GET("api/v1/post/myposts")
    fun getAllPostsByUserId(
        @Header("Authorization") token: String
    ): Call<ListPostItem>

    @GET("api/v1/post/")
    fun getAllPosts(
        @Header("Authorization") token: String
    ): Call<ListPostLoc>

    @GET("api/v1/post/{id}")
    fun getPostById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<PostResponse>

    @PUT("api/v1/post/{id}")
    fun updatePost(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body postResponse: UpdatePostDataClass,
    ): Call<PostResponse>

    @DELETE("api/v1/post/{id}")
    fun deletePost(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DeleteResponse>

    @GET("api/v1/users/profile")
    fun getUserProfile(
        @Header("Authorization") token: String
    ): Call<UserResponse>
}