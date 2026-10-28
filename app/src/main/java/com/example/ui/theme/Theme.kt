package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = MedicalCyan,
  onPrimary = MedicalNavyDark,
  primaryContainer = MedicalNavyLight,
  onPrimaryContainer = Color.White,
  secondary = MedicalEmerald,
  onSecondary = Color.White,
  secondaryContainer = MedicalEmeraldBg,
  onSecondaryContainer = MedicalEmerald,
  tertiary = MedicalAmber,
  onTertiary = Color.White,
  background = MedicalNavyDark,
  onBackground = Slate50,
  surface = MedicalNavy,
  onSurface = Slate50,
  surfaceVariant = MedicalNavyCard,
  onSurfaceVariant = Slate200,
  outline = MedicalNavyLight,
  error = MedicalCrimson,
  onError = Color.White
)

private val LightColorScheme = lightColorScheme(
  primary = Color(0xFF0284C7),
  onPrimary = Color.White,
  primaryContainer = Color(0xFFE0F2FE),
  onPrimaryContainer = Color(0xFF0369A1),
  secondary = MedicalEmerald,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFDCFCE7),
  onSecondaryContainer = MedicalEmeraldDark,
  tertiary = Color(0xFFD97706),
  onTertiary = Color.White,
  background = Slate50,
  onBackground = Slate900,
  surface = Color.White,
  onSurface = Slate900,
  surfaceVariant = Slate100,
  onSurfaceVariant = Slate600,
  outline = Slate200,
  error = MedicalCrimson,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Default to deep medical dark mode for optimal contrast
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
