package com.siternak.app.ui.questionnaire

import androidx.lifecycle.ViewModel
import com.siternak.app.domain.model.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PmkQuestionnaireViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(QuestionnaireUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        val questions = listOf(
            Question(1, "Apakah sapi Anda mengeluarkan air liur berlebih?", listOf("Tidak Ada", "Sedikit", "Banyak")),
            Question(2, "Apakah sapi Anda mengalami lepuh pada mulut dan kaki?", listOf("Tidak Ada", "Ada")),
            Question(3, "Apakah sapi Anda kehilangan nafsu makan?", listOf("Masih mau makan", "Nafsu makan berkurang", "Tidak mau makan")),
            Question(4, "Bagaimana keadaan suhu tubuh sapi Anda?", listOf("Normal (37-39°C)", "Demam Ringan (39-40°C)", "Demam (>40°C)")),
            Question(5, "Adakah kasus PMK disekitar Anda, pada radius 5 kilometer?", listOf("Tidak Berlanjut", "Kasus PMK")),
            Question(6, "Apakah orang/pekerja bebas keluar masuk kandang?", listOf("Tidak ada", "Ada")),
            Question(7, "Apakah sumber mata air yang dipergunakan untuk peternakan berasal dari sungai?", listOf("Tidak menggunakan air sungai", "Menggunakan air dari sungai"))
        )
        _uiState.update { it.copy(questions = questions) }
    }

    fun onEvent(event: QuestionnaireEvent) {
        when (event) {
            is QuestionnaireEvent.AnswerSelected -> {
                _uiState.update { currentState ->
                    val newAnswers = currentState.answers.toMutableMap()
                    newAnswers[event.questionId] = event.answer
                    currentState.copy(answers = newAnswers)
                }
            }
            QuestionnaireEvent.NextClicked -> {
                val currentState = _uiState.value
                if (currentState.currentQuestionIndex < currentState.questions.size - 1) {
                    _uiState.update { it.copy(currentQuestionIndex = it.currentQuestionIndex + 1) }
                } else {
                    // Pertanyaan terakhir, trigger navigasi
                    // Di sini Anda bisa mengirimkan jawaban `currentState.answers` ke repository/use case
                    _uiState.update { it.copy(navigateToScan = true) }
                }
            }
            QuestionnaireEvent.PreviousClicked -> {
                if (_uiState.value.currentQuestionIndex > 0) {
                    _uiState.update { it.copy(currentQuestionIndex = it.currentQuestionIndex - 1) }
                }
            }
        }
    }

    // Panggil ini setelah navigasi selesai untuk mereset state
    fun onNavigationHandled() {
        _uiState.update { it.copy(navigateToScan = false) }
    }
}