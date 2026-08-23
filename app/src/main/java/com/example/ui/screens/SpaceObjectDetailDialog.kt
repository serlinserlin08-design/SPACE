package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.model.SpaceObject
import com.example.ui.components.CosmicGlassCard
import com.example.ui.components.ScientificStatusBadge
import com.example.ui.theme.SleekBlack
import com.example.ui.theme.SleekBlue
import com.example.ui.theme.SleekBlueDark
import com.example.ui.theme.SleekBorderSubtle
import com.example.ui.theme.SleekCardElevated
import com.example.ui.theme.SleekCardSurface
import com.example.ui.theme.SleekGold
import com.example.ui.theme.SleekPurple
import com.example.ui.theme.SleekPurpleDark
import com.example.ui.theme.TextSlate100
import com.example.ui.theme.TextSlate300
import com.example.ui.theme.TextSlate400
import com.example.ui.theme.TextSlate500

@Composable
fun SpaceObjectDetailDialog(
    obj: SpaceObject,
    isFavorite: Boolean,
    onDismiss: () -> Unit,
    onToggleFavorite: (SpaceObject, Boolean) -> Unit,
    onShare: (SpaceObject) -> Unit,
    onView3D: ((SpaceObject) -> Unit)? = null
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(SleekBlack.copy(alpha = 0.98f))
                .padding(16.dp),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Action Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSlate100)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { onToggleFavorite(obj, isFavorite) },
                            modifier = Modifier.testTag("detail_dialog_bookmark_button")
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (isFavorite) SleekGold else TextSlate400
                            )
                        }

                        IconButton(
                            onClick = { onShare(obj) },
                            modifier = Modifier.testTag("detail_dialog_share_button")
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = SleekBlue)
                        }
                    }
                }

                // Hero Image Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.dp, SleekBorderSubtle, RoundedCornerShape(18.dp))
                ) {
                    val imageRes = obj.imageDrawableRes ?: R.drawable.img_hero_cosmos
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = obj.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, SleekBlack.copy(alpha = 0.9f))
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        ScientificStatusBadge(status = obj.status)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = obj.name,
                            color = TextSlate100,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${obj.designation} • Discovered ${obj.discoveryYear}",
                            color = SleekBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Prominent "🌌 VIEW IN 3D" Button
                ElevatedButton(
                    onClick = {
                        onDismiss()
                        onView3D?.invoke(obj)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(SleekBlue, SleekPurple, Color(0xFF38BDF8))
                            )
                        )
                        .testTag("view_in_3d_button"),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🌌 VIEW IN 3D",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Overview Card
                CosmicGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = SleekBorderSubtle,
                    backgroundColor = SleekCardSurface
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "OBJECT OVERVIEW",
                            color = SleekBlue,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = obj.overview,
                            color = TextSlate100,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 6-Point Scientific Telemetry Matrix
                Text(
                    text = "ASTROPHYSICAL TELEMETRY",
                    color = TextSlate500,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TelemetryBox(
                            title = "Distance from Earth",
                            value = obj.distanceDisplay,
                            iconColor = SleekBlue,
                            modifier = Modifier.weight(1f)
                        )
                        TelemetryBox(
                            title = "Estimated Mass",
                            value = obj.massDisplay,
                            iconColor = SleekGold,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TelemetryBox(
                            title = "Radius / Scale",
                            value = obj.radiusDisplay,
                            iconColor = SleekPurple,
                            modifier = Modifier.weight(1f)
                        )
                        TelemetryBox(
                            title = "Temperature",
                            value = obj.temperatureDisplay,
                            iconColor = Color(0xFFF43F5E),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TelemetryBox(
                            title = "Gravitational Field",
                            value = obj.gravitationalPullDisplay,
                            iconColor = SleekBlue,
                            modifier = Modifier.weight(1f)
                        )
                        TelemetryBox(
                            title = "Object Class",
                            value = obj.category.label,
                            iconColor = Color(0xFF10B981),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Relativistic Spacetime & Mechanics Breakdown
                CosmicGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = SleekPurpleDark.copy(alpha = 0.4f),
                    backgroundColor = SleekCardSurface
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "FASCINATING SPACETIME MECHANICS",
                            color = SleekPurple,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = obj.fascinatingMechanics,
                            color = TextSlate300,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun TelemetryBox(
    title: String,
    value: String,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SleekCardSurface)
            .border(1.dp, SleekBorderSubtle, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = title,
                color = TextSlate500,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = TextSlate100,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
