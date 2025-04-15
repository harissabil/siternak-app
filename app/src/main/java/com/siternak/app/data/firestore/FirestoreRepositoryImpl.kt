package com.siternak.app.data.firestore

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.siternak.app.data.firestore.request.toPostRequest
import com.siternak.app.data.firestore.request.toUserDataRequest
import com.siternak.app.data.firestore.response.PostResponse
import com.siternak.app.data.firestore.response.UserDataResponse
import com.siternak.app.domain.model.Post
import com.siternak.app.domain.model.UserData
import com.siternak.app.domain.repository.FirestoreRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreRepositoryImpl(
    private val auth: FirebaseAuth,
    private val userDataRef: CollectionReference,
    private val postRef: CollectionReference,
) : FirestoreRepository {
    override suspend fun addUserData(
        userData: UserData,
    ): Result<Boolean> =
        try {
            val userDataRequest = userData.toUserDataRequest()
            userDataRef.add(userDataRequest.copy(uid = auth.currentUser?.uid)).await()
            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }

    override suspend fun getUserData(): Result<UserData?> =
        try {
            val snapshot = userDataRef.whereEqualTo("uid", auth.currentUser?.uid).get().await()
            val userDataResponse = snapshot.toObjects(UserDataResponse::class.java).firstOrNull()
            if (userDataResponse != null) {
                Log.d("FirestoreRepositoryImpl", "getUserData: ${userDataResponse.toUserData()}")
                Result.success(userDataResponse.toUserData())
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }

    override suspend fun addPost(
        post: Post,
    ): Result<Boolean> =
        try {
            val postRequest = post.toPostRequest()
            postRef.add(
                postRequest.toPostResponse().copy(
                    uid = auth.currentUser?.uid.toString(),
                    createdAt = Timestamp.now(),
                )
            ).await()
            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }

    override fun getMyPosts(): Flow<Result<List<Post>>> = callbackFlow {
        val snapshotListener = postRef
            .whereEqualTo("uid", auth.currentUser?.uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    trySend(Result.failure(e))
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val posts = snapshot.toObjects(PostResponse::class.java).map { it.toPost() }
                    trySend(Result.success(posts))
                } else {
                    trySend(Result.success(emptyList()))
                }
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getAllPosts(): Flow<Result<List<Post>>> = callbackFlow {
        val snapshotListener = postRef
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    trySend(Result.failure(e))
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val posts = snapshot.toObjects(PostResponse::class.java).map { it.toPost() }
                    trySend(Result.success(posts))
                } else {
                    trySend(Result.success(emptyList()))
                }
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun getPostById(id: String): Result<Post> =
        try {
            val snapshot = postRef.document(id).get().await()
            val postResponse = snapshot.toObject(PostResponse::class.java)
            if (postResponse != null) {
                Result.success(postResponse.toPost())
            } else {
                Result.failure(Exception("Post not found"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }

    override suspend fun updatePost(post: Post): Result<Boolean> =
        try {
            val postRequest = post.toPostRequest()
            postRef.document(postRequest.id!!).set(
                postRequest.toPostResponse().copy(
                    updatedAt = Timestamp.now()
                )
            ).await()
            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }

    override suspend fun deletePost(id: String): Result<Boolean> =
        try {
            postRef.document(id).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
}