package com.siternak.app.ui.questionnaire

import com.siternak.app.domain.model.Question
import com.siternak.app.ui.scan.DetectionResult
import com.siternak.app.ui.scan.PartScanResult
import kotlin.collections.getOrNull

data class QuestionnaireUiState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val answers: Map<Int, Int> = emptyMap(), // Jawaban sekarang Int (0,1,2)
    val scanResult: PartScanResult? = null,
    val finalResult: DetectionResult? = null,
    val navigateToResult: Boolean = false
) {
    val currentQuestion: Question?
        get() = questions.getOrNull(currentQuestionIndex)

    val progress: Float
        get() = if (questions.isEmpty()) 0f else (currentQuestionIndex + 1) / questions.size.toFloat()
}