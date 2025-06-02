package com.siternak.app.ui.pmk_detector.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun GreetingSection(name: String) {
    Column {
        Text(
            text = "Hai $name!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "Yuk cek kondisi ternak-mu!",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}