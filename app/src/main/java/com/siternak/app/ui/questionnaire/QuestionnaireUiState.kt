package com.siternak.app.ui.questionnaire

import com.siternak.app.domain.model.Question
import kotlin.collections.getOrNull

data class QuestionnaireUiState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val answers: Map<Int, String> = emptyMap(),
    val navigateToScan: Boolean = false
) {
    val currentQuestion: Question?
        get() = questions.getOrNull(currentQuestionIndex)

    val progress: Float
        get() = if (questions.isEmpty()) 0f else (currentQuestionIndex + 1) / questions.size.toFloat()
}