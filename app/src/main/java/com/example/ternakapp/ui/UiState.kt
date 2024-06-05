package com.example.ternakapp.ui

// tampilan loading, success loading, dan error
sealed class UiState<out T : Any?> {
    object Loading : UiState<Nothing>()
    data class Success<out T : Any>(val data: T) : UiState<T>()
    data class Error(val errorMessage: String) : UiState<Nothing>()
}