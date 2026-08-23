package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.datasource.SpaceAssistantOfflineKnowledge
import com.example.data.model.AssistantMessage
import com.example.data.model.EpistemicStatus
import com.example.data.model.SpaceImageStyle
import com.example.data.model.SpaceVisualClassification
import com.example.data.model.VisualWidgetType
import com.example.ui.components.InteractiveVisualWidget
import com.example.ui.theme.SleekBlack
import com.example.ui.theme.SleekBlue
import com.example.ui.theme.SleekBlueDark
import com.example.ui.theme.SleekBorderSubtle
import com.example.ui.theme.SleekCardElevated
import com.example.ui.theme.SleekCardSurface
import com.example.ui.theme.SleekGold
import com.example.ui.theme.SleekPurple
import com.example.ui.theme.TextSlate100
import com.example.ui.theme.TextSlate300
import com.example.ui.theme.TextSlate400
import com.example.ui.theme.TextSlate500
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpaceAssistantScreen(
    messages: List<AssistantMessage>,
    isLoading: Boolean,
    onSendMessage: (String) -> Unit,
    onRegenerateImageStyle: ((messageId: String, style: SpaceImageStyle) -> Unit)? = null,
    onOpen3DViewer: ((String) -> Unit)? = null,
    onClearChat: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var selectedFullscreenImage by remember { mutableStateOf<AssistantMessage?>(null) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(messages.size, isLoading) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("space_assistant_screen")
    ) {
        // Top Subheader with Clear & Status Indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SleekCardSurface)
                        .border(1.dp, SleekBlue.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_cosmoai_logo),
                        contentDescription = "COSMOAI Official Logo",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "COSMOAI ASTROPHYSICS ASSISTANT",
                        color = SleekBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.1.sp
                    )
                    Text(
                        text = "Scientific Reasoning • 3D Visualizer • AI Space Images",
                        color = TextSlate500,
                        fontSize = 10.sp
                    )
                }
            }

            if (messages.isNotEmpty()) {
                IconButton(
                    onClick = onClearChat,
                    modifier = Modifier.testTag("clear_chat_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Clear Conversation",
                        tint = TextSlate400,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Message Feed
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    AssistantWelcomeCard(onSelectPrompt = {
                        inputText = it
                        onSendMessage(it)
                        inputText = ""
                    })
                }
            }

            items(messages, key = { it.id }) { message ->
                if (message.isUser) {
                    UserMessageBubble(text = message.text)
                } else {
                    AssistantRichResponseCard(
                        message = message,
                        onFollowUpSelected = {
                            inputText = it
                            onSendMessage(it)
                            inputText = ""
                        },
                        onRegenerateStyle = { style ->
                            onRegenerateImageStyle?.invoke(message.id, style)
                        },
                        onOpenFullscreen = {
                            selectedFullscreenImage = message
                        },
                        onOpen3DViewer = { target ->
                            onOpen3DViewer?.invoke(target)
                        },
                        onShare = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "✨ Space Assistant: ${message.visualTitle ?: "Cosmic Insight"}\n\n${message.directAnswer}\n\n${message.deepExplanation}\n\n🔭 Sources: ${message.sourcesCited.joinToString(", ")}"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Astrophysics Answer"))
                        },
                        onCopy = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Cosmic Answer", "${message.directAnswer}\n\n${message.deepExplanation}")
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            if (isLoading) {
                item {
                    AssistantLoadingBubble()
                }
            }
        }

        // Quick Starter Suggestions Bar (Horizontal Pill Scroll)
        if (messages.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val actionStarters = listOf(
                    "🎨 Generate a realistic black hole",
                    "🎬 Create TON 618 visualization",
                    "🔭 Show me what a quasar looks like",
                    "🪐 Generate an exoplanet with huge rings",
                    "💫 Generate a galaxy collision",
                    "📐 Show me a neutron star"
                )
                items(actionStarters) { prompt ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(SleekCardSurface)
                            .border(0.5.dp, SleekBorderSubtle, RoundedCornerShape(20.dp))
                            .clickable {
                                onSendMessage(prompt)
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = prompt,
                            color = TextSlate300,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Input Field & Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .weight(1f)
                    .testTag("assistant_query_input"),
                placeholder = {
                    Text(
                        text = "Ask or say 'Generate a black hole / TON 618'...",
                        color = TextSlate500,
                        fontSize = 13.sp
                    )
                },
                maxLines = 3,
                textStyle = TextStyle(color = TextSlate100, fontSize = 13.sp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SleekCardSurface,
                    unfocusedContainerColor = SleekCardSurface,
                    focusedBorderColor = SleekBlue,
                    unfocusedBorderColor = SleekBorderSubtle,
                    cursorColor = SleekBlue
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    if (inputText.isNotBlank() && !isLoading) {
                        onSendMessage(inputText.trim())
                        inputText = ""
                    }
                })
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        if (inputText.isNotBlank() && !isLoading) Brush.linearGradient(listOf(SleekBlue, SleekPurple))
                        else Brush.linearGradient(listOf(SleekCardSurface, SleekCardSurface))
                    )
                    .clickable(enabled = inputText.isNotBlank() && !isLoading) {
                        onSendMessage(inputText.trim())
                        inputText = ""
                    }
                    .testTag("assistant_send_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = if (inputText.isNotBlank() && !isLoading) Color.White else TextSlate500,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    // Fullscreen Image Zoom Dialog
    selectedFullscreenImage?.let { msg ->
        FullscreenImageInspectionDialog(
            message = msg,
            onDismiss = { selectedFullscreenImage = null },
            onShare = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "🌌 AI-Generated Astrophysical Visualization: ${msg.visualTitle ?: "Cosmic Object"}\n\nPrompt: ${msg.generatedImagePrompt}\n\nClassification: ${msg.imageClassification.badge}\n${msg.imageClassification.disclaimer}"
                    )
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share Cosmic Image"))
            }
        )
    }
}

// User Query Bubble
@Composable
fun UserMessageBubble(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .clip(RoundedCornerShape(18.dp, 18.dp, 4.dp, 18.dp))
                .background(Brush.linearGradient(listOf(SleekBlueDark, Color(0xFF1E1B4B))))
                .border(1.dp, SleekBlue.copy(alpha = 0.5f), RoundedCornerShape(18.dp, 18.dp, 4.dp, 18.dp))
                .padding(14.dp)
        ) {
            Text(
                text = text,
                color = TextSlate100,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Assistant Rich Response Card with Interactive Visuals, Image Generator, Telemetry & References
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AssistantRichResponseCard(
    message: AssistantMessage,
    onFollowUpSelected: (String) -> Unit,
    onRegenerateStyle: (SpaceImageStyle) -> Unit,
    onOpenFullscreen: () -> Unit,
    onOpen3DViewer: ((String) -> Unit)? = null,
    onShare: () -> Unit,
    onCopy: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SleekCardSurface)
            .border(1.dp, SleekBorderSubtle, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        // Epistemic Classification Badge & Actions Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(SleekNavy)
                        .border(0.5.dp, SleekBlue.copy(alpha = 0.4f), RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_cosmoai_logo),
                        contentDescription = "COSMOAI Logo",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(6.dp)),
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                EpistemicBadge(status = message.epistemicStatus)
            }

            Row {
                IconButton(onClick = onCopy, modifier = Modifier.size(30.dp)) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = TextSlate400,
                        modifier = Modifier.size(16.dp)
                    )
                }
                IconButton(onClick = onShare, modifier = Modifier.size(30.dp)) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = SleekBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Direct Answer (Highlighted Lead)
        message.directAnswer?.let { direct ->
            Text(
                text = direct,
                color = TextSlate100,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 21.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // --- PROMINENT "🌌 VIEW IN 3D" BUTTON ---
        val target3dQuery = message.space3DQueryTarget
            ?: message.visualTitle
            ?: message.directAnswer?.take(40)
            ?: "Space Object"

        Button(
            onClick = { onOpen3DViewer?.invoke(target3dQuery) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(SleekBlue, SleekPurple, Color(0xFF38BDF8))
                    )
                )
                .testTag("assistant_view_in_3d_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🌌 VIEW IN 3D",
                    color = Color.White,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- AI GENERATED SPACE IMAGE COMPONENT ---
        if (message.generatedImageBase64 != null || message.isImageGenerating || message.imageGenerationError != null) {
            GeneratedSpaceImageCard(
                message = message,
                onRegenerateStyle = onRegenerateStyle,
                onOpenFullscreen = onOpenFullscreen
            )
            Spacer(modifier = Modifier.height(12.dp))
        } else {
            // "Generate AI Space Visualization" affordance for text-only answers
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(SleekBlue.copy(alpha = 0.12f), SleekPurple.copy(alpha = 0.12f))))
                    .border(0.5.dp, SleekBlue.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    .clickable { onRegenerateStyle(SpaceImageStyle.PHOTOREALISTIC) }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Generate Visual",
                            tint = SleekBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Generate AI Space Visualization",
                            color = SleekBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "7 Styles Available ❯",
                        color = TextSlate400,
                        fontSize = 10.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Interactive Visual Simulation Widget (if applicable)
        if (message.visualType != VisualWidgetType.NONE) {
            InteractiveVisualWidget(
                type = message.visualType,
                modifier = Modifier.fillMaxWidth()
            )

            // Visual Label & Source Attribution
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                message.visualCaption?.let { caption ->
                    Text(
                        text = "🔬 $caption",
                        color = TextSlate400,
                        fontSize = 10.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
                message.visualSourceType?.let { src ->
                    Text(
                        text = "[$src]",
                        color = SleekBlue,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // 6-Point / 4-Point Scientific Telemetry Highlights
        if (message.telemetryHighlights.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                message.telemetryHighlights.forEach { tele ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(SleekBlack)
                            .border(0.5.dp, SleekBorderSubtle, RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Column {
                            Text(text = tele.label, color = TextSlate500, fontSize = 9.sp, fontWeight = FontWeight.Medium)
                            Text(text = tele.value, color = TextSlate100, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                            if (tele.unitOrContext.isNotBlank()) {
                                Text(text = tele.unitOrContext, color = SleekBlue, fontSize = 9.sp)
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Deeper Scientific Breakdown
        message.deepExplanation?.let { deep ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SleekBlack.copy(alpha = 0.6f))
                    .border(0.5.dp, SleekBorderSubtle, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = deep,
                    color = TextSlate300,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Intuitive Distance / Scale Comparison Anchor
        message.distanceIntuition?.let { intuition ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SleekGold.copy(alpha = 0.08f))
                    .border(0.5.dp, SleekGold.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Intuition",
                    tint = SleekGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = intuition,
                    color = SleekGold,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Authoritative References Cited
        if (message.sourcesCited.isNotEmpty()) {
            Text(
                text = "Authoritative Sources: ${message.sourcesCited.joinToString(" • ")}",
                color = TextSlate500,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Follow-Up Question Chips
        if (message.followUpQuestions.isNotEmpty()) {
            Text(
                text = "CONTINUE EXPLORING",
                color = TextSlate500,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                message.followUpQuestions.forEach { followUp ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(SleekBlue.copy(alpha = 0.12f))
                            .border(0.5.dp, SleekBlue.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                            .clickable { onFollowUpSelected(followUp) }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = followUp,
                            color = SleekBlue,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

// AI Generated Space Image Card Component
@Composable
fun GeneratedSpaceImageCard(
    message: AssistantMessage,
    onRegenerateStyle: (SpaceImageStyle) -> Unit,
    onOpenFullscreen: () -> Unit
) {
    val context = LocalContext.current
    var isPromptExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SleekBlack)
            .border(1.dp, SleekBlue.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        // Image Header with Mandatory Scientific Classification Badge & Disclaimers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF312E81).copy(alpha = 0.6f))
                        .border(0.5.dp, SleekBlue, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "🎨 ${message.imageClassification.badge.uppercase()}",
                        color = SleekBlue,
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.6.sp
                    )
                }
                Text(
                    text = message.imageClassification.disclaimer,
                    color = TextSlate500,
                    fontSize = 8.5.sp,
                    lineHeight = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Fullscreen Zoom Button
            if (message.generatedImageBase64 != null) {
                IconButton(
                    onClick = onOpenFullscreen,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Fullscreen,
                        contentDescription = "Expand Fullscreen",
                        tint = TextSlate300,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Image View Container (16:9 Aspect Ratio)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF030712))
                .border(0.5.dp, SleekBorderSubtle, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            when {
                message.isImageGenerating -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = SleekBlue,
                            modifier = Modifier.size(32.dp),
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Synthesizing Space Visualization...",
                            color = TextSlate400,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Applying astrophysical physics parameters",
                            color = TextSlate500,
                            fontSize = 9.sp
                        )
                    }
                }
                message.generatedImageBase64 != null -> {
                    val bitmap = remember(message.generatedImageBase64) {
                        decodeBase64ToBitmap(message.generatedImageBase64)
                    }
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap,
                            contentDescription = message.visualTitle ?: "AI Generated Space Visualization",
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { onOpenFullscreen() },
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = "Visual rendering complete",
                            color = TextSlate400,
                            fontSize = 11.sp
                        )
                    }
                }
                message.imageGenerationError != null -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = message.imageGenerationError,
                            color = Color(0xFFFB7185),
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Button(
                            onClick = { onRegenerateStyle(message.generatedImageStyle ?: SpaceImageStyle.PHOTOREALISTIC) },
                            colors = ButtonDefaults.buttonColors(containerColor = SleekBlue),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text("Retry Visualization", fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // AI Optimized Prompt Inspector (Collapsible)
        message.generatedImagePrompt?.let { prompt ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0F172A).copy(alpha = 0.7f))
                    .border(0.5.dp, SleekBorderSubtle, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isPromptExpanded = !isPromptExpanded },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Prompt",
                            tint = SleekBlue,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "AI-CRAFTED IMAGE PROMPT",
                            color = SleekBlue,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isPromptExpanded) "Collapse ▲" else "Expand ▼",
                            color = TextSlate400,
                            fontSize = 9.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("AI Space Prompt", prompt)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Prompt copied to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Prompt",
                                tint = TextSlate400,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }

                if (isPromptExpanded) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = prompt,
                        color = TextSlate300,
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Style Switcher Chips (Photorealistic, Scientific Illustration, Cinematic, NASA-style, Telescope-style, 3D Render, Artistic)
        Text(
            text = "SWITCH VISUAL STYLE",
            color = TextSlate500,
            fontSize = 8.5.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(SpaceImageStyle.values()) { style ->
                val isCurrent = message.generatedImageStyle == style
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isCurrent) SleekBlue else SleekCardSurface)
                        .border(
                            0.5.dp,
                            if (isCurrent) SleekBlue else SleekBorderSubtle,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { onRegenerateStyle(style) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = when (style) {
                            SpaceImageStyle.PHOTOREALISTIC -> "📷 Photorealistic"
                            SpaceImageStyle.SCIENTIFIC_ILLUSTRATION -> "📐 Scientific Illustration"
                            SpaceImageStyle.CINEMATIC -> "🎬 Cinematic"
                            SpaceImageStyle.NASA_STYLE -> "🛰️ NASA-style"
                            SpaceImageStyle.TELESCOPE_STYLE -> "🔭 Telescope-style"
                            SpaceImageStyle.RENDER_3D -> "🧊 3D Render"
                            SpaceImageStyle.ARTISTIC -> "🎨 Artistic"
                        },
                        color = if (isCurrent) Color.White else TextSlate300,
                        fontSize = 9.5.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

// Fullscreen Image Inspection Dialog
@Composable
fun FullscreenImageInspectionDialog(
    message: AssistantMessage,
    onDismiss: () -> Unit,
    onShare: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(SleekBlack)
                .border(1.dp, SleekBlue, RoundedCornerShape(20.dp)),
            color = SleekBlack
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header with Title and Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = message.visualTitle ?: "Astrophysical Visualization",
                            color = TextSlate100,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "STYLE: ${message.generatedImageStyle?.displayName ?: "Photorealistic"}",
                            color = SleekBlue,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Row {
                        IconButton(onClick = onShare) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = SleekBlue
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextSlate400
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // High-Res Image Display
                message.generatedImageBase64?.let { base64 ->
                    val bitmap = remember(base64) { decodeBase64ToBitmap(base64) }
                    if (bitmap != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, SleekBorderSubtle, RoundedCornerShape(12.dp))
                        ) {
                            Image(
                                bitmap = bitmap,
                                contentDescription = "Fullscreen Space Visualization",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Mandatory Scientific Classification Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF1E1B4B).copy(alpha = 0.7f))
                        .border(0.5.dp, SleekBlue.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "🏷️ ${message.imageClassification.badge}",
                            color = SleekBlue,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = message.imageClassification.disclaimer,
                            color = TextSlate300,
                            fontSize = 9.sp,
                            lineHeight = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Prompt Details Box
                message.generatedImagePrompt?.let { prompt ->
                    Text(
                        text = "SYNTHESIS PARAMETERS",
                        color = TextSlate500,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F172A))
                            .border(0.5.dp, SleekBorderSubtle, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = prompt,
                            color = TextSlate400,
                            fontSize = 9.5.sp,
                            lineHeight = 14.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

// Utility to decode Base64 string to Compose ImageBitmap
fun decodeBase64ToBitmap(base64Str: String): ImageBitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}

// Epistemic Classification Status Badge
@Composable
fun EpistemicBadge(status: EpistemicStatus) {
    val (bgColor, textColor, borderC) = when (status) {
        EpistemicStatus.CONFIRMED_OBSERVATION -> Triple(Color(0xFF064E3B), Color(0xFF34D399), Color(0xFF059669))
        EpistemicStatus.THEORETICAL_PHYSICS -> Triple(Color(0xFF312E81), Color(0xFF818CF8), Color(0xFF6366F1))
        EpistemicStatus.COMPUTATIONAL_SIMULATION -> Triple(Color(0xFF1E3A5F), Color(0xFF38BDF8), Color(0xFF0284C7))
        EpistemicStatus.ESTIMATION_BOUND -> Triple(Color(0xFF451A03), Color(0xFFFBBF24), Color(0xFFD97706))
        EpistemicStatus.MYTH_VS_REALITY -> Triple(Color(0xFF4C0519), Color(0xFFFB7185), Color(0xFFE11D48))
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor.copy(alpha = 0.5f))
            .border(0.5.dp, borderC, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = "● ${status.label.uppercase()}",
            color = textColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
    }
}

// Assistant Loading Indicator
@Composable
fun AssistantLoadingBubble() {
    val infiniteTransition = rememberInfiniteTransition(label = "ThinkingAnim")
    val dotAlpha1 by infiniteTransition.animateFloat(0.2f, 1.0f, infiniteRepeatable(tween(600), RepeatMode.Reverse), "dot1")
    val dotAlpha2 by infiniteTransition.animateFloat(0.2f, 1.0f, infiniteRepeatable(tween(600, delayMillis = 200), RepeatMode.Reverse), "dot2")
    val dotAlpha3 by infiniteTransition.animateFloat(0.2f, 1.0f, infiniteRepeatable(tween(600, delayMillis = 400), RepeatMode.Reverse), "dot3")

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SleekCardSurface)
            .border(1.dp, SleekBorderSubtle, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(SleekNavy)
                    .border(0.5.dp, SleekBlue.copy(alpha = 0.4f), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_cosmoai_logo),
                    contentDescription = "COSMOAI Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "COSMOAI synthesizing astrophysics & 3D telemetry", color = TextSlate400, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(SleekBlue.copy(alpha = dotAlpha1)))
            Spacer(modifier = Modifier.width(4.dp))
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(SleekBlue.copy(alpha = dotAlpha2)))
            Spacer(modifier = Modifier.width(4.dp))
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(SleekBlue.copy(alpha = dotAlpha3)))
        }
    }
}

// Welcome Screen with Categories, Image Generation Starters, and Scientific Prompts
@Composable
fun AssistantWelcomeCard(onSelectPrompt: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SleekCardSurface)
            .border(1.dp, SleekBorderSubtle, RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SleekNavy)
                    .border(1.dp, SleekBlue.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_cosmoai_logo),
                    contentDescription = "COSMOAI Official Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "COSMOAI Assistant & Visualizer",
                    color = TextSlate100,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Explore the cosmos • View in 3D • AI Image Generation",
                    color = TextSlate400,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Ask any astrophysics question or ask to generate/show ANY celestial object, black hole, neutron star, galaxy collision, or exoplanet. The AI will provide scientific explanations and automatically create physics-grounded visualizations in 7 different visual styles.",
            color = TextSlate300,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "🎨 GENERATE SPACE VISUALIZATIONS",
            color = SleekBlue,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        val imageStarters = listOf(
            "🎨 Generate a realistic black hole",
            "🎬 Create TON 618",
            "🔭 Show me what a quasar looks like",
            "🪐 Create an exoplanet with huge rings",
            "💫 Generate a galaxy collision",
            "📐 Show me a neutron star"
        )

        imageStarters.forEach { starter ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SleekBlack)
                    .border(0.5.dp, SleekBlue.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    .clickable { onSelectPrompt(starter.substring(2).trim()) }
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = starter,
                        color = TextSlate100,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Visualize ❯",
                        color = SleekBlue,
                        fontSize = 10.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "POPULAR ASTROPHYSICS QUESTIONS",
            color = TextSlate500,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        val physicsStarters = listOf(
            "🕳️ How does a black hole work and why can't light escape?",
            "⏳ How does gravitational time dilation work near high gravity?",
            "🚀 How far is Voyager 1 right now in light-hours?",
            "☀️ What will happen when our Sun dies?"
        )

        physicsStarters.forEach { starter ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SleekBlack)
                    .border(0.5.dp, SleekBorderSubtle, RoundedCornerShape(12.dp))
                    .clickable { onSelectPrompt(starter.substring(3).trim()) }
                    .padding(12.dp)
            ) {
                Text(
                    text = starter,
                    color = TextSlate300,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
