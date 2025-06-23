package com.siternak.app.domain.model

data class Question(
    val id: Int,
    val questionText: String,
    val options: List<String>
)