package com.example.ternakapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ternakapp.data.UserRepository
import com.example.ternakapp.ui.login.LoginViewModel

class ViewModelFactory private constructor(
    // user dan post repository belum dibuat
    private val userRepository: UserRepository,
    // private val postRepository: PostRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}