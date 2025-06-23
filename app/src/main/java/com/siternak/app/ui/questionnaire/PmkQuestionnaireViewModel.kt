package com.siternak.app.ui.questionnaire

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siternak.app.domain.model.Question
import com.siternak.app.ui.scan.DetectionResult
import com.siternak.app.ui.scan.PartClassification
import com.siternak.app.ui.scan.PartScanResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PmkQuestionnaireViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(QuestionnaireUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadNewQuestions()
    }

    fun setInitialScanResult(scanResult: PartScanResult) {
        _uiState.update { it.copy(scanResult = scanResult) }
    }

    private fun loadNewQuestions() {
        val questions = listOf(
            Question(1, "Bagaimana kondisi suhu tubuh sapi Anda?", listOf("Normal", "Demam Ringan", "Demam Tinggi")),
            Question(2, "Bagaimana nafsu makan sapi Anda?", listOf("Normal", "Berkurang", "Tidak Mau Makan")),
            Question(3, "Apakah ada kasus PMK di sekitar 5KM?", listOf("Tidak Ada", "Ada Laporan", "Sudah Positif")),
            Question(4, "Bagaimana lalu lintas orang/hewan ke kandang?", listOf("Tidak Ada", "Sedikit", "Banyak")),
            Question(5, "Apakah sumber air berasal dari sungai?", listOf("Tidak", "Ya"))
        )
        _uiState.update { it.copy(questions = questions) }
    }

    fun onEvent(event: QuestionnaireEvent) {
        when (event) {
            is QuestionnaireEvent.AnswerSelected -> {
                _uiState.update { currentState ->
                    val newAnswers = currentState.answers.toMutableMap()
                    // Simpan index dari jawaban sebagai nilai Int (0, 1, 2, dst.)
                    val answerIndex = currentState.questions[currentState.currentQuestionIndex]
                        .options.indexOf(event.answer)
                    newAnswers[event.questionId] = answerIndex
                    currentState.copy(answers = newAnswers)
                }
            }
            QuestionnaireEvent.NextClicked -> {
                val currentState = _uiState.value
                if (currentState.currentQuestionIndex < currentState.questions.size - 1) {
                    _uiState.update { it.copy(currentQuestionIndex = it.currentQuestionIndex + 1) }
                } else {
                    // Pertanyaan terakhir, kalkulasi hasil akhir
                    calculateFinalResultAndNavigate()
                }
            }
            QuestionnaireEvent.PreviousClicked -> {
                if (_uiState.value.currentQuestionIndex > 0) {
                    _uiState.update { it.copy(currentQuestionIndex = it.currentQuestionIndex - 1) }
                }
            }
        }
    }

    private fun calculateFinalResultAndNavigate() = viewModelScope.launch {
        val currentState = _uiState.value
        val scanRes = currentState.scanResult ?: return@launch

        // 1. Kalkulasi Skor dari Hasil Scan (Bobot 60%)
        val scanScores = mutableMapOf<String, Int>()
        scanScores["Mulut"] = mapSeverityToScore(scanRes.mouthResult)
        scanScores["Lidah"] = mapSeverityToScore(scanRes.tongueResult)
        scanScores["Gusi"] = mapSeverityToScore(scanRes.gumResult)
        scanScores["Kaki"] = mapSeverityToScore(scanRes.footResult)
        val averageScanScore = scanScores.values.average()

        // 2. Kalkulasi Skor dari Jawaban Kuesioner (Bobot 40%)
        val questionAnswers = currentState.answers.values.toList() // isinya [0,1,2,...]
        // Normalisasi skor kuesioner ke skala 0-100
        // Max score = 2+2+2+2+1 = 9
        val totalQuestionScore = questionAnswers.sum()
        val normalizedQuestionScore = (1 - (totalQuestionScore.toDouble() / 9.0)) * 100

        // 3. Kombinasi Skor (contoh: 60% dari scan, 40% dari kuesioner)
        val finalPercentage = ((averageScanScore * 0.6) + (normalizedQuestionScore * 0.4)).toInt()

        // 4. Buat objek `DetectionResult` untuk dikirim ke halaman hasil
        val classifications = scanScores.map { (partName, _) ->
            val resultInt = when (partName) {
                "Mulut" -> scanRes.mouthResult
                "Lidah" -> scanRes.tongueResult
                "Gusi" -> scanRes.gumResult
                "Kaki" -> scanRes.footResult
                else -> -1
            }
            // Ubah Int kembali ke format label String untuk ditampilkan
            PartClassification(partName, mapIntToLabel(resultInt), 1.0f)
        }

        val finalResult = DetectionResult(finalPercentage, classifications)

        _uiState.update { it.copy(finalResult = finalResult, navigateToResult = true) }
    }

    // Helper untuk kalkulasi skor
    private fun mapSeverityToScore(severity: Int?): Int {
        return when (severity) {
            0 -> 100 // Sehat
            1 -> 75  // Ringan
            2 -> 50  // Sedang
            3 -> 25  // Berat
            else -> 100 // Jika null (opsional dilewati), anggap sehat
        }
    }

    private fun mapIntToLabel(severity: Int?): String {
        return when (severity) {
            0 -> "0_sehat"
            1 -> "1_ringan"
            2 -> "2_sedang"
            3 -> "3_berat"
            else -> "N/A" // Not Applicable
        }
    }

    // Panggil ini setelah navigasi selesai untuk mereset state
    fun onResultNavigationHandled() {
        _uiState.update { it.copy(navigateToResult = false, finalResult = null) }
    }
}