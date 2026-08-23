package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VisualWidgetType
import com.example.ui.theme.SleekBlack
import com.example.ui.theme.SleekBlue
import com.example.ui.theme.SleekBorderSubtle
import com.example.ui.theme.SleekCardElevated
import com.example.ui.theme.SleekCardSurface
import com.example.ui.theme.SleekGold
import com.example.ui.theme.SleekPurple
import com.example.ui.theme.TextSlate100
import com.example.ui.theme.TextSlate300
import com.example.ui.theme.TextSlate400
import com.example.ui.theme.TextSlate500
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun InteractiveVisualWidget(
    type: VisualWidgetType,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SleekCardElevated)
            .border(1.dp, SleekBorderSubtle, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        when (type) {
            VisualWidgetType.EVENT_HORIZON -> EventHorizonWidget()
            VisualWidgetType.GRAVITATIONAL_TIME_DILATION -> GravitationalDilationVisualWidget()
            VisualWidgetType.NEUTRON_STAR_STRUCTURE -> NeutronStarVisualWidget()
            VisualWidgetType.TON_618_SUPERMASSIVE -> Ton618ScaleWidget()
            VisualWidgetType.STELLAR_EVOLUTION -> StellarEvolutionWidget()
            VisualWidgetType.GALAXY_COLLISION -> GalaxyCollisionWidget()
            VisualWidgetType.LIGHT_TRAVEL_CALCULATOR -> LightTravelVisualWidget()
            VisualWidgetType.GALAXY_SCALE_COMPARISON -> ScaleComparisonWidget()
            VisualWidgetType.VOYAGER_DISTANCE -> VoyagerTelemetryWidget()
            VisualWidgetType.SOLAR_SYSTEM_ORBITS -> SolarSystemOrbitWidget()
            VisualWidgetType.GENERIC_CELESTIAL, VisualWidgetType.NONE -> GenericCosmicDiagramWidget()
        }
    }
}

// 1. Interactive Black Hole Event Horizon Visualizer
@Composable
fun EventHorizonWidget() {
    var solarMasses by remember { mutableFloatStateOf(10f) } // Solar masses
    val infiniteTransition = rememberInfiniteTransition(label = "AccretionDiskSpin")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Restart),
        label = "angle"
    )

    val rsKm = 2.95f * solarMasses

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SCHWARZSCHILD HORIZON SIMULATION",
                color = SleekBlue,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "${solarMasses.toInt()} M☉",
                color = SleekGold,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Canvas Simulation of Shadow, Photon Sphere & Lensed Accretion Disc
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SleekBlack)
                .border(0.5.dp, SleekBorderSubtle, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(160.dp)) {
                val center = Offset(size.width / 2, size.height / 2)
                val baseRadius = 18f + (solarMasses / 100f) * 22f

                // Outer Accretion Glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF9E0B).copy(alpha = 0.45f),
                            Color(0xFFE11D48).copy(alpha = 0.15f),
                            Color.Transparent
                        ),
                        center = center,
                        radius = baseRadius * 3.2f
                    ),
                    radius = baseRadius * 3.2f,
                    center = center
                )

                // Lensed Upper & Lower Accretion Arc (Einstein Ring)
                drawArc(
                    brush = Brush.sweepGradient(
                        listOf(Color(0xFFFDE047), Color(0xFFF97316), Color(0xFFE11D48), Color(0xFFFDE047))
                    ),
                    startAngle = angle,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(center.x - baseRadius * 2.4f, center.y - baseRadius * 1.2f),
                    size = Size(baseRadius * 4.8f, baseRadius * 2.4f),
                    style = Stroke(width = 6f)
                )

                // Photon Sphere (at 1.5 r_s)
                drawCircle(
                    color = Color(0xFF60A5FA).copy(alpha = 0.6f),
                    radius = baseRadius * 1.5f,
                    center = center,
                    style = Stroke(width = 1.5f, pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(8f, 6f)))
                )

                // Black Hole Shadow (Event Horizon)
                drawCircle(
                    color = Color.Black,
                    radius = baseRadius,
                    center = center
                )

                // Thin Starlight boundary
                drawCircle(
                    color = Color(0xFFFCD34D).copy(alpha = 0.8f),
                    radius = baseRadius,
                    center = center,
                    style = Stroke(width = 1.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Interactive Mass Slider
        Text(
            text = "Adjust Black Hole Mass ($solarMasses M☉):",
            color = TextSlate400,
            fontSize = 11.sp
        )
        Slider(
            value = solarMasses,
            onValueChange = { solarMasses = it },
            valueRange = 3f..100f,
            colors = SliderDefaults.colors(
                thumbColor = SleekBlue,
                activeTrackColor = SleekBlue,
                inactiveTrackColor = SleekCardSurface
            ),
            modifier = Modifier.height(32.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "r_s = 2GM/c²: ${String.format("%.1f", rsKm)} km",
                color = TextSlate100,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Photon Orbit: ${String.format("%.1f", rsKm * 1.5f)} km",
                color = SleekBlue,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// 2. Gravitational Time Dilation Visualizer
@Composable
fun GravitationalDilationVisualWidget() {
    var proximityFraction by remember { mutableFloatStateOf(0.7f) } // 0 = far, 1 = near horizon
    var travelerTime by remember { mutableDoubleStateOf(0.0) }
    var observerTime by remember { mutableDoubleStateOf(0.0) }

    // Ratio r_s / r
    val ratio = proximityFraction * 0.95f
    val dilationFactor = if (ratio < 0.999f) 1.0 / sqrt(1.0 - ratio.toDouble()) else 100.0

    LaunchedEffect(dilationFactor) {
        while (true) {
            delay(100)
            travelerTime += 0.1
            observerTime += 0.1 * dilationFactor
        }
    }

    Column {
        Text(
            text = "GENERAL RELATIVISTIC TIME DILATION",
            color = SleekPurple,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Dual Synchronized Clocks
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Near Horizon Clock (Traveler)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SleekBlack)
                    .border(1.dp, SleekPurple.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .padding(12.dp)
            ) {
                Column {
                    Text(text = "🚀 TRAVELER (NEAR MASS)", color = SleekPurple, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${String.format("%.1f", travelerTime)} s",
                        color = TextSlate100,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Ticks at proper time", color = TextSlate500, fontSize = 10.sp)
                }
            }

            // Flat Deep Space Clock (Observer)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SleekBlack)
                    .border(1.dp, SleekBlue.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .padding(12.dp)
            ) {
                Column {
                    Text(text = "🌍 DEEP SPACE OBSERVER", color = SleekBlue, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${String.format("%.1f", observerTime)} s",
                        color = SleekGold,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Dilated ${String.format("%.2f", dilationFactor)}x faster",
                        color = TextSlate400,
                        fontSize = 10.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Proximity to Event Horizon: ${(proximityFraction * 100).toInt()}%",
            color = TextSlate400,
            fontSize = 11.sp
        )
        Slider(
            value = proximityFraction,
            onValueChange = { proximityFraction = it },
            valueRange = 0.05f..0.98f,
            colors = SliderDefaults.colors(
                thumbColor = SleekPurple,
                activeTrackColor = SleekPurple,
                inactiveTrackColor = SleekCardSurface
            ),
            modifier = Modifier.height(32.dp)
        )

        Text(
            text = "Formula: Δt_obs = Δt_proper / √(1 - 2GM / rc²)",
            color = TextSlate500,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

// 3. Neutron Star Internal Cross-Section Widget
@Composable
fun NeutronStarVisualWidget() {
    var selectedLayer by remember { mutableIntStateOf(1) } // 0=Atmosphere, 1=Crust/Pasta, 2=Superfluid Core

    val infiniteTransition = rememberInfiniteTransition(label = "PulsarJet")
    val jetPulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "jetPulse"
    )

    Column {
        Text(
            text = "NEUTRON STAR & PULSAR ANATOMY",
            color = SleekBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SleekBlack)
                .border(0.5.dp, SleekBorderSubtle, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(150.dp)) {
                val center = Offset(size.width / 2, size.height / 2)

                // Polar Magnetic Jet Beams
                drawLine(
                    brush = Brush.linearGradient(listOf(Color(0xFF38BDF8).copy(alpha = jetPulse), Color.Transparent)),
                    start = center,
                    end = Offset(center.x, 0f),
                    strokeWidth = 8f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    brush = Brush.linearGradient(listOf(Color(0xFF38BDF8).copy(alpha = jetPulse), Color.Transparent)),
                    start = center,
                    end = Offset(center.x, size.height),
                    strokeWidth = 8f,
                    cap = StrokeCap.Round
                )

                // Outer Crust (Iron-56 Crystal Lattice)
                drawCircle(
                    color = Color(0xFF64748B),
                    radius = 48f,
                    center = center
                )

                // Inner Crust ("Nuclear Pasta" transition)
                drawCircle(
                    color = Color(0xFF0284C7),
                    radius = 36f,
                    center = center
                )

                // Superfluid Neutron Core
                drawCircle(
                    color = Color(0xFF38BDF8),
                    radius = 22f,
                    center = center
                )

                // Magnetic Field Ring Lines
                drawArc(
                    color = Color(0xFF818CF8).copy(alpha = 0.5f),
                    startAngle = 45f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(center.x - 65f, center.y - 65f),
                    size = Size(130f, 130f),
                    style = Stroke(width = 1.5f)
                )
                drawArc(
                    color = Color(0xFF818CF8).copy(alpha = 0.5f),
                    startAngle = 225f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(center.x - 65f, center.y - 65f),
                    size = Size(130f, 130f),
                    style = Stroke(width = 1.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Layer selection buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val layers = listOf("Outer Crust", "Nuclear Pasta", "Neutron Core")
            layers.forEachIndexed { index, name ->
                val isSel = selectedLayer == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSel) SleekBlue.copy(alpha = 0.25f) else SleekCardSurface)
                        .border(1.dp, if (isSel) SleekBlue else SleekBorderSubtle, RoundedCornerShape(8.dp))
                        .clickable { selectedLayer = index }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name,
                        color = if (isSel) TextSlate100 else TextSlate400,
                        fontSize = 10.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val desc = when (selectedLayer) {
            0 -> "Outer Crust (0.3 km): Solid crystalline iron lattice compressed to 10⁹ g/cm³."
            1 -> "Nuclear Pasta (0.5 km): Intense pressure deforms protons and neutrons into sheets & tubes."
            else -> "Superfluid Core (~10 km): Superconducting protons & superfluid neutrons at 10¹⁵ g/cm³."
        }

        Text(text = desc, color = TextSlate300, fontSize = 11.sp)
    }
}

// 4. TON 618 vs Solar System Supermassive Comparison
@Composable
fun Ton618ScaleWidget() {
    Column {
        Text(
            text = "TON 618 HYPERMASSIVE SCALE COMPARISON",
            color = SleekGold,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SleekBlack)
                .border(0.5.dp, SleekBorderSubtle, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(140.dp)) {
                val center = Offset(size.width / 2, size.height / 2)

                // TON 618 Event Horizon
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.Black, Color(0xFFB45309).copy(alpha = 0.6f)),
                        center = center,
                        radius = 65f
                    ),
                    radius = 65f,
                    center = center
                )
                drawCircle(
                    color = Color(0xFFF59E0B),
                    radius = 65f,
                    center = center,
                    style = Stroke(width = 2f)
                )

                // Neptune Orbit (30 AU) scale dot
                drawCircle(
                    color = Color(0xFF38BDF8),
                    radius = 3.5f,
                    center = center,
                    style = Stroke(width = 1f)
                )

                // Sun dot (invisible to true scale, shown as pixel)
                drawCircle(
                    color = Color.White,
                    radius = 1.5f,
                    center = center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "🟠 TON 618: 390 Billion km (2,600 AU)", color = SleekGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(text = "🔵 Neptune Orbit: ~9 Billion km (60 AU)", color = SleekBlue, fontSize = 10.sp)
        }
    }
}

// 5. Interactive Stellar Evolution Lifecycle
@Composable
fun StellarEvolutionWidget() {
    var stageIndex by remember { mutableIntStateOf(1) } // 0=Nebula, 1=Main Sequence, 2=Red Giant, 3=Supernova/Remnant
    val stages = listOf("Stellar Nebula", "Main Sequence (Now)", "Red Giant", "Supernova / Remnant")

    Column {
        Text(
            text = "STELLAR LIFECYCLE TIMELINE",
            color = SleekBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            stages.forEachIndexed { index, name ->
                val isSelected = stageIndex == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) SleekBlue.copy(alpha = 0.3f) else SleekCardSurface)
                        .border(1.dp, if (isSelected) SleekBlue else SleekBorderSubtle, RoundedCornerShape(6.dp))
                        .clickable { stageIndex = index }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "P${index + 1}",
                        color = if (isSelected) TextSlate100 else TextSlate400,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val (title, info, color) = when (stageIndex) {
            0 -> Triple("Stellar Nebula (T = 0)", "Gravitational collapse of interstellar hydrogen gas clouds forming a spinning protostar.", Color(0xFF818CF8))
            1 -> Triple("Main Sequence (0 – 10 Gyr)", "Hydrostatic equilibrium: Hydrogen fusion in core balances gravitational collapse. (Current Sun state).", Color(0xFFFBBF24))
            2 -> Triple("Red Giant Expansion (~10 – 11 Gyr)", "Core hydrogen depleted; helium flash triggers envelope expansion beyond Earth's orbit.", Color(0xFFEF4444))
            else -> Triple("Final Remnant (11+ Gyr)", "Planetary nebula ejection leaving an ultra-dense carbon-oxygen White Dwarf (or Black Hole for >20 M☉).", Color(0xFF38BDF8))
        }

        Text(text = title, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = info, color = TextSlate300, fontSize = 11.sp, lineHeight = 16.sp)
    }
}

// 6. Galaxy Collision Simulation Widget
@Composable
fun GalaxyCollisionWidget() {
    val infiniteTransition = rememberInfiniteTransition(label = "CollisionAnim")
    val progress by infiniteTransition.animateFloat(
        initialValue = -50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(tween(3500, easing = LinearEasing), RepeatMode.Reverse),
        label = "progress"
    )

    Column {
        Text(
            text = "MILKY WAY - ANDROMEDA MERGER (T + 4.5 GYR)",
            color = SleekPurple,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SleekBlack)
                .border(0.5.dp, SleekBorderSubtle, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                val centerY = size.height / 2
                val midX = size.width / 2

                val mwX = midX - 60f + progress * 0.8f
                val andromedaX = midX + 60f - progress * 0.8f

                // Milky Way Spiral
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF38BDF8), Color.Transparent),
                        center = Offset(mwX, centerY),
                        radius = 45f
                    ),
                    radius = 45f,
                    center = Offset(mwX, centerY)
                )

                // Andromeda Spiral
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFC084FC), Color.Transparent),
                        center = Offset(andromedaX, centerY),
                        radius = 55f
                    ),
                    radius = 55f,
                    center = Offset(andromedaX, centerY)
                )

                // Central cores
                drawCircle(color = Color.White, radius = 4f, center = Offset(mwX, centerY))
                drawCircle(color = Color(0xFFFDE047), radius = 5f, center = Offset(andromedaX, centerY))
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Stars do not collide directly due to light-years of empty space; tidal gravity merges the galaxies into 'Milkdromeda'.",
            color = TextSlate400,
            fontSize = 10.sp
        )
    }
}

// 7. Light Travel Visual Calculator
@Composable
fun LightTravelVisualWidget() {
    var selectedTarget by remember { mutableIntStateOf(0) }
    val targets = listOf(
        Triple("Moon", "1.28 Light-Seconds", 384400.0),
        Triple("Sun", "8.33 Light-Minutes", 149600000.0),
        Triple("Mars", "3.1 to 22.2 Light-Minutes", 225000000.0),
        Triple("Alpha Centauri", "4.37 Light-Years", 4.134e13)
    )

    Column {
        Text(
            text = "PHOTON PROPAGATION DELAYS",
            color = SleekBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            targets.forEachIndexed { index, (name, _, _) ->
                val isSel = selectedTarget == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSel) SleekBlue.copy(alpha = 0.3f) else SleekCardSurface)
                        .border(1.dp, if (isSel) SleekBlue else SleekBorderSubtle, RoundedCornerShape(6.dp))
                        .clickable { selectedTarget = index }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name,
                        color = if (isSel) TextSlate100 else TextSlate400,
                        fontSize = 9.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val current = targets[selectedTarget]
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Earth ➔ ${current.first}", color = TextSlate100, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(text = current.second, color = SleekGold, fontSize = 12.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
        }
    }
}

// 8. Scale Comparison Visual
@Composable
fun ScaleComparisonWidget() {
    Column {
        Text(
            text = "COSMIC SCALES RELATIVE MATRIX",
            color = SleekGold,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        val scales = listOf(
            Triple("Earth Diameter", "12,742 km", 0.05f),
            Triple("Sun Diameter", "1,392,700 km (109 Earths)", 0.25f),
            Triple("Solar System (to Neptune)", "9 Billion km (60 AU)", 0.55f),
            Triple("Milky Way Galaxy", "100,000 Light-Years", 0.85f),
            Triple("Observable Universe", "93 Billion Light-Years", 1.0f)
        )

        scales.forEach { (label, value, fraction) ->
            Column(modifier = Modifier.padding(vertical = 3.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = label, color = TextSlate300, fontSize = 10.sp)
                    Text(text = value, color = SleekBlue, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(SleekBlack)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction)
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(Brush.horizontalGradient(listOf(SleekBlue, SleekPurple)))
                    )
                }
            }
        }
    }
}

// 9. Voyager 1 Telemetry Widget
@Composable
fun VoyagerTelemetryWidget() {
    Column {
        Text(
            text = "VOYAGER 1 LIVE INTERSTELLAR TELEMETRY",
            color = SleekBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SleekBlack)
                    .padding(8.dp)
            ) {
                Column {
                    Text(text = "DISTANCE FROM SUN", color = TextSlate500, fontSize = 9.sp)
                    Text(text = "162.5 AU (24.3B km)", color = TextSlate100, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SleekBlack)
                    .padding(8.dp)
            ) {
                Column {
                    Text(text = "ONE-WAY LIGHT TIME", color = TextSlate500, fontSize = 9.sp)
                    Text(text = "22 Hours 32 Min", color = SleekGold, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// 10. Solar System Orbits Widget
@Composable
fun SolarSystemOrbitWidget() {
    val infiniteTransition = rememberInfiniteTransition(label = "OrbitAnim")
    val orbitAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing), RepeatMode.Restart),
        label = "orbitAngle"
    )

    Column {
        Text(
            text = "KEPLERIAN ORBITAL VELOCITIES",
            color = SleekBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SleekBlack),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(110.dp)) {
                val center = Offset(size.width / 2, size.height / 2)

                // Sun
                drawCircle(color = Color(0xFFFBBF24), radius = 8f, center = center)

                // Orbit 1: Mercury (fast)
                val mercR = 22f
                val mercA = (orbitAngle * 4.15f) * (PI / 180f)
                drawCircle(color = Color.White.copy(alpha = 0.2f), radius = mercR, center = center, style = Stroke(1f))
                drawCircle(color = Color(0xFF94A3B8), radius = 3f, center = Offset(center.x + mercR * cos(mercA).toFloat(), center.y + mercR * sin(mercA).toFloat()))

                // Orbit 2: Earth (1 yr)
                val earthR = 42f
                val earthA = (orbitAngle) * (PI / 180f)
                drawCircle(color = Color.White.copy(alpha = 0.2f), radius = earthR, center = center, style = Stroke(1f))
                drawCircle(color = Color(0xFF38BDF8), radius = 4f, center = Offset(center.x + earthR * cos(earthA).toFloat(), center.y + earthR * sin(earthA).toFloat()))
            }
        }
    }
}

// 11. Generic Celestial Diagram Widget
@Composable
fun GenericCosmicDiagramWidget() {
    val infiniteTransition = rememberInfiniteTransition(label = "WaveAnim")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = "pulse"
    )

    Column {
        Text(
            text = "ASTROPHYSICAL PHENOMENON DIAGRAM",
            color = SleekBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SleekBlack),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxWidth().height(90.dp)) {
                val midY = size.height / 2
                val path = Path()
                path.moveTo(0f, midY)
                for (x in 0..size.width.toInt() step 10) {
                    val y = midY + sin((x / 30f) + (pulse * 2f)) * 20f
                    path.lineTo(x.toFloat(), y)
                }
                drawPath(
                    path = path,
                    color = Color(0xFF38BDF8).copy(alpha = pulse),
                    style = Stroke(width = 2.5f)
                )
            }
        }
    }
}
