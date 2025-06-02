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
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siternak.app.core.theme.TernakAppRed

@Composable
fun EmergencyContactsSection(
    modifier: Modifier = Modifier,
    onHotlineClick: () -> Unit,
    onRSHewanClick: () -> Unit
) {
    Column {
        Text(
            text = "Kontak Darurat",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            EmergencyContactButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.Whatsapp, // Or a custom WhatsApp icon
                phoneNumber = "0811-1182-7889",
                description = "Hotline Kesehatan Hewan Ternak",
                onClick = onHotlineClick
            )
            EmergencyContactButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.LocalHospital,
                phoneNumber = "0821-2546-7104",
                description = "RS Hewan Pendidikan SKHB IPB",
                onClick = onRSHewanClick
            )
        }
    }
}

@Composable
fun EmergencyContactButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    phoneNumber: String,
    description: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = TernakAppRed), // Red color
        modifier = Modifier
            .height(120.dp)
            .then(modifier) // Ensure buttons take equal space
            .padding(horizontal = 8.dp) // Add some spacing between buttons
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = description,
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = phoneNumber,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                color = Color.White,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp
            )
        }
    }
}