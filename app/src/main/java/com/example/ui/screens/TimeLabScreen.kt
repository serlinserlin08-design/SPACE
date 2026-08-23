package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.HourglassFull
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.LightTimePresetsData
import com.example.data.model.DistanceUnit
import com.example.data.model.GravityPreset
import com.example.data.model.LightTimePreset
import com.example.data.model.RelativityEngine
import com.example.data.model.VelocityPreset
import com.example.ui.components.CosmicGlassCard
import com.example.ui.theme.SleekBlack
import com.example.ui.theme.SleekBlue
import com.example.ui.theme.SleekBlueDark
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekBorderSubtle
import com.example.ui.theme.SleekCardElevated
import com.example.ui.theme.SleekCardSurface
import com.example.ui.theme.SleekGlassSubtle
import com.example.ui.theme.SleekGold
import com.example.ui.theme.SleekPurple
import com.example.ui.theme.SleekPurpleDark
import com.example.ui.theme.TextSlate100
import com.example.ui.theme.TextSlate300
import com.example.ui.theme.TextSlate400
import com.example.ui.theme.TextSlate500
import com.example.ui.theme.TextSlate600
import com.example.ui.viewmodel.DilationMode
import com.example.ui.viewmodel.LightTimeState
import com.example.ui.viewmodel.TimeDilationState
import com.example.ui.viewmodel.TimeLabTab

@Composable
fun TimeLabScreen(
    currentTab: TimeLabTab,
    onTabSelected: (TimeLabTab) -> Unit,
    lightTimeState: LightTimeState,
    onLightTimeInputChange: (String) -> Unit,
    onLightTimeUnitChange: (DistanceUnit) -> Unit,
    onSelectLightTimePreset: (LightTimePreset) -> Unit,
    timeDilationState: TimeDilationState,
    onSetDilationMode: (DilationMode) -> Unit,
    onSetGravityRatio: (Float) -> Unit,
    onSetVelocityFraction: (Float) -> Unit,
    onSelectGravityPreset: (GravityPreset) -> Unit,
    onSelectVelocityPreset: (VelocityPreset) -> Unit,
    onToggleClockRunning: () -> Unit,
    onResetClock: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Header Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SleekBlueDark.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Science,
                    contentDescription = "Time Lab",
                    tint = SleekBlue,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "RELATIVITY & TIME LAB",
                    color = SleekBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.6.sp
                )
                Text(
                    text = "Interactive Einstein Space-Time Engine",
                    color = TextSlate500,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tab Selector Row
        TabRow(
            selectedTabIndex = if (currentTab == TimeLabTab.TIME_DILATION) 0 else 1,
            containerColor = SleekCardSurface,
            contentColor = SleekBlue,
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, SleekBorderSubtle, RoundedCornerShape(14.dp)),
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[if (currentTab == TimeLabTab.TIME_DILATION) 0 else 1]),
                    color = SleekBlue,
                    height = 2.5.dp
                )
            }
        ) {
            Tab(
                selected = currentTab == TimeLabTab.TIME_DILATION,
                onClick = { onTabSelected(TimeLabTab.TIME_DILATION) },
                text = {
                    Text(
                        text = "Time Dilation",
                        fontWeight = if (currentTab == TimeLabTab.TIME_DILATION) FontWeight.Bold else FontWeight.Medium,
                        color = if (currentTab == TimeLabTab.TIME_DILATION) SleekBlue else TextSlate500,
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier.testTag("tab_time_dilation")
            )
            Tab(
                selected = currentTab == TimeLabTab.LIGHT_TIME,
                onClick = { onTabSelected(TimeLabTab.LIGHT_TIME) },
                text = {
                    Text(
                        text = "Light-Time Calculator",
                        fontWeight = if (currentTab == TimeLabTab.LIGHT_TIME) FontWeight.Bold else FontWeight.Medium,
                        color = if (currentTab == TimeLabTab.LIGHT_TIME) SleekBlue else TextSlate500,
                        fontSize = 13.sp
                    )
                },
                modifier = Modifier.testTag("tab_light_time")
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (currentTab) {
            TimeLabTab.TIME_DILATION -> {
                TimeDilationSimulatorContent(
                    state = timeDilationState,
                    onSetMode = onSetDilationMode,
                    onSetGravityRatio = onSetGravityRatio,
                    onSetVelocityFraction = onSetVelocityFraction,
                    onSelectGravityPreset = onSelectGravityPreset,
                    onSelectVelocityPreset = onSelectVelocityPreset,
                    onToggleClockRunning = onToggleClockRunning,
                    onResetClock = onResetClock
                )
            }
            TimeLabTab.LIGHT_TIME -> {
                LightTimeCalculatorContent(
                    state = lightTimeState,
                    onInputChange = onLightTimeInputChange,
                    onUnitChange = onLightTimeUnitChange,
                    onSelectPreset = onSelectLightTimePreset
                )
            }
        }
    }
}

@Composable
private fun TimeDilationSimulatorContent(
    state: TimeDilationState,
    onSetMode: (DilationMode) -> Unit,
    onSetGravityRatio: (Float) -> Unit,
    onSetVelocityFraction: (Float) -> Unit,
    onSelectGravityPreset: (GravityPreset) -> Unit,
    onSelectVelocityPreset: (VelocityPreset) -> Unit,
    onToggleClockRunning: () -> Unit,
    onResetClock: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Mode Selector: Gravitational vs Special Relativistic
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SleekCardSurface)
                    .border(1.dp, SleekBorderSubtle, RoundedCornerShape(14.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (state.mode == DilationMode.GRAVITATIONAL) SleekPurpleDark
                            else Color.Transparent
                        )
                        .clickable { onSetMode(DilationMode.GRAVITATIONAL) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "General Relativity (Gravity)",
                        color = if (state.mode == DilationMode.GRAVITATIONAL) Color.White else TextSlate400,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (state.mode == DilationMode.VELOCITY) SleekBlueDark.copy(alpha = 0.25f)
                            else Color.Transparent
                        )
                        .clickable { onSetMode(DilationMode.VELOCITY) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Special Relativity (Velocity)",
                        color = if (state.mode == DilationMode.VELOCITY) SleekBlue else TextSlate400,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Live Dual Synchronized Cosmic Clocks
        item {
            CosmicGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("time_dilation_clock_card"),
                borderColor = SleekBorderSubtle,
                backgroundColor = SleekCardSurface,
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (state.isClockRunning) Icons.Default.HourglassFull else Icons.Default.HourglassEmpty,
                                contentDescription = "Clock status",
                                tint = if (state.isClockRunning) SleekBlue else TextSlate500,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "DUAL RELATIVISTIC CLOCKS",
                                color = SleekBlue,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onToggleClockRunning) {
                                Icon(
                                    imageVector = if (state.isClockRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play/Pause",
                                    tint = SleekBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            IconButton(onClick = onResetClock) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reset Clock",
                                    tint = TextSlate500,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Clock Panels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Traveler Clock
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(SleekBlack.copy(alpha = 0.8f))
                                .border(1.dp, SleekBorderSubtle, RoundedCornerShape(14.dp))
                                .padding(12.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (state.mode == DilationMode.GRAVITATIONAL) "TRAVELER / IN GRAVITY" else "TRAVELER / SHIP",
                                    color = TextSlate500,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = formatSeconds(state.travelerElapsedSeconds),
                                    color = TextSlate100,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Ticks at normal rate (1.00x)",
                                    color = SleekBlue,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        // Distant Earth Observer Clock
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(SleekBlack.copy(alpha = 0.8f))
                                .border(
                                    1.dp,
                                    if (state.mode == DilationMode.GRAVITATIONAL) SleekPurple.copy(alpha = 0.35f) else SleekGold.copy(alpha = 0.35f),
                                    RoundedCornerShape(14.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "DISTANT OBSERVER / EARTH",
                                    color = TextSlate500,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = formatSeconds(state.observerElapsedSeconds),
                                    color = if (state.mode == DilationMode.GRAVITATIONAL) SleekPurple else SleekGold,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = String.format("Elapsed at %.2fx rate", state.dilationFactor),
                                    color = if (state.mode == DilationMode.GRAVITATIONAL) SleekPurple else SleekGold,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dilation Multiplier Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SleekGlassSubtle)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Dilation Factor (γ)",
                                color = TextSlate400,
                                fontSize = 12.sp
                            )
                            Text(
                                text = String.format("%.4fx time stretch", state.dilationFactor),
                                color = SleekBlue,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Preset Chips
        item {
            Column {
                Text(
                    text = if (state.mode == DilationMode.GRAVITATIONAL) "GRAVITY WELL PRESETS" else "VELOCITY PRESETS",
                    color = TextSlate500,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (state.mode == DilationMode.GRAVITATIONAL) {
                        RelativityEngine.gravityPresets.forEach { preset ->
                            val isSelected = state.selectedGravityPreset?.id == preset.id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(
                                        if (isSelected) SleekPurpleDark.copy(alpha = 0.35f)
                                        else SleekCardSurface
                                    )
                                    .border(
                                        width = if (isSelected) 1.dp else 0.5.dp,
                                        color = if (isSelected) SleekPurple else SleekBorderSubtle,
                                        shape = RoundedCornerShape(50.dp)
                                    )
                                    .clickable { onSelectGravityPreset(preset) }
                                    .padding(horizontal = 14.dp, vertical = 7.dp)
                            ) {
                                Text(
                                    text = preset.name,
                                    color = if (isSelected) Color.White else TextSlate400,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    } else {
                        RelativityEngine.velocityPresets.forEach { preset ->
                            val isSelected = state.selectedVelocityPreset?.id == preset.id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(
                                        if (isSelected) SleekBlueDark.copy(alpha = 0.25f)
                                        else SleekCardSurface
                                    )
                                    .border(
                                        width = if (isSelected) 1.dp else 0.5.dp,
                                        color = if (isSelected) SleekBlue else SleekBorderSubtle,
                                        shape = RoundedCornerShape(50.dp)
                                    )
                                    .clickable { onSelectVelocityPreset(preset) }
                                    .padding(horizontal = 14.dp, vertical = 7.dp)
                            ) {
                                Text(
                                    text = preset.name,
                                    color = if (isSelected) SleekBlue else TextSlate400,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Interactive Precision Slider
        item {
            CosmicGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = SleekBorderSubtle,
                backgroundColor = SleekCardSurface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (state.mode == DilationMode.GRAVITATIONAL) "Gravity Potential (rs / r)" else "Velocity Fraction (v / c)",
                            color = TextSlate100,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (state.mode == DilationMode.GRAVITATIONAL) String.format("%.3f", state.gravityRatio) else String.format("%.4f c", state.velocityFraction),
                            color = SleekBlue,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    if (state.mode == DilationMode.GRAVITATIONAL) {
                        Slider(
                            value = state.gravityRatio,
                            onValueChange = onSetGravityRatio,
                            valueRange = 0f..0.98f,
                            colors = SliderDefaults.colors(
                                thumbColor = SleekPurple,
                                activeTrackColor = SleekPurple,
                                inactiveTrackColor = SleekCardElevated
                            )
                        )
                    } else {
                        Slider(
                            value = state.velocityFraction,
                            onValueChange = onSetVelocityFraction,
                            valueRange = 0f..0.9999f,
                            colors = SliderDefaults.colors(
                                thumbColor = SleekBlue,
                                activeTrackColor = SleekBlue,
                                inactiveTrackColor = SleekCardElevated
                            )
                        )
                    }
                }
            }
        }

        // Plain English Explanation Breakdown Card
        item {
            CosmicGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = SleekBorderSubtle,
                backgroundColor = SleekCardSurface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = "Explanation",
                            tint = SleekGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "WHAT THIS MEANS IN REALITY",
                            color = SleekGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    val explanation = if (state.mode == DilationMode.GRAVITATIONAL) {
                        state.selectedGravityPreset?.humanComparison
                            ?: "At this gravitational depth (rs/r = ${String.format("%.3f", state.gravityRatio)}), every 1 hour spent by the traveler corresponds to ${(state.dilationFactor).toInt()} hours and ${((state.dilationFactor % 1) * 60).toInt()} minutes elapsed on Earth."
                    } else {
                        state.selectedVelocityPreset?.humanComparison
                            ?: "Traveling at ${(state.velocityFraction * 100).toInt()}% light speed means your internal clock runs ${(state.dilationFactor).toInt()}x slower relative to observers resting on Earth."
                    }

                    Text(
                        text = explanation,
                        color = TextSlate300,
                        fontSize = 12.sp,
                        lineHeight = 19.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun LightTimeCalculatorContent(
    state: LightTimeState,
    onInputChange: (String) -> Unit,
    onUnitChange: (DistanceUnit) -> Unit,
    onSelectPreset: (LightTimePreset) -> Unit
) {
    var unitMenuExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Preset Celestial Destinations Carousel
        item {
            Column {
                Text(
                    text = "FAMOUS COSMIC DESTINATIONS",
                    color = TextSlate500,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LightTimePresetsData.presets.forEach { preset ->
                        val isSelected = state.selectedPreset?.id == preset.id
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isSelected) SleekBlueDark.copy(alpha = 0.22f)
                                    else SleekCardSurface
                                )
                                .border(
                                    width = if (isSelected) 1.dp else 0.5.dp,
                                    color = if (isSelected) SleekBlue else SleekBorderSubtle,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { onSelectPreset(preset) }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Column {
                                Text(
                                    text = preset.name,
                                    color = if (isSelected) SleekBlue else TextSlate100,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = preset.distanceDisplay,
                                    color = TextSlate500,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Custom Distance Input Card
        item {
            CosmicGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = SleekBorderSubtle,
                backgroundColor = SleekCardSurface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ENTER CUSTOM DISTANCE",
                        color = SleekBlue,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = state.inputValue,
                            onValueChange = onInputChange,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("light_time_distance_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            placeholder = { Text("e.g. 2500000", color = TextSlate600, fontSize = 13.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = SleekBlack,
                                unfocusedContainerColor = SleekBlack,
                                focusedBorderColor = SleekBlue,
                                unfocusedBorderColor = SleekBorderSubtle,
                                focusedTextColor = TextSlate100,
                                unfocusedTextColor = TextSlate100
                            )
                        )

                        // Unit Selector Dropdown
                        Box {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(SleekCardElevated)
                                    .border(1.dp, SleekBorderSubtle, RoundedCornerShape(14.dp))
                                    .clickable { unitMenuExpanded = true }
                                    .padding(horizontal = 14.dp, vertical = 14.dp)
                            ) {
                                Text(
                                    text = state.selectedUnit.symbol,
                                    color = SleekBlue,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            DropdownMenu(
                                expanded = unitMenuExpanded,
                                onDismissRequest = { unitMenuExpanded = false }
                            ) {
                                DistanceUnit.entries.forEach { unit ->
                                    DropdownMenuItem(
                                        text = { Text("${unit.label} (${unit.symbol})") },
                                        onClick = {
                                            onUnitChange(unit)
                                            unitMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Computed Light Travel Result Card
        item {
            CosmicGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("light_time_result_card"),
                borderColor = SleekBorderSubtle,
                backgroundColor = SleekCardElevated
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "LIGHT TRAVEL TIME TO REACH EARTH",
                        color = SleekBlue,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = state.formattedDuration.ifBlank { "—" },
                        color = TextSlate100,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Speed of light (c) ≈ 299,792.458 km/s",
                        color = TextSlate500,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Earth Historical Anchor Card
        item {
            CosmicGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = SleekBorderSubtle,
                backgroundColor = SleekCardSurface
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "EARTH HISTORICAL ANCHOR",
                        color = SleekPurple,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "When this light first left its source:",
                        color = TextSlate400,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = state.earthEra.ifBlank { "Calculating historical epoch..." },
                        color = TextSlate100,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Scientific Note Card (if preset selected)
        if (state.selectedPreset != null) {
            item {
                CosmicGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = SleekBorderSubtle,
                    backgroundColor = SleekCardSurface
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "ASTRONOMICAL CONTEXT",
                            color = SleekGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = state.selectedPreset.scientificNote,
                            color = TextSlate400,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

private fun formatSeconds(sec: Double): String {
    val totalSeconds = sec.toInt()
    val minutes = totalSeconds / 60
    val remainderSeconds = totalSeconds % 60
    val tenths = ((sec - totalSeconds) * 10).toInt()
    return String.format("%02d:%02d.%d", minutes, remainderSeconds, tenths)
}
