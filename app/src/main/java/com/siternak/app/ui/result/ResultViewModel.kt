package com.siternak.app.ui.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.siternak.app.ui.scan.DetectionResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ResultViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<DetectionResult?>(null)
    val uiState = _uiState.asStateFlow()

    // Buat fungsi publik agar Fragment bisa memberikan data
    fun setResult(result: DetectionResult) {
        _uiState.update { result }
    }
}