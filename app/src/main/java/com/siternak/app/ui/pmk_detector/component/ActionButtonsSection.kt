package com.siternak.app.ui.pmk_detector.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siternak.app.core.theme.TernakAppRed

@Composable
fun ActionButtonsSection(
    modifier: Modifier = Modifier,
    onScanClick: () -> Unit,
    onRiwayatClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActionButton(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.DocumentScanner,
            label = "Scan",
            onClick = onScanClick
        )
        ActionButton(
            modifier = Modifier.weight(1f),
            icon = Icons.Filled.History,
            label = "Riwayat",
            onClick = onRiwayatClick
        )
    }
}

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector, label: String, onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = TernakAppRed), // Red color
        modifier = Modifier
            .height(150.dp)
            .then(modifier) // Ensure buttons take equal space
            .padding(horizontal = 8.dp) // Add some spacing between buttons
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, color = Color.White, fontSize = 16.sp)
        }
    }
}
