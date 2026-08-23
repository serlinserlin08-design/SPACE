package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CosmicColorScheme = darkColorScheme(
    primary = StarlightCyan,
    onPrimary = SpaceBlack,
    primaryContainer = CosmicSurfaceElevated,
    onPrimaryContainer = StarlightCyan,
    secondary = NebulaViolet,
    onSecondary = Color.White,
    secondaryContainer = DeepViolet.copy(alpha = 0.3f),
    onSecondaryContainer = NebulaViolet,
    tertiary = SupernovaGold,
    onTertiary = SpaceBlack,
    tertiaryContainer = SolarAmber.copy(alpha = 0.2f),
    onTertiaryContainer = SupernovaGold,
    background = SpaceBlack,
    onBackground = TextPrimary,
    surface = CosmicSurface,
    onSurface = TextPrimary,
    surfaceVariant = CosmicSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = CosmicBorder,
    error = PlasmaPink,
    onError = Color.White
)

@Composable
fun CosmicTimeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CosmicColorScheme,
        typography = Typography,
        content = content
    )
}
