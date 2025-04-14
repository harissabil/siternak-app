package com.siternak.app.domain.repository

import com.siternak.app.data.auth.response.UserResponse
import com.siternak.app.domain.model.User

interface AuthRepository {
    suspend fun registerWithEmail(email: String, password: String): Result<User>

    suspend fun signInWithEmail(email: String, password: String): Result<User>

    suspend fun signInWithGoogle(token: String): Result<User>

    suspend fun signOut(): Result<Boolean>

    fun getUser(): Result<UserResponse>

    fun isUserLoggedIn(): Boolean
}