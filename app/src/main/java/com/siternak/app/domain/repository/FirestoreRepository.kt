package com.siternak.app.domain.repository

import com.siternak.app.domain.model.Post
import com.siternak.app.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    suspend fun addUserData(userData: UserData): Result<Boolean>

    suspend fun getUserData(): Result<UserData?>

    suspend fun addPost(post: Post): Result<Boolean>

    fun getMyPosts(): Flow<Result<List<Post>>>

    fun getAllPosts(): Flow<Result<List<Post>>>

    suspend fun getPostById(id: String): Result<Post>

    suspend fun updatePost(post: Post): Result<Boolean>

    suspend fun deletePost(id: String): Result<Boolean>
}