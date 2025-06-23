package com.siternak.app.ui.result

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siternak.app.ui.scan.DetectionResult
import com.siternak.app.ui.scan.PartClassification

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(result: DetectionResult?, onSave: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hasil") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF0F2F5))
            )
        },
        containerColor = Color(0xFFF0F2F5)
    ) { paddingValues ->
        if (result == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Data hasil tidak ditemukan.")
            }
            return@Scaffold
        }
        
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Tingkat Keparahan PMK",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            item {
                CircularPercentageIndicator(
                    percentage = result.overallPercentage,
                    size = 200.dp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Hewan ternak anda memiliki presentase harapan hidup ${result.overallPercentage}%",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
            
            items(result.classifications) { classification ->
                ResultItem(classification = classification)
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
                ) {
                    Text("Simpan", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ResultItem(classification: PartClassification) {
    val (icon, color) = when (classification.result.lowercase()) {
        "0_sehat" -> Icons.Default.CheckCircle to Color(0xFF16A34A) // Green
        "1_ringan" -> Icons.Default.Warning to Color(0xFFF59E0B)    // Amber
        "3_berat" -> Icons.Default.Error to Color(0xFFDC2626)      // Red
        else -> Icons.Default.Error to Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = classification.result,
                tint = color,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = classification.partName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                Text(
                    text = classification.result,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}

@Composable
fun CircularPercentageIndicator(
    percentage: Int,
    size: Dp = 150.dp,
    strokeWidth: Dp = 16.dp
) {
    val color = when {
        percentage >= 70 -> Color(0xFF16A34A) // Green
        percentage >= 40 -> Color(0xFFF59E0B) // Amber
        else -> Color(0xFFDC2626) // Red
    }

    var animationPlayed by remember { mutableStateOf(false) }
    val currentPercentage by animateFloatAsState(
        targetValue = if (animationPlayed) percentage / 100f else 0f,
        animationSpec = tween(durationMillis = 1500),
        label = "percentageAnimation"
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Background track
            drawArc(
                color = Color.LightGray.copy(alpha = 0.5f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            // Foreground progress
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360 * currentPercentage,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = "$percentage",
            fontSize = 60.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}