package com.siternak.app.ui.scan

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siternak.app.data.ml.PMKClassifier
import kotlinx.coroutines.async
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
    private val gusiClassifier: PMKClassifier,
    private val lidahClassifier: PMKClassifier,
    private val airLiurClassifier: PMKClassifier,
    private val kakiClassifier: PMKClassifier
) : ViewModel() {

    private val _state = MutableStateFlow(ScanState())
    val state: StateFlow<ScanState> = _state.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    fun onSetImageFile(currentStep: ScanStep, file: File?) {
        _state.update { currentState ->
            when (currentStep) {
                ScanStep.MOUTH -> currentState.copy(mouthFile = file)
                ScanStep.TONGUE -> currentState.copy(tongueFile = file)
                ScanStep.SALIVA -> currentState.copy(gumFile = file)
                ScanStep.FOOT -> currentState.copy(footFile = file)
            }
        }
    }

    /**
     * Fungsi ini sekarang memproses gambar, mendapatkan hasil Int, dan memicu navigasi ke Kuesioner
     */
    fun processScansAndNavigate() = viewModelScope.launch {
        val currentState = _state.value
        if (currentState.mouthFile == null || currentState.footFile == null) {
            _errorMessage.emit("Foto Mulut dan Kaki wajib diisi.")
            return@launch
        }
        _state.update { it.copy(isLoading = true) }

        // Helper untuk menjalankan model dan mendapatkan hasil Int
        suspend fun getClassificationResult(file: File?, step: ScanStep): Int? {
            if (file == null) return null

            val bitmap = BitmapFactory.decodeFile(file.path)

            // Pilih classifier yang benar berdasarkan langkah/step
            val (className, _) = when (step) {
                // Ingat: UI "Mulut" adalah Gusi, UI "Air Liur" adalah Air Liur
                ScanStep.MOUTH -> gusiClassifier.classifyImage(bitmap)
                ScanStep.TONGUE -> lidahClassifier.classifyImage(bitmap)
                ScanStep.SALIVA -> airLiurClassifier.classifyImage(bitmap)
                ScanStep.FOOT -> kakiClassifier.classifyImage(bitmap)
            }

            // Konversi nama kelas (e.g., "1_ringan") menjadi Int
            return className.substringBefore('_').toIntOrNull()
        }

        val mouthResultDef = async { getClassificationResult(currentState.mouthFile, ScanStep.MOUTH)!! }
        val tongueResultDef = async { getClassificationResult(currentState.tongueFile, ScanStep.TONGUE) }
        val gumResultDef = async { getClassificationResult(currentState.gumFile, ScanStep.SALIVA) }
        val footResultDef = async { getClassificationResult(currentState.footFile, ScanStep.FOOT)!! }

        val scanResult = PartScanResult(
            mouthResult = mouthResultDef.await(),
            tongueResult = tongueResultDef.await(),
            gumResult = gumResultDef.await(),
            footResult = footResultDef.await()
        )

        _state.update {
            it.copy(
                isLoading = false,
                scanResult = scanResult,
                navigateToQuestionnaire = true // Memicu navigasi
            )
        }
    }

    fun onNavigationHandled() {
        _state.update { it.copy(navigateToQuestionnaire = false, scanResult = null) }
    }
}