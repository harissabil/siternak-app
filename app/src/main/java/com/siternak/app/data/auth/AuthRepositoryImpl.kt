package com.siternak.app.data.auth

import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.siternak.app.data.auth.response.UserResponse
import com.siternak.app.domain.model.User
import com.siternak.app.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class AuthRepositoryImpl(
    private val credentialManager: CredentialManager,
    private val auth: FirebaseAuth,
) : AuthRepository {
    override suspend fun registerWithEmail(email: String, password: String): Result<User> =
        try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
            Result.success<User>(
                value = user!!.run {
                    UserResponse(
                        uid = uid,
                        userName = displayName,
                        email = email,
                        profilePictureUrl = photoUrl?.toString(),
                    ).toUser()
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            Result.failure<User>(e)
        }

    override suspend fun signInWithEmail(email: String, password: String): Result<User> =
        try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            Result.success<User>(
                value = user!!.run {
                    UserResponse(
                        uid = uid,
                        userName = displayName,
                        email = email,
                        profilePictureUrl = photoUrl?.toString(),
                    ).toUser()
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            Result.failure<User>(e)
        }

    override suspend fun signInWithGoogle(token: String): Result<User> {

        val googleCredentials = GoogleAuthProvider.getCredential(token, null)

        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            Result.success<User>(
                value = user!!.run {
                    UserResponse(
                        uid = uid,
                        userName = displayName,
                        email = email,
                        profilePictureUrl = photoUrl?.toString(),
                    ).toUser()
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            Result.failure<User>(e)
        }
    }

    override suspend fun signOut(): Result<Boolean> {
        return try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            auth.signOut()
            Result.success(value = true)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            Result.failure<Boolean>(e)
        }
    }

    override fun getUser(): Result<UserResponse> {
        val user = auth.currentUser
        return if (user != null) {
            Result.success(
                value = UserResponse(
                    uid = user.uid,
                    userName = user.displayName,
                    email = user.email,
                    profilePictureUrl = user.photoUrl.toString(),
                )
            )
        } else {
            Result.failure<UserResponse>(
                Exception("User not found")
            )
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}