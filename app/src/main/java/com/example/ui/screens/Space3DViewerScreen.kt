package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.LabelOff
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Space3DHotspot
import com.example.data.model.Space3DModelData
import com.example.data.model.Space3DObjectType
import com.example.ui.theme.SleekBlack
import com.example.ui.theme.SleekBlue
import com.example.ui.theme.SleekBorderSubtle
import com.example.ui.theme.SleekCardSurface
import com.example.ui.theme.SleekNavy
import com.example.ui.theme.SleekPurple
import com.example.ui.theme.TextSlate100
import com.example.ui.theme.TextSlate300
import com.example.ui.theme.TextSlate400
import com.example.ui.theme.TextSlate500
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

enum class RenderViewMode {
    REALISTIC,
    WIREFRAME_GRID,
    XRAY_CORE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Space3DViewerScreen(
    model: Space3DModelData,
    onBack: () -> Unit
) {
    // 3D Camera / Transform State
    var rotX by remember { mutableFloatStateOf(model.axialTiltDegrees) } // Pitch
    var rotY by remember { mutableFloatStateOf(0f) } // Yaw
    var zoomScale by remember { mutableFloatStateOf(1.0f) }
    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var panOffsetY by remember { mutableFloatStateOf(0f) }

    // Interactive Toggles
    var isAutoRotating by remember { mutableStateOf(true) }
    var showHotspotLabels by remember { mutableStateOf(true) }
    var isFullScreen by remember { mutableStateOf(false) }
    var renderMode by remember { mutableStateOf(RenderViewMode.REALISTIC) }
    var showInfoSheet by remember { mutableStateOf(false) }
    var selectedHotspot by remember { mutableStateOf<Space3DHotspot?>(null) }
    var isPanModeEnabled by remember { mutableStateOf(false) }

    // Animations
    val coroutineScope = rememberCoroutineScope()
    val infiniteTransition = rememberInfiniteTransition(label = "3DWorldLoop")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(tween(2500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "GlowPulse"
    )
    val autoSpinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween((20000 / model.rotationSpeed).toInt().coerceIn(4000, 60000), easing = LinearEasing), RepeatMode.Restart),
        label = "AutoSpin"
    )

    // Compute effective Yaw
    val currentYaw = if (isAutoRotating) rotY + autoSpinAngle else rotY

    // Reset Camera Animation
    fun resetCamera() {
        coroutineScope.launch {
            rotX = model.axialTiltDegrees
            rotY = 0f
            zoomScale = 1.0f
            panOffsetX = 0f
            panOffsetY = 0f
            isAutoRotating = true
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = SleekBlack
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF0F172A), SleekBlack),
                        center = Offset(500f, 600f),
                        radius = 1200f
                    )
                )
        ) {
            // ================= 3D INTERACTIVE CANVAS =================
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(isPanModeEnabled) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            if (isPanModeEnabled) {
                                panOffsetX += pan.x
                                panOffsetY += pan.y
                            } else {
                                rotY += pan.x * 0.4f
                                rotX = (rotX - pan.y * 0.4f).coerceIn(-89f, 89f)
                            }
                            zoomScale = (zoomScale * zoom).coerceIn(0.4f, 4.0f)
                            if (abs(pan.x) > 1f || abs(pan.y) > 1f) {
                                isAutoRotating = false
                            }
                        }
                    }
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("space_3d_canvas")
                ) {
                    val centerX = size.width / 2f + panOffsetX
                    val centerY = size.height / 2f + panOffsetY
                    val effectiveRadius = (model.baseRadius * zoomScale).coerceAtLeast(30f)

                    // 1. Draw Starfield Background in 3D depth
                    drawStarfield3D(rotX, currentYaw, size)

                    // 2. Draw Space Object based on Type and Mode
                    drawCelestialObject3D(
                        model = model,
                        centerX = centerX,
                        centerY = centerY,
                        radius = effectiveRadius,
                        rotX = rotX,
                        rotY = currentYaw,
                        pulse = pulseGlow,
                        mode = renderMode
                    )

                    // 3. Draw Relativistic Jets / Accretion Ring (if Black Hole or Pulsar)
                    if (model.hasAccretionDisk) {
                        drawAccretionDisk3D(
                            centerX = centerX,
                            centerY = centerY,
                            radius = effectiveRadius,
                            rotX = rotX,
                            rotY = currentYaw,
                            pulse = pulseGlow
                        )
                    }

                    // 4. Draw Rings (if present, like Saturn)
                    if (model.hasRings) {
                        drawPlanetaryRings3D(
                            model = model,
                            centerX = centerX,
                            centerY = centerY,
                            radius = effectiveRadius,
                            rotX = rotX,
                            rotY = currentYaw
                        )
                    }

                    // 5. Draw Spacecraft Modules / Solar Arrays (if ISS / JWST / Satellite)
                    if (model.hasSolarPanels || model.hasHexagonalMirrors || model.hasHighGainAntenna) {
                        drawSpacecraftStructure3D(
                            model = model,
                            centerX = centerX,
                            centerY = centerY,
                            radius = effectiveRadius,
                            rotX = rotX,
                            rotY = currentYaw
                        )
                    }

                    // 6. Draw 3D Hotspot Annotations
                    if (showHotspotLabels) {
                        drawHotspots3D(
                            hotspots = model.hotspots,
                            centerX = centerX,
                            centerY = centerY,
                            radius = effectiveRadius,
                            rotX = rotX,
                            rotY = currentYaw
                        )
                    }
                }
            }

            // ================= TOP BAR OVERLAY =================
            AnimatedVisibility(
                visible = !isFullScreen,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    SleekBlack.copy(alpha = 0.92f),
                                    SleekBlack.copy(alpha = 0.6f),
                                    Color.Transparent
                                )
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(SleekCardSurface)
                                    .border(1.dp, SleekBorderSubtle, CircleShape)
                                    .testTag("viewer_back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = TextSlate100,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = model.name,
                                    color = TextSlate100,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = model.scientificDesignation,
                                    color = SleekBlue,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Info Sheet Toggle Button
                        IconButton(
                            onClick = { showInfoSheet = true },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SleekCardSurface)
                                .border(1.dp, SleekBorderSubtle, CircleShape)
                                .testTag("object_info_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Object Info",
                                tint = SleekBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Epistemic Classification Badge (Mandatory requirement)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (model.isConfirmedObservationalData) Color(0xFF064E3B).copy(alpha = 0.8f)
                                else Color(0xFF312E81).copy(alpha = 0.8f)
                            )
                            .border(
                                0.5.dp,
                                if (model.isConfirmedObservationalData) Color(0xFF10B981) else Color(0xFF818CF8),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (model.isConfirmedObservationalData) "✓ ${model.classificationLabel.uppercase()}"
                                else "✨ ${model.classificationLabel.uppercase()}",
                                color = if (model.isConfirmedObservationalData) Color(0xFF34D399) else Color(0xFFA5B4FC),
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }
                }
            }

            // ================= INTERACTIVE FLOATING CONTROLS (RIGHT SIDE) =================
            AnimatedVisibility(
                visible = !isFullScreen,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(SleekNavy.copy(alpha = 0.85f))
                        .border(1.dp, SleekBorderSubtle, RoundedCornerShape(24.dp))
                        .padding(vertical = 8.dp, horizontal = 6.dp)
                ) {
                    // Zoom In
                    ViewerControlButton(
                        icon = Icons.Default.Add,
                        label = "Zoom In",
                        onClick = { zoomScale = (zoomScale * 1.25f).coerceAtMost(4.0f) },
                        testTag = "zoom_in_button"
                    )

                    // Zoom Out
                    ViewerControlButton(
                        icon = Icons.Default.Remove,
                        label = "Zoom Out",
                        onClick = { zoomScale = (zoomScale / 1.25f).coerceAtLeast(0.4f) },
                        testTag = "zoom_out_button"
                    )

                    // Reset Camera
                    ViewerControlButton(
                        icon = Icons.Default.RestartAlt,
                        label = "Reset Camera",
                        onClick = { resetCamera() },
                        testTag = "reset_camera_button"
                    )

                    // Auto-Rotate Toggle (360° Viewing)
                    ViewerControlButton(
                        icon = if (isAutoRotating) Icons.Default.Sync else Icons.Default.PlayArrow,
                        label = if (isAutoRotating) "Pause Rotation" else "Auto Rotate 360°",
                        isActive = isAutoRotating,
                        onClick = { isAutoRotating = !isAutoRotating },
                        testTag = "auto_rotate_button"
                    )

                    // Pan Mode Toggle
                    ViewerControlButton(
                        icon = Icons.Default.TouchApp,
                        label = if (isPanModeEnabled) "Pan Mode" else "Orbit Mode",
                        isActive = isPanModeEnabled,
                        onClick = { isPanModeEnabled = !isPanModeEnabled },
                        testTag = "pan_mode_button"
                    )

                    // Hotspots / Labels Toggle
                    ViewerControlButton(
                        icon = if (showHotspotLabels) Icons.Default.Label else Icons.Default.LabelOff,
                        label = "Toggle Labels",
                        isActive = showHotspotLabels,
                        onClick = { showHotspotLabels = !showHotspotLabels },
                        testTag = "toggle_labels_button"
                    )

                    // Render Mode Switch (Realistic / Wireframe / X-Ray)
                    ViewerControlButton(
                        icon = when (renderMode) {
                            RenderViewMode.REALISTIC -> Icons.Default.LightMode
                            RenderViewMode.WIREFRAME_GRID -> Icons.Default.GridOn
                            RenderViewMode.XRAY_CORE -> Icons.Default.Visibility
                        },
                        label = "Mode: ${renderMode.name}",
                        onClick = {
                            renderMode = when (renderMode) {
                                RenderViewMode.REALISTIC -> RenderViewMode.WIREFRAME_GRID
                                RenderViewMode.WIREFRAME_GRID -> RenderViewMode.XRAY_CORE
                                RenderViewMode.XRAY_CORE -> RenderViewMode.REALISTIC
                            }
                        },
                        testTag = "render_mode_button"
                    )
                }
            }

            // ================= BOTTOM BAR (QUICK METRICS & FULLSCREEN) =================
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                SleekBlack.copy(alpha = 0.75f),
                                SleekBlack.copy(alpha = 0.95f)
                            )
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Quick Scale Pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(SleekCardSurface)
                            .border(1.dp, SleekBorderSubtle, RoundedCornerShape(20.dp))
                            .clickable { showInfoSheet = true }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Science,
                            contentDescription = "Metrics",
                            tint = SleekBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Radius: ${model.radiusDisplay}",
                            color = TextSlate300,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "• Details ❯",
                            color = SleekBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Full-screen Mode Toggle
                    IconButton(
                        onClick = { isFullScreen = !isFullScreen },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(SleekCardSurface)
                            .border(1.dp, SleekBorderSubtle, CircleShape)
                            .testTag("fullscreen_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isFullScreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                            contentDescription = "Fullscreen",
                            tint = TextSlate100,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // ================= HOTSPOT POPUP DIALOG =================
            selectedHotspot?.let { spot ->
                Dialog(
                    onDismissRequest = { selectedHotspot = null },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(SleekNavy)
                            .border(1.dp, SleekBlue.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                            .padding(20.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = spot.name,
                                    color = TextSlate100,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(
                                    onClick = { selectedHotspot = null },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close",
                                        tint = TextSlate400
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = spot.description,
                                color = TextSlate300,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // ================= OBJECT INFORMATION BOTTOM SHEET =================
            if (showInfoSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showInfoSheet = false },
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    containerColor = SleekNavy,
                    dragHandle = {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .size(width = 40.dp, height = 4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(TextSlate500)
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 32.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = model.name,
                                    color = TextSlate100,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = model.scientificDesignation,
                                    color = SleekBlue,
                                    fontSize = 12.sp
                                )
                            }
                            IconButton(onClick = { showInfoSheet = false }) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSlate400)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Classification Disclaimer
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0F172A))
                                .border(0.5.dp, SleekBorderSubtle, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "🏷️ ${model.classificationLabel}",
                                    color = if (model.isConfirmedObservationalData) Color(0xFF34D399) else Color(0xFF818CF8),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = model.classificationDisclaimer,
                                    color = TextSlate400,
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "SCIENTIFIC METRICS",
                            color = TextSlate500,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        MetricGridItem("Physical Radius", model.radiusDisplay)
                        MetricGridItem("Distance from Earth", model.distanceDisplay)
                        MetricGridItem("Gravitational Mass", model.massDisplay)
                        MetricGridItem("Surface Temperature", model.surfaceTemperatureDisplay)
                        MetricGridItem("Surface Gravity", model.gravityDisplay)
                        MetricGridItem("Orbital Period", model.orbitalPeriodDisplay)

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "ASTROPHYSICAL OVERVIEW",
                            color = TextSlate500,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = model.scientificOverview,
                            color = TextSlate300,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )

                        if (model.interestingFacts.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "IMPORTANT FACTS & OBSERVATIONS",
                                color = TextSlate500,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            model.interestingFacts.forEach { fact ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Text(text = "•", color = SleekBlue, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = fact, color = TextSlate300, fontSize = 12.sp, lineHeight = 17.sp)
                                }
                            }
                        }

                        if (model.hotspots.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "STRUCTURAL FEATURES & HOTSPOTS",
                                color = TextSlate500,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            model.hotspots.forEach { spot ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF0B1120))
                                        .border(0.5.dp, SleekBorderSubtle, RoundedCornerShape(8.dp))
                                        .clickable {
                                            showInfoSheet = false
                                            selectedHotspot = spot
                                        }
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(text = spot.name, color = SleekBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text(text = spot.description, color = TextSlate400, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricGridItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF0B1120))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = TextSlate400, fontSize = 11.5.sp)
        Text(text = value, color = TextSlate100, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ViewerControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean = false,
    onClick: () -> Unit,
    testTag: String
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(if (isActive) SleekBlue else Color.Transparent)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isActive) Color.White else TextSlate300,
            modifier = Modifier.size(18.dp)
        )
    }
}

// ================= 3D CANVAS RENDER HELPER FUNCTIONS =================

private fun DrawScope.drawStarfield3D(rotX: Float, rotY: Float, size: Size) {
    val starCount = 120
    val radX = rotX * (PI / 180f).toFloat()
    val radY = rotY * (PI / 180f).toFloat()

    for (i in 0 until starCount) {
        val seed = i * 137.5f
        val sx = (sin(seed * 0.1f) * 0.5f + 0.5f) * size.width
        val sy = (cos(seed * 0.2f) * 0.5f + 0.5f) * size.height
        val depth = ((i % 5) + 1) * 0.2f
        val brightness = ((sin(seed + radY) * 0.3f + 0.7f) * 255).toInt().coerceIn(60, 255)

        // Parallax star shift
        val offsetX = (sx + sin(radY * depth) * 40f) % size.width
        val offsetY = (sy + sin(radX * depth) * 30f) % size.height

        drawCircle(
            color = Color(brightness, brightness, brightness, brightness),
            radius = if (i % 7 == 0) 1.8f else 1.0f,
            center = Offset(offsetX, offsetY)
        )
    }
}

private fun DrawScope.drawCelestialObject3D(
    model: Space3DModelData,
    centerX: Float,
    centerY: Float,
    radius: Float,
    rotX: Float,
    rotY: Float,
    pulse: Float,
    mode: RenderViewMode
) {
    val primaryColor = Color(model.primaryColorHex)
    val secondaryColor = Color(model.secondaryColorHex)
    val accentColor = Color(model.accentColorHex)

    // 1. Atmosphere Fresnel Outer Glow
    if (model.hasAtmosphere && mode != RenderViewMode.WIREFRAME_GRID) {
        val glowRadius = radius + model.atmosphereThickness * pulse
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(model.atmosphereColorHex).copy(alpha = 0.45f),
                    Color(model.atmosphereColorHex).copy(alpha = 0.15f),
                    Color.Transparent
                ),
                center = Offset(centerX, centerY),
                radius = glowRadius
            ),
            radius = glowRadius,
            center = Offset(centerX, centerY)
        )
    }

    when (mode) {
        RenderViewMode.REALISTIC -> {
            // Spherical Lambertian Lighting Calculation
            val lightAngle = 45f * (PI / 180f).toFloat()
            val lightOffsetX = centerX - radius * 0.35f
            val lightOffsetY = centerY - radius * 0.35f

            // Base Spherical Gradient
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        accentColor,
                        primaryColor,
                        secondaryColor,
                        Color(0xFF030712) // Dark terminator edge
                    ),
                    center = Offset(lightOffsetX, lightOffsetY),
                    radius = radius * 1.2f
                ),
                radius = radius,
                center = Offset(centerX, centerY)
            )

            // Texture details (Continents, Bands, Craters, Granulation, Spiral Arms)
            drawSurfaceTextures3D(
                model = model,
                centerX = centerX,
                centerY = centerY,
                radius = radius,
                rotX = rotX,
                rotY = rotY
            )
        }

        RenderViewMode.WIREFRAME_GRID -> {
            // Scientific Wireframe Longitude and Latitude 3D lines
            drawCircle(
                color = SleekBlue.copy(alpha = 0.2f),
                radius = radius,
                center = Offset(centerX, centerY),
                style = Fill
            )
            drawCircle(
                color = SleekBlue,
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1.5f)
            )
            draw3DWireframeGrid(
                centerX = centerX,
                centerY = centerY,
                radius = radius,
                rotX = rotX,
                rotY = rotY
            )
        }

        RenderViewMode.XRAY_CORE -> {
            // Core Cutaway X-Ray
            drawCircle(
                color = secondaryColor.copy(alpha = 0.4f),
                radius = radius,
                center = Offset(centerX, centerY),
                style = Fill
            )
            drawCircle(
                color = accentColor,
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
            )
            // Dense Inner Core
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFEF08A), Color(0xFFF97316), Color(0xFFDC2626)),
                    center = Offset(centerX, centerY),
                    radius = radius * 0.4f
                ),
                radius = radius * 0.4f,
                center = Offset(centerX, centerY)
            )
            // Core Magnetic Lines
            draw3DWireframeGrid(
                centerX = centerX,
                centerY = centerY,
                radius = radius,
                rotX = rotX,
                rotY = rotY,
                lineColor = Color.Cyan.copy(alpha = 0.5f)
            )
        }
    }
}

private fun DrawScope.drawSurfaceTextures3D(
    model: Space3DModelData,
    centerX: Float,
    centerY: Float,
    radius: Float,
    rotX: Float,
    rotY: Float
) {
    val radY = rotY * (PI / 180f).toFloat()

    when (model.texturePattern) {
        "bands" -> {
            // Jupiter / Saturn atmospheric jet stream bands
            for (i in -4..4) {
                val bandY = centerY + (i * radius * 0.22f)
                val bandHeight = radius * 0.12f
                val bandWidth = 2f * sqrt((radius * radius - (bandY - centerY) * (bandY - centerY)).coerceAtLeast(0f))
                if (bandWidth > 5f) {
                    val bandColor = if (i % 2 == 0) Color(model.secondaryColorHex).copy(alpha = 0.6f)
                    else Color(model.accentColorHex).copy(alpha = 0.4f)
                    drawRect(
                        color = bandColor,
                        topLeft = Offset(centerX - bandWidth / 2f, bandY - bandHeight / 2f),
                        size = Size(bandWidth, bandHeight)
                    )
                }
            }
        }

        "continents" -> {
            // Earth / Mars / Exoplanet landmasses rotating in 3D
            val continentCount = 6
            for (c in 0 until continentCount) {
                val baseAngle = c * (2 * PI / continentCount) + radY
                val contX = centerX + cos(baseAngle).toFloat() * radius * 0.7f
                val contY = centerY + sin(c * 1.5f) * radius * 0.4f
                val isVisible = cos(baseAngle) > -0.2f // Depth occlusion

                if (isVisible) {
                    val contRadius = radius * 0.28f * (cos(baseAngle).toFloat() * 0.4f + 0.6f)
                    drawCircle(
                        color = Color(model.secondaryColorHex).copy(alpha = 0.85f),
                        radius = contRadius,
                        center = Offset(contX, contY)
                    )
                }
            }
        }

        "cratered" -> {
            // Moon / Asteroid impact craters
            val craterCount = 8
            for (k in 0 until craterCount) {
                val angle = k * (2 * PI / craterCount) + radY
                val cx = centerX + cos(angle).toFloat() * radius * 0.65f
                val cy = centerY + sin(k * 2.1f) * radius * 0.5f
                if (cos(angle) > -0.1f) {
                    drawCircle(
                        color = Color(0xFF1E293B).copy(alpha = 0.9f),
                        radius = radius * 0.12f,
                        center = Offset(cx, cy),
                        style = Stroke(width = 2f)
                    )
                }
            }
        }

        "granulation" -> {
            // Solar plasma convection cells
            val flareCount = 12
            for (f in 0 until flareCount) {
                val fAngle = f * (2 * PI / flareCount) + radY * 0.5f
                val fx = centerX + cos(fAngle).toFloat() * radius * 0.8f
                val fy = centerY + sin(f * 3.7f) * radius * 0.6f
                drawCircle(
                    color = Color(model.accentColorHex).copy(alpha = 0.35f),
                    radius = radius * 0.18f,
                    center = Offset(fx, fy)
                )
            }
        }

        "spiral_arms" -> {
            // Galaxy spiral arms
            val armCount = 2
            val armPoints = 80
            for (arm in 0 until armCount) {
                val armOffsetAngle = arm * PI.toFloat() + radY
                for (p in 0 until armPoints) {
                    val progress = p.toFloat() / armPoints
                    val armR = progress * radius * 1.1f
                    val theta = armOffsetAngle + progress * 3.5f
                    val armX = centerX + cos(theta) * armR
                    val armY = centerY + sin(theta) * armR * 0.45f // 3D tilt
                    drawCircle(
                        color = if (arm == 0) Color(model.primaryColorHex).copy(alpha = 0.6f) else Color(model.accentColorHex).copy(alpha = 0.6f),
                        radius = 2f + progress * 3f,
                        center = Offset(armX, armY)
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawPlanetaryRings3D(
    model: Space3DModelData,
    centerX: Float,
    centerY: Float,
    radius: Float,
    rotX: Float,
    rotY: Float
) {
    val ringInner = radius * model.ringInnerRatio
    val ringOuter = radius * model.ringOuterRatio
    val tiltRad = (model.ringTiltDegrees + rotX * 0.5f) * (PI / 180f).toFloat()

    val ringWidth = ringOuter - ringInner
    val path = Path()

    // Draw Elliptical Rings in 3D Perspective
    drawOval(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.Transparent,
                Color(model.ringPrimaryColorHex).copy(alpha = 0.85f),
                Color(0xFF0F172A).copy(alpha = 0.3f), // Cassini division
                Color(model.ringSecondaryColorHex).copy(alpha = 0.75f),
                Color.Transparent
            ),
            center = Offset(centerX, centerY),
            radius = ringOuter
        ),
        topLeft = Offset(centerX - ringOuter, centerY - ringOuter * 0.32f),
        size = Size(ringOuter * 2f, ringOuter * 0.64f),
        style = Stroke(width = ringWidth)
    )
}

private fun DrawScope.drawAccretionDisk3D(
    centerX: Float,
    centerY: Float,
    radius: Float,
    rotX: Float,
    rotY: Float,
    pulse: Float
) {
    val diskRadius = radius * 2.8f * pulse

    // Accretion Disk (Doppler Blueshifted Left / Redshifted Right)
    drawOval(
        brush = Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF38BDF8), // Relativistic approaching blueshift
                Color(0xFFF97316),
                Color(0xFFEF4444), // Relativistic receding redshift
                Color(0xFF7F1D1D)
            )
        ),
        topLeft = Offset(centerX - diskRadius, centerY - diskRadius * 0.28f),
        size = Size(diskRadius * 2f, diskRadius * 0.56f),
        style = Stroke(width = radius * 0.45f)
    )

    // Einstein Photon Ring
    drawCircle(
        color = Color(0xFFFBBF24).copy(alpha = 0.9f),
        radius = radius * 1.25f,
        center = Offset(centerX, centerY),
        style = Stroke(width = 3f)
    )

    // Relativistic Polar Jets
    val jetLength = radius * 3.2f
    drawLine(
        brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color(0xFF67E8F9), Color.White)
        ),
        start = Offset(centerX, centerY - radius * 0.8f),
        end = Offset(centerX, centerY - jetLength),
        strokeWidth = 5f,
        cap = StrokeCap.Round
    )
    drawLine(
        brush = Brush.verticalGradient(
            colors = listOf(Color.White, Color(0xFF67E8F9), Color.Transparent)
        ),
        start = Offset(centerX, centerY + radius * 0.8f),
        end = Offset(centerX, centerY + jetLength),
        strokeWidth = 5f,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawSpacecraftStructure3D(
    model: Space3DModelData,
    centerX: Float,
    centerY: Float,
    radius: Float,
    rotX: Float,
    rotY: Float
) {
    val radY = rotY * (PI / 180f).toFloat()

    // Central Cylindrical Hull / Bus
    drawRoundRect(
        color = Color(model.primaryColorHex),
        topLeft = Offset(centerX - radius * 0.5f, centerY - radius * 0.35f),
        size = Size(radius * 1.0f, radius * 0.7f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
    )

    // Hexagonal Gold Mirror Array (JWST)
    if (model.hasHexagonalMirrors) {
        val hexPoints = 6
        val mirrorRadius = radius * 0.65f
        for (h in 0 until hexPoints) {
            val angle = h * (2 * PI / hexPoints)
            val mx = centerX + cos(angle).toFloat() * mirrorRadius * 0.6f
            val my = centerY + sin(angle).toFloat() * mirrorRadius * 0.6f
            drawCircle(
                color = Color(0xFFFBBF24),
                radius = mirrorRadius * 0.28f,
                center = Offset(mx, my)
            )
            drawCircle(
                color = Color(0xFFD97706),
                radius = mirrorRadius * 0.28f,
                center = Offset(mx, my),
                style = Stroke(width = 1.5f)
            )
        }
    }

    // Solar Panel Arrays (ISS / Satellites)
    if (model.hasSolarPanels) {
        val panelWidth = radius * 1.4f
        val panelHeight = radius * 0.35f
        // Left Wing
        drawRect(
            color = Color(0xFF1E3A8A),
            topLeft = Offset(centerX - radius * 0.5f - panelWidth, centerY - panelHeight / 2f),
            size = Size(panelWidth, panelHeight)
        )
        drawRect(
            color = Color(0xFFF59E0B),
            topLeft = Offset(centerX - radius * 0.5f - panelWidth, centerY - panelHeight / 2f),
            size = Size(panelWidth, panelHeight),
            style = Stroke(width = 1.5f)
        )
        // Right Wing
        drawRect(
            color = Color(0xFF1E3A8A),
            topLeft = Offset(centerX + radius * 0.5f, centerY - panelHeight / 2f),
            size = Size(panelWidth, panelHeight)
        )
        drawRect(
            color = Color(0xFFF59E0B),
            topLeft = Offset(centerX + radius * 0.5f, centerY - panelHeight / 2f),
            size = Size(panelWidth, panelHeight),
            style = Stroke(width = 1.5f)
        )
    }

    // High Gain Antenna (Voyager)
    if (model.hasHighGainAntenna) {
        drawOval(
            color = Color(0xFFE2E8F0),
            topLeft = Offset(centerX - radius * 0.75f, centerY - radius * 0.75f),
            size = Size(radius * 1.5f, radius * 0.5f),
            style = Stroke(width = 3f)
        )
    }
}

private fun DrawScope.draw3DWireframeGrid(
    centerX: Float,
    centerY: Float,
    radius: Float,
    rotX: Float,
    rotY: Float,
    lineColor: Color = SleekBlue.copy(alpha = 0.6f)
) {
    val latLines = 6
    val longLines = 8

    // Latitude Ellipses
    for (i in 1..latLines) {
        val latFraction = (i.toFloat() / (latLines + 1)) * 2f - 1f
        val yOffset = latFraction * radius * 0.85f
        val currentRadius = sqrt((radius * radius - yOffset * yOffset).coerceAtLeast(0f))
        drawOval(
            color = lineColor,
            topLeft = Offset(centerX - currentRadius, centerY + yOffset - currentRadius * 0.25f),
            size = Size(currentRadius * 2f, currentRadius * 0.5f),
            style = Stroke(width = 1.0f)
        )
    }

    // Longitude Meridians
    for (j in 0 until longLines) {
        val angle = (j * (180f / longLines) + rotY) * (PI / 180f).toFloat()
        val width = radius * cos(angle).toFloat()
        drawOval(
            color = lineColor,
            topLeft = Offset(centerX - abs(width), centerY - radius),
            size = Size(abs(width) * 2f, radius * 2f),
            style = Stroke(width = 1.0f)
        )
    }
}

private fun DrawScope.drawHotspots3D(
    hotspots: List<Space3DHotspot>,
    centerX: Float,
    centerY: Float,
    radius: Float,
    rotX: Float,
    rotY: Float
) {
    val radX = rotX * (PI / 180f).toFloat()
    val radY = rotY * (PI / 180f).toFloat()

    hotspots.forEach { spot ->
        val spotU = (spot.u * (PI / 180f).toFloat()) + radY
        val spotV = (spot.v * (PI / 180f).toFloat())

        // 3D Spherical Coordinate Projection
        val spotRadius = radius * spot.radiusRatio
        val x3D = spotRadius * cos(spotV) * sin(spotU)
        val y3D = -spotRadius * sin(spotV)
        val z3D = spotRadius * cos(spotV) * cos(spotU)

        // Only draw front hemisphere hotspots (z3D > -10f)
        if (z3D > -10f) {
            val screenX = centerX + x3D
            val screenY = centerY + y3D

            // Hotspot Pulsing Pin
            drawCircle(
                color = SleekBlue.copy(alpha = 0.4f),
                radius = 12f,
                center = Offset(screenX, screenY)
            )
            drawCircle(
                color = Color.White,
                radius = 5f,
                center = Offset(screenX, screenY)
            )
            drawCircle(
                color = SleekBlue,
                radius = 5f,
                center = Offset(screenX, screenY),
                style = Stroke(width = 1.5f)
            )

            // Pin Line and Dot
            drawLine(
                color = SleekBlue.copy(alpha = 0.7f),
                start = Offset(screenX, screenY),
                end = Offset(screenX + 16f, screenY - 16f),
                strokeWidth = 1.5f
            )
        }
    }
}
