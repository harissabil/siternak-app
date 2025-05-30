package com.siternak.app.core.theme

import androidx.compose.ui.graphics.Color

// Colors from Theme.TernakApp.Light XML
val TernakAppRed = Color(0xFFCF0000)
val TernakAppRedVariantLight = Color(0xFFFFB4B4) // Mapped from colorPrimaryVariant
val TernakAppWhite = Color(0xFFFFFFFF)         // Mapped from colorOnPrimary
// val TernakAppSecondaryRed = Color(0xFFCF0000) // colorSecondary is the same as primary
val TernakAppSurfaceVariantLight = Color(0xFFF7F6F1) // Mapped from colorSecondaryVariant, good for surfaceVariant
val TernakAppBlack = Color(0xFF000000)          // Mapped from colorOnSecondary

// Define corresponding Dark Theme colors
// These are suggestions; adjust them to your dark theme design
val TernakAppDarkPrimary = Color(0xFFE57373) // Lighter red for dark theme primary
val TernakAppDarkPrimaryContainer = Color(0xFFB71C1C) // Darker container for primary
val TernakAppDarkOnPrimary = Color(0xFF000000) // Black text on lighter red

val TernakAppDarkSecondary = Color(0xFFE57373) // Lighter red for dark theme secondary
val TernakAppDarkOnSecondary = Color(0xFF000000) // Black text on lighter red
val TernakAppDarkSurfaceVariant = Color(0xFF424242) // Darker surface variant