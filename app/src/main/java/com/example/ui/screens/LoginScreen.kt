package com.example.ui.screens

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.datasource.AuthSessionManager
import com.example.data.model.AuthUser
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
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LoginScreen(
    authManager: AuthSessionManager,
    onLoginSuccess: (AuthUser) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }

    // Orbital animation background
    val infiniteTransition = rememberInfiniteTransition(label = "LoginOrbit")
    val orbitAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(16000, easing = LinearEasing), RepeatMode.Restart),
        label = "Orbit"
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = SleekBlack
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF030712),
                            Color(0xFF0B132B),
                            SleekBlack
                        )
                    )
                )
        ) {
            // Cosmic Background Canvas with glowing orbital rings & stars
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height * 0.32f

                // Deep star particles
                for (i in 0 until 80) {
                    val angle = (i * 137.5f) * (PI / 180f).toFloat()
                    val dist = (i % 6 + 1) * (size.width * 0.08f)
                    val starX = cx + cos(angle) * dist
                    val starY = cy + sin(angle) * dist * 0.6f
                    drawCircle(
                        color = Color.White.copy(alpha = (i % 4 + 2) * 0.2f),
                        radius = if (i % 5 == 0) 1.8f else 1.0f,
                        center = Offset(starX, starY)
                    )
                }

                // Orbital elliptical rings
                drawOval(
                    color = SleekBlue.copy(alpha = 0.25f),
                    topLeft = Offset(cx - 160f, cy - 60f),
                    size = androidx.compose.ui.geometry.Size(320f, 120f),
                    style = Stroke(width = 1.5f)
                )
                drawOval(
                    color = SleekPurple.copy(alpha = 0.2f),
                    topLeft = Offset(cx - 240f, cy - 90f),
                    size = androidx.compose.ui.geometry.Size(480f, 180f),
                    style = Stroke(width = 1.0f)
                )

                // Orbiting satellite dot
                val rad = orbitAngle * (PI / 180f).toFloat()
                val satX = cx + cos(rad) * 160f
                val satY = cy + sin(rad) * 60f
                drawCircle(
                    color = Color(0xFF67E8F9),
                    radius = 4f,
                    center = Offset(satX, satY)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Official COSMOAI Logo Badge (exact proportions, no distortion)
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .background(Color(0xFF0F172A))
                        .border(
                            1.5.dp,
                            Brush.linearGradient(
                                listOf(Color(0xFF38BDF8), Color(0xFF818CF8), Color(0xFF60A5FA))
                            ),
                            RoundedCornerShape(26.dp)
                        )
                        .shadow(12.dp, RoundedCornerShape(26.dp), spotColor = SleekBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_cosmoai_logo),
                        contentDescription = "COSMOAI Official Logo",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(26.dp)),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // App Title
                Text(
                    text = "COSMOAI",
                    color = TextSlate100,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "EXPLORE • DISCOVER • VISUALIZE",
                    color = SleekBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "The Universe, In Your Hands",
                    color = TextSlate300,
                    fontSize = 13.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Interact with 3D celestial bodies, generate physics-grounded space visuals with AI, calculate light-time distances, and explore real-time astrophysical telemetry.",
                    color = TextSlate400,
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(26.dp))

                // Feature Highlights Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    FeaturePill(icon = Icons.Default.AutoAwesome, label = "AI Visualizer")
                    Spacer(modifier = Modifier.width(8.dp))
                    FeaturePill(icon = Icons.Default.Science, label = "Interactive 3D")
                    Spacer(modifier = Modifier.width(8.dp))
                    FeaturePill(icon = Icons.Default.Security, label = "Secure Cloud")
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Error Message if any
                errorMessage?.let { error ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF4C0519).copy(alpha = 0.8f))
                            .border(1.dp, Color(0xFFE11D48), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = error,
                            color = Color(0xFFFB7185),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // ================= GOOGLE SIGN IN BUTTON =================
                Button(
                    onClick = {
                        isLoading = true
                        errorMessage = null
                        coroutineScope.launch {
                            val result = authManager.signInWithGoogle(context)
                            isLoading = false
                            result.onSuccess { user ->
                                onLoginSuccess(user)
                            }.onFailure { ex ->
                                errorMessage = ex.message ?: "Sign-in failed. Please try again."
                            }
                        }
                    },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF1F2937)
                    ),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("google_signin_button")
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = SleekBlue,
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Authenticating...",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                    } else {
                        // Google 'G' Icon
                        GoogleLogoIcon(modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Continue with Google",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Privacy Policy & Terms of Service Footer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Terms of Service",
                        color = TextSlate400,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clickable { showTermsDialog = true }
                            .testTag("terms_button")
                    )
                    Text(
                        text = "  •  ",
                        color = TextSlate500,
                        fontSize = 11.5.sp
                    )
                    Text(
                        text = "Privacy Policy",
                        color = TextSlate400,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clickable { showPrivacyDialog = true }
                            .testTag("privacy_button")
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
            }

            // ================= TERMS OF SERVICE DIALOG =================
            if (showTermsDialog) {
                Dialog(onDismissRequest = { showTermsDialog = false }) {
                    LegalDocumentDialog(
                        title = "Terms of Service",
                        content = "Welcome to COSMOAI.\n\n1. Acceptance: By signing in with Google, you agree to access educational astrophysics data and AI visualization tools responsibly.\n\n2. Scientific Epistemic Integrity: AI-generated visual representations and 3D artistic models are labeled as simulations derived from mathematical/astrophysical equations and are not photographic records of unmapped surfaces.\n\n3. Account Security: Your Google authentication tokens are managed securely on-device via Credential Manager.",
                        onDismiss = { showTermsDialog = false }
                    )
                }
            }

            // ================= PRIVACY POLICY DIALOG =================
            if (showPrivacyDialog) {
                Dialog(onDismissRequest = { showPrivacyDialog = false }) {
                    LegalDocumentDialog(
                        title = "Privacy Policy",
                        content = "COSMOAI respects your privacy:\n\n1. Authentication: We use Google Sign-In to verify your identity and save your preferences securely.\n\n2. Telemetry & Analytics: No personal telemetry or location data is sold or shared with third parties.\n\n3. Session Persistence: Login state is stored encrypted on your local device SharedPreferences and can be cleared at any time via the Log Out setting.",
                        onDismiss = { showPrivacyDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturePill(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SleekNavy.copy(alpha = 0.7f))
            .border(0.5.dp, SleekBorderSubtle, RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = SleekBlue, modifier = Modifier.size(13.dp))
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = label, color = TextSlate300, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun GoogleLogoIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val s = size.width
        val radius = s / 2f
        val center = Offset(s / 2f, s / 2f)

        // Draw Google Quad-Color G Shape
        drawCircle(
            color = Color(0xFF4285F4),
            radius = radius,
            center = center,
            style = Stroke(width = s * 0.22f)
        )
        // Crossbar
        drawRect(
            color = Color(0xFF4285F4),
            topLeft = Offset(center.x, center.y - s * 0.11f),
            size = androidx.compose.ui.geometry.Size(s * 0.5f, s * 0.22f)
        )
    }
}

@Composable
private fun LegalDocumentDialog(title: String, content: String, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(20.dp))
            .background(SleekNavy)
            .border(1.dp, SleekBorderSubtle, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = TextSlate100,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSlate400)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = content,
                color = TextSlate300,
                fontSize = 12.5.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = SleekBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Understood", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
