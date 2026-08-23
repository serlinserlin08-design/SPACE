package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NebulaViolet
import com.example.ui.theme.SpaceBlack
import com.example.ui.theme.SpaceDeepBlue
import com.example.ui.theme.SupernovaGold
import kotlin.random.Random

private data class Star(
    val xRatio: Float,
    val yRatio: Float,
    val baseRadius: Float,
    val color: Color,
    val twinkleSpeed: Int,
    val phaseOffset: Float
)

@Composable
fun StarfieldBackground(
    modifier: Modifier = Modifier,
    starCount: Int = 120
) {
    val stars = remember {
        val random = Random(42)
        val starColors = listOf(
            Color.White,
            Color(0xFFE2E8F0),
            NeonCyan.copy(alpha = 0.8f),
            NebulaViolet.copy(alpha = 0.8f),
            SupernovaGold.copy(alpha = 0.9f)
        )
        List(starCount) {
            Star(
                xRatio = random.nextFloat(),
                yRatio = random.nextFloat(),
                baseRadius = random.nextFloat() * 2.2f + 0.8f,
                color = starColors.random(random),
                twinkleSpeed = random.nextInt(2000, 4500),
                phaseOffset = random.nextFloat()
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "StarfieldTwinkle")
    val twinklePulse = infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "twinklePulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Deep Space Radial & Linear Glows
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    SpaceBlack,
                    SpaceDeepBlue,
                    SpaceBlack
                )
            )
        )

        // Cosmic Nebula Glow Overlay
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    NebulaViolet.copy(alpha = 0.12f),
                    Color.Transparent
                ),
                center = Offset(width * 0.85f, height * 0.15f),
                radius = width * 0.65f
            )
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    NeonCyan.copy(alpha = 0.08f),
                    Color.Transparent
                ),
                center = Offset(width * 0.15f, height * 0.75f),
                radius = width * 0.7f
            )
        )

        // Render Twinkling Stars
        stars.forEach { star ->
            val brightnessFactor = ((twinklePulse.value + star.phaseOffset) % 1.0f).coerceIn(0.25f, 1.0f)
            val currentRadius = star.baseRadius * (0.8f + (brightnessFactor * 0.4f))
            drawCircle(
                color = star.color.copy(alpha = brightnessFactor),
                radius = currentRadius,
                center = Offset(star.xRatio * width, star.yRatio * height)
            )
        }
    }
}
