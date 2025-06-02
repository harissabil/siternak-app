package com.siternak.app.ui.scan

import android.R.attr.bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siternak.app.data.ml.PMKLidahClassifier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class ScanViewModel(
    private val pmkLidahClassifier: PMKLidahClassifier
) : ViewModel() {

    private val _state = MutableStateFlow(ScanState())
    val state: StateFlow<ScanState> = _state.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    fun classifyTongue() = viewModelScope.launch {
        if (_state.value.tongueFile == null) {
            _errorMessage.emit("Gambar lidah belum dipilih")
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        pmkLidahClassifier.loadModel("pmk_lidah_simple.tflite")

        val bitmap = BitmapFactory.decodeFile(_state.value.tongueFile!!.path)

        val result = pmkLidahClassifier.classifyImage(bitmap)
        val (className, confidence) = result
        Log.d("ScanViewModel", "Hasil: $className dengan confidence ${confidence * 100}%")
        _errorMessage.emit("Hasil Lidah: $className dengan confidence ${confidence * 100}%")

        _state.update { it.copy(isLoading = false,) }
    }

    fun onSetImageFile(currentStep: ScanStep, file: File?) {
        _state.update { currentState ->
            when (currentStep) {
                ScanStep.MOUTH -> currentState.copy(mouthFile = file)
                ScanStep.TONGUE -> currentState.copy(tongueFile = file)
                ScanStep.SALIVA -> currentState.copy(salivaFile = file)
                ScanStep.FOOT -> currentState.copy(footFile = file)
            }
        }
    }
}