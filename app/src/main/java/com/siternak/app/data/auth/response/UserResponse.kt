package com.siternak.app.data.auth.response

import com.siternak.app.domain.model.User

data class UserResponse(
    val uid: String,
    val userName: String?,
    val email: String?,
    val profilePictureUrl: String?,
) {
    fun toUser(): User {
        return User(
            uid = uid,
            userName = userName,
            email = email,
            profilePictureUrl = profilePictureUrl
        )
    }
}
