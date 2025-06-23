package com.siternak.app.ui.questionnaire

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.siternak.app.domain.model.Question

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PmkQuestionnaireScreen(
    state: QuestionnaireUiState,
    onEvent: (QuestionnaireEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PMK Monitor") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF9F9F9))
            )
        },
        containerColor = Color(0xFFF9F9F9) // Warna latar
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Progress Bar
            LinearProgressIndicator(
                progress = state.progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color.LightGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Konten Pertanyaan
            state.currentQuestion?.let { question ->
                // PERBAIKAN: Konversi index (Int) yang tersimpan menjadi teks (String)
                val selectedAnswerIndex = state.answers[question.id]
                val selectedAnswerString = selectedAnswerIndex?.let {
                    question.options.getOrNull(it)
                }
                QuestionContent(
                    question = question,
                    selectedAnswer = selectedAnswerString, // Berikan string yang sudah dikonversi
                    onAnswerSelected = { answer ->
                        // Event tetap mengirim string
                        onEvent(QuestionnaireEvent.AnswerSelected(question.id, answer))
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Mendorong tombol ke bawah

            // Tombol Navigasi
            NavigationButtons(
                isFirstQuestion = state.currentQuestionIndex == 0,
                isLastQuestion = state.currentQuestionIndex == state.questions.size - 1,
                onPreviousClicked = { onEvent(QuestionnaireEvent.PreviousClicked) },
                onNextClicked = { onEvent(QuestionnaireEvent.NextClicked) }
            )
        }
    }
}

@Composable
private fun QuestionContent(
    question: Question,
    selectedAnswer: String?,
    onAnswerSelected: (String) -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Pertanyaan ${question.id}",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = question.questionText,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Opsi Jawaban
        question.options.forEach { option ->
            AnswerOption(
                text = option,
                isSelected = option == selectedAnswer,
                onClick = { onAnswerSelected(option) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun AnswerOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onClick
            ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}


@Composable
private fun NavigationButtons(
    isFirstQuestion: Boolean,
    isLastQuestion: Boolean,
    onPreviousClicked: () -> Unit,
    onNextClicked: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Tombol Kembali
        Button(
            onClick = onPreviousClicked,
            enabled = !isFirstQuestion,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.LightGray
            ),
            border = BorderStroke(
                1.dp,
                if (!isFirstQuestion) MaterialTheme.colorScheme.primary else Color.LightGray
            )
        ) {
            Text("Kembali")
        }

        // Tombol Lanjut / Selesai
        Button(
            onClick = onNextClicked,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(if (isLastQuestion) "Selesai & Lanjut Scan" else "Lanjut")
        }
    }
}