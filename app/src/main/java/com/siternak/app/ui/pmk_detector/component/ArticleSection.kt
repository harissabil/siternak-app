package com.siternak.app.ui.pmk_detector.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siternak.app.core.theme.TernakAppRed

@Composable
fun ArticleSection() {
    Column {
        Text(
            text = "Artikel",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp) // Adjust height as needed
                .clip(RoundedCornerShape(12.dp))
                .background(TernakAppRed), // Red color
            contentAlignment = Alignment.Center
        ) {
            // Placeholder for article content.
            // You can replace this with an Image or a more complex Composable.
            // Example using a placeholder text:
            Text(
                text = "Article Placeholder",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 16.sp
            )
            // If you have a placeholder image:
            // Image(
            //     painter = painterResource(id = R.drawable.your_placeholder_image),
            //     contentDescription = "Article Image",
            //     contentScale = ContentScale.Crop,
            //     modifier = Modifier.fillMaxSize()
            // )
        }
    }
}