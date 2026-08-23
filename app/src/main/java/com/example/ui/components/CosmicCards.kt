package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ScientificCertainty
import com.example.data.model.ScientificStatus
import com.example.ui.theme.SleekBlue
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekBorderSubtle
import com.example.ui.theme.SleekCardElevated
import com.example.ui.theme.SleekCardSurface
import com.example.ui.theme.SleekGlassSubtle
import com.example.ui.theme.SleekHeroEnd
import com.example.ui.theme.SleekHeroVia
import com.example.ui.theme.SleekPurple
import com.example.ui.theme.TextSlate100
import com.example.ui.theme.TextSlate300
import com.example.ui.theme.TextSlate400
import com.example.ui.theme.TextSlate500
import com.example.ui.theme.TextSlate600

@Composable
fun CosmicGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    borderColor: Color = SleekBorder,
    backgroundColor: Color = SleekCardSurface.copy(alpha = 0.9f),
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val cardModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else {
        modifier
    }

    Card(
        modifier = cardModifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        content()
    }
}

/**
 * Sleek Interface Hero Gradient Card (from-[#1A1C2E] via-[#2D1B4E] to-[#0D0F1A])
 * with dashed orbital ring accents and glass highlights.
 */
@Composable
fun SleekHeroCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val clickMod = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        SleekCardElevated,
                        SleekHeroVia,
                        SleekHeroEnd
                    )
                )
            )
            .then(clickMod)
    ) {
        // Dashed orbital decorative background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 2.dp.toPx()
            val dashPathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
            drawCircle(
                color = Color.White.copy(alpha = 0.12f),
                radius = size.width * 0.38f,
                center = Offset(size.width * 0.92f, size.height * 0.15f),
                style = Stroke(width = strokeWidth, pathEffect = dashPathEffect)
            )
            drawCircle(
                color = SleekPurple.copy(alpha = 0.08f),
                radius = size.width * 0.58f,
                center = Offset(size.width * 0.92f, size.height * 0.15f),
                style = Stroke(width = strokeWidth * 0.75f, pathEffect = dashPathEffect)
            )
        }

        content()
    }
}

/**
 * Sleek Telemetry Quick Stat Card
 */
@Composable
fun SleekStatCard(
    title: String,
    subtitle: String,
    value: String,
    valueLabel: String,
    iconEmoji: String,
    iconBgColor: Color,
    valueColor: Color = SleekBlue,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val clickModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(SleekCardSurface)
            .then(clickModifier)
            .padding(14.dp)
    ) {
        Column {
            // Icon Pill
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(text = iconEmoji, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                color = TextSlate300,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                color = TextSlate500,
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                color = valueColor,
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = valueLabel,
                color = TextSlate600,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Sleek List Item Card
 */
@Composable
fun SleekListItem(
    title: String,
    subtitle: String,
    iconEmoji: String,
    iconBgColor: Color,
    modifier: Modifier = Modifier,
    trailingText: String = "→",
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SleekGlassSubtle)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Text(text = iconEmoji, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = TextSlate100,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = TextSlate500,
                fontSize = 11.sp,
                maxLines = 1
            )
        }

        Text(
            text = trailingText,
            color = TextSlate500,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CertaintyBadge(
    certainty: ScientificCertainty,
    modifier: Modifier = Modifier
) {
    val color = Color(certainty.badgeColorHex)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = certainty.label,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ScientificStatusBadge(
    status: ScientificStatus,
    modifier: Modifier = Modifier
) {
    val color = when (status) {
        ScientificStatus.OBSERVED_CONFIRMED -> Color(0xFF34D399)
        ScientificStatus.CANDIDATE_STUDY -> SleekBlue
        ScientificStatus.THEORETICAL_EXTRAPOLATION -> Color(0xFFFBBF24)
    }
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = status.label,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun CosmicMetricPill(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SleekCardElevated.copy(alpha = 0.85f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$label: ",
                color = TextSlate500,
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = value,
                color = TextSlate100,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
