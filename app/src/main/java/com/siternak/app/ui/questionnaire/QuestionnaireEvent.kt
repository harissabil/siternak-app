package com.siternak.app.ui.questionnaire

sealed class QuestionnaireEvent {
    data class AnswerSelected(val questionId: Int, val answer: String) : QuestionnaireEvent()
    object NextClicked : QuestionnaireEvent()
    object PreviousClicked : QuestionnaireEvent()
}