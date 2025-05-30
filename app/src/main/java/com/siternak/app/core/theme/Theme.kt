package com.siternak.app.core.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val LightColorScheme = lightColorScheme(
    primary = TernakAppRed,
    onPrimary = TernakAppWhite,
    primaryContainer = TernakAppRedVariantLight,
    onPrimaryContainer = TernakAppRed, // Color for text/icons on primaryContainer

    secondary = TernakAppRed, // As per XML, secondary is the same as primary
    onSecondary = TernakAppBlack,
    secondaryContainer = TernakAppRedVariantLight, // Using similar logic as primary for container
    onSecondaryContainer = TernakAppRed,   // Color for text/icons on secondaryContainer

    tertiary = TernakAppRed, // You can define a specific tertiary or reuse
    onTertiary = TernakAppWhite,
    tertiaryContainer = TernakAppRedVariantLight,
    onTertiaryContainer = TernakAppRed,

    background = Color(0xFFFCFCFC), // A typical light background
    onBackground = Color(0xFF1A1C1E),

    surface = Color(0xFFFCFCFC), // A typical light surface
    onSurface = Color(0xFF1A1C1E),

    surfaceVariant = TernakAppSurfaceVariantLight, // From colorSecondaryVariant
    onSurfaceVariant = TernakAppBlack, // Text/icons on surfaceVariant

    error = Color(0xFFB00020), // Standard Material error color
    onError = Color.White,

    outline = Color(0xFF73777F),
    outlineVariant = Color(0xFFC3C7CF),
    scrim = Color(0xFF000000)
)

private val DarkColorScheme = darkColorScheme(
    primary = TernakAppDarkPrimary,
    onPrimary = TernakAppDarkOnPrimary,
    primaryContainer = TernakAppDarkPrimaryContainer,
    onPrimaryContainer = TernakAppWhite, // Text on dark primary container

    secondary = TernakAppDarkSecondary,
    onSecondary = TernakAppDarkOnSecondary,
    secondaryContainer = TernakAppDarkPrimaryContainer, // Similar logic for dark
    onSecondaryContainer = TernakAppWhite,

    tertiary = TernakAppDarkPrimary, // You can define a specific tertiary or reuse
    onTertiary = TernakAppDarkOnPrimary,
    tertiaryContainer = TernakAppDarkPrimaryContainer,
    onTertiaryContainer = TernakAppWhite,

    background = Color(0xFF1A1C1E), // A typical dark background
    onBackground = Color(0xFFE2E2E6),

    surface = Color(0xFF1A1C1E), // A typical dark surface
    onSurface = Color(0xFFE2E2E6),

    surfaceVariant = TernakAppDarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFC3C7CF), // Text/icons on dark surfaceVariant

    error = Color(0xFFCF6679), // Standard Material dark error color
    onError = Color(0xFF141213),

    outline = Color(0xFF8D9199),
    outlineVariant = Color(0xFF43474E),
    scrim = Color(0xFF000000)
)

@Composable
fun SiTernakTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}