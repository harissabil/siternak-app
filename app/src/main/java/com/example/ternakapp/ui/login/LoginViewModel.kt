package com.example.ternakapp.ui.login

import androidx.lifecycle.ViewModel
import com.example.ternakapp.data.UserRepository

class LoginViewModel(
    private val repository: UserRepository
) : ViewModel() {
    //fun postLogin(email: String, password: String) = repository.postLogin(email, password)
}