package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.QuizScoreEntity
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
import com.example.ui.viewmodel.QuizState

@Composable
fun QuizScreen(
    quizState: QuizState,
    scoreHistory: List<QuizScoreEntity>,
    onStartQuiz: (String) -> Unit,
    onSelectOption: (Int) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit,
    onResetQuiz: () -> Unit
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
                    .background(SleekPurpleDark.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "Quiz",
                    tint = SleekPurple,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "COSMIC QUIZ",
                    color = SleekPurple,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.6.sp
                )
                Text(
                    text = "Test Your Astrophysics Knowledge",
                    color = TextSlate500,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when {
            quizState.isQuizFinished -> {
                QuizFinishedView(
                    quizState = quizState,
                    onPlayAgain = { onStartQuiz(quizState.selectedDifficulty) },
                    onHome = onResetQuiz
                )
            }
            quizState.isQuizActive -> {
                ActiveQuizView(
                    quizState = quizState,
                    onSelectOption = onSelectOption,
                    onSubmitAnswer = onSubmitAnswer,
                    onNextQuestion = onNextQuestion
                )
            }
            else -> {
                QuizStartHubView(
                    scoreHistory = scoreHistory,
                    onStartQuiz = onStartQuiz
                )
            }
        }
    }
}

@Composable
private fun QuizStartHubView(
    scoreHistory: List<QuizScoreEntity>,
    onStartQuiz: (String) -> Unit
) {
    val difficulties = listOf("All", "Novice", "Intermediate", "Astrophysicist")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Welcome Banner Card
        item {
            CosmicGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = SleekBorderSubtle,
                backgroundColor = SleekCardSurface
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "ASTRONOMY TRIVIA CHALLENGE",
                        color = SleekGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Challenge your understanding of black holes, relativistic time dilation, pulsar mechanics, and cosmic origins.",
                        color = TextSlate100,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "SELECT DIFFICULTY LEVEL:",
                        color = TextSlate500,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        difficulties.forEach { diff ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SleekCardElevated)
                                    .border(1.dp, SleekBorderSubtle, RoundedCornerShape(12.dp))
                                    .clickable { onStartQuiz(diff) }
                                    .padding(vertical = 10.dp)
                                    .testTag("quiz_difficulty_btn_$diff"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = diff,
                                    color = if (diff == "Astrophysicist") SleekPurple else if (diff == "Intermediate") SleekBlue else TextSlate300,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // High Score / History Card
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Trophy",
                                tint = SleekGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "RECENT SCORE HISTORY",
                                color = SleekGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        Text(
                            text = "${scoreHistory.size} completed",
                            color = TextSlate500,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (scoreHistory.isEmpty()) {
                        Text(
                            text = "No quiz attempts yet. Start a session above!",
                            color = TextSlate400,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        scoreHistory.take(5).forEach { score ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${score.difficulty} Level",
                                    color = TextSlate100,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SleekCardElevated)
                                        .border(0.5.dp, SleekBorderSubtle, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${score.score} / ${score.totalQuestions} pts",
                                        color = if (score.score >= score.totalQuestions * 0.8) Color(0xFF10B981) else SleekBlue,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
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
private fun ActiveQuizView(
    quizState: QuizState,
    onSelectOption: (Int) -> Unit,
    onSubmitAnswer: () -> Unit,
    onNextQuestion: () -> Unit
) {
    val currentQ = quizState.currentQuestions.getOrNull(quizState.currentIndex) ?: return
    val totalCount = quizState.currentQuestions.size
    val progress = (quizState.currentIndex + 1).toFloat() / totalCount.toFloat()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Progress & Streak Bar
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Question ${quizState.currentIndex + 1} of $totalCount",
                        color = TextSlate400,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (quizState.currentStreak > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = SleekGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${quizState.currentStreak} Streak",
                                color = SleekGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = SleekBlue,
                    trackColor = SleekCardSurface
                )
            }
        }

        // Question Card
        item {
            CosmicGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = SleekBorderSubtle,
                backgroundColor = SleekCardSurface
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SleekPurpleDark.copy(alpha = 0.3f))
                            .border(0.5.dp, SleekPurple.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${currentQ.category} • ${currentQ.difficulty}",
                            color = SleekPurple,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = currentQ.question,
                        color = TextSlate100,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Multiple-Choice Options
        items(currentQ.options.indices.toList()) { index ->
            val optionText = currentQ.options[index]
            val isSelected = quizState.selectedOptionIndex == index
            val isSubmitted = quizState.isAnswerSubmitted
            val isCorrectOption = index == currentQ.correctIndex

            val cardBorderColor = when {
                !isSubmitted && isSelected -> SleekBlue
                isSubmitted && isCorrectOption -> Color(0xFF10B981)
                isSubmitted && isSelected && !isCorrectOption -> Color(0xFFEF4444)
                else -> SleekBorderSubtle
            }

            val cardBackgroundColor = when {
                !isSubmitted && isSelected -> SleekBlueDark.copy(alpha = 0.2f)
                isSubmitted && isCorrectOption -> Color(0xFF10B981).copy(alpha = 0.15f)
                isSubmitted && isSelected && !isCorrectOption -> Color(0xFFEF4444).copy(alpha = 0.15f)
                else -> SleekCardSurface
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(cardBackgroundColor)
                    .border(1.dp, cardBorderColor, RoundedCornerShape(14.dp))
                    .clickable(enabled = !isSubmitted) { onSelectOption(index) }
                    .padding(14.dp)
                    .testTag("quiz_option_$index")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = optionText,
                        color = if (isSelected) TextSlate100 else TextSlate300,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.weight(1f)
                    )

                    if (isSubmitted) {
                        if (isCorrectOption) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Correct",
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(18.dp)
                            )
                        } else if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Incorrect",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // Scientific Explanation Reveal Box
        if (quizState.isAnswerSubmitted) {
            item {
                CosmicGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = if (quizState.isCorrect) Color(0xFF10B981).copy(alpha = 0.4f) else Color(0xFFEF4444).copy(alpha = 0.4f),
                    backgroundColor = SleekCardSurface
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (quizState.isCorrect) "✓ SCIENTIFICALLY ACCURATE" else "✗ INCORRECT PREDICTION",
                            color = if (quizState.isCorrect) Color(0xFF10B981) else Color(0xFFEF4444),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentQ.scientificExplanation,
                            color = TextSlate300,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Bottom Action Button (Submit or Next)
        item {
            if (!quizState.isAnswerSubmitted) {
                ElevatedButton(
                    onClick = onSubmitAnswer,
                    enabled = quizState.selectedOptionIndex != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_quiz_answer_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = SleekPurple,
                        contentColor = Color.White,
                        disabledContainerColor = SleekCardElevated,
                        disabledContentColor = TextSlate600
                    )
                ) {
                    Text(text = "Submit Answer", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                ElevatedButton(
                    onClick = onNextQuestion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("next_quiz_question_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = SleekBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (quizState.currentIndex + 1 >= totalCount) "View Results" else "Next Question",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun QuizFinishedView(
    quizState: QuizState,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit
) {
    val total = quizState.currentQuestions.size
    val score = quizState.currentScore
    val percentage = if (total > 0) (score * 100) / total else 0

    val rankTitle = when {
        percentage >= 90 -> "Cosmic Master 🌌"
        percentage >= 70 -> "Orbital Physicist 🚀"
        percentage >= 50 -> "Stargazer 🔭"
        else -> "Space Cadet 🧑‍🚀"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(SleekGold, SleekPurpleDark))),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Trophy",
                tint = SleekBlack,
                modifier = Modifier.size(38.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = rankTitle,
            color = SleekGold,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Quiz Session Complete",
            color = TextSlate500,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        CosmicGlassCard(
            modifier = Modifier.fillMaxWidth(0.9f),
            borderColor = SleekBorderSubtle,
            backgroundColor = SleekCardSurface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "SCORE", color = TextSlate500, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = "$score / $total", color = TextSlate100, fontSize = 17.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "ACCURACY", color = TextSlate500, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = "$percentage%", color = SleekBlue, fontSize = 17.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "BEST STREAK", color = TextSlate500, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = "${quizState.highestStreak}", color = SleekGold, fontSize = 17.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onHome,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SleekBorderSubtle)
            ) {
                Text(text = "Home", color = TextSlate300)
            }

            ElevatedButton(
                onClick = onPlayAgain,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = SleekPurple,
                    contentColor = Color.White
                )
            ) {
                Icon(imageVector = Icons.Default.Replay, contentDescription = "Play Again", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Play Again", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}
