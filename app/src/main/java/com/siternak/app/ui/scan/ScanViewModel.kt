package com.siternak.app.ui.scan

import android.R.attr.bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siternak.app.data.ml.PMKLidahClassifier
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

    /**
     * Fungsi ini akan menjalankan klasifikasi untuk semua gambar yang telah diambil.
     */
    fun runFullClassification() = viewModelScope.launch {
        val currentState = _state.value
        val filesToClassify = mapOf(
            "Mulut" to currentState.mouthFile,
            "Lidah" to currentState.tongueFile,
            "Air Liur" to currentState.salivaFile,
            "Kaki" to currentState.footFile
        ).filterValues { it != null }

        if (filesToClassify.size < 4) {
            _errorMessage.emit("Harap lengkapi semua foto sebelum melanjutkan.")
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        // Proses klasifikasi secara paralel
        val classificationJobs = filesToClassify.map { (partName, file) ->
            async {
                // NOTE: Ini hanya contoh. Anda harus menggunakan model yang sesuai untuk setiap bagian.
                // Saat ini kita hanya menggunakan pmkLidahClassifier untuk semua sebagai placeholder.
                // Ganti dengan classifier yang benar.
                val bitmap = BitmapFactory.decodeFile(file!!.path)
                val (className, confidence) = when (partName) {
                    "Lidah" -> pmkLidahClassifier.classifyImage(bitmap)
                    // Ganti dengan pemanggilan model yang sesuai
                    "Mulut" -> Pair("0_sehat", 0.9f)
                    "Air Liur" -> Pair("2_sedang", 0.75f)
                    "Kaki" -> Pair("3_berat", 0.88f)
                    else -> Pair("Unknown", 0f)
                }
                Log.d("ScanVM", "Hasil $partName: $className (${confidence * 100}%)")
                PartClassification(partName, className, confidence)
            }
        }

        val results = classificationJobs.awaitAll()

        // Kalkulasi skor "Harapan Hidup" (Contoh Logika)
        // Normal = 100, Sedang = 60, Tinggi = 20
        val totalScore = results.map { classification ->
            when (classification.result.lowercase()) {
                "0_sehat" -> 100
                "1_ringan" -> 75
                "2_sedang" -> 50
                "3_berat" -> 25
                else -> 0
            }
        }.sum()
        val overallPercentage = if (results.isNotEmpty()) totalScore / results.size else 0

        val finalResult = DetectionResult(overallPercentage, results)

        _state.update {
            it.copy(
                isLoading = false,
                detectionResult = finalResult,
                navigateToResult = true // Siap untuk navigasi
            )
        }
    }

    // Fungsi untuk mereset trigger navigasi setelah navigasi dilakukan
    fun onResultNavigationHandled() {
        _state.update { it.copy(navigateToResult = false, detectionResult = null) }
    }
}