package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.SpaceFact
import com.example.ui.components.CertaintyBadge
import com.example.ui.components.CosmicGlassCard
import com.example.ui.components.SleekHeroCard
import com.example.ui.theme.SleekBlack
import com.example.ui.theme.SleekBlue
import com.example.ui.theme.SleekBlueDark
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekBorderSubtle
import com.example.ui.theme.SleekCardElevated
import com.example.ui.theme.SleekCardSurface
import com.example.ui.theme.SleekGlassSubtle
import com.example.ui.theme.SleekGlassWhite
import com.example.ui.theme.SleekGold
import com.example.ui.theme.SleekPurple
import com.example.ui.theme.SleekPurpleDark
import com.example.ui.theme.TextSlate100
import com.example.ui.theme.TextSlate300
import com.example.ui.theme.TextSlate400
import com.example.ui.theme.TextSlate500
import com.example.ui.theme.TextSlate600

@Composable
fun DailyFactScreen(
    currentFact: SpaceFact,
    allFacts: List<SpaceFact>,
    isFavorite: Boolean,
    onToggleFavorite: (SpaceFact, Boolean) -> Unit,
    onNextFact: () -> Unit,
    onPreviousFact: () -> Unit,
    onRandomFact: () -> Unit,
    onSelectFact: (SpaceFact) -> Unit,
    onOpenStoryCard: (SpaceFact) -> Unit,
    onShareText: (SpaceFact) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Badge Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(SleekBlueDark.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Cosmic icon",
                            tint = SleekBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "CHRONO-OBSERVATORY",
                            color = SleekBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.6.sp
                        )
                        Text(
                            text = "Universal Phenomenon Feed",
                            color = TextSlate500,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(
                    onClick = { onToggleFavorite(currentFact, isFavorite) },
                    modifier = Modifier.testTag("bookmark_daily_fact_button")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark Fact",
                        tint = if (isFavorite) SleekGold else TextSlate500,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Main Featured Fact Sleek Hero Card
        item {
            SleekHeroCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SleekBorder, RoundedCornerShape(24.dp))
                    .testTag("daily_fact_main_card")
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Header Artwork Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_hero_cosmos),
                            contentDescription = "Cosmic hero illustration",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            SleekCardElevated.copy(alpha = 0.9f)
                                        )
                                    )
                                )
                        )

                        // Badges Overlay
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SleekPurpleDark.copy(alpha = 0.85f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = currentFact.category.uppercase(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                )
                            }
                            CertaintyBadge(certainty = currentFact.certainty)
                        }
                    }

                    // Fact Text Details
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = currentFact.title,
                            color = TextSlate100,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 25.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = currentFact.summary,
                            color = TextSlate400,
                            fontSize = 13.sp,
                            lineHeight = 21.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Scientific Observation Source Pill
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(SleekBlack.copy(alpha = 0.6f))
                                .border(1.dp, SleekBorderSubtle, RoundedCornerShape(12.dp))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Source info",
                                tint = SleekBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Observation: ${currentFact.observationalSource}",
                                color = TextSlate500,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Expandable In-Depth Astrophysics Breakdown
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { isExpanded = !isExpanded }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isExpanded) "Hide Deep Mechanics" else "Read Astrophysics Breakdown",
                                color = SleekBlue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Toggle explanation",
                                tint = SleekBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(SleekBlack.copy(alpha = 0.5f))
                                    .border(1.dp, SleekBorderSubtle, RoundedCornerShape(14.dp))
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = "SCIENTIFIC MECHANICS",
                                    color = SleekGold,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.2.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = currentFact.detailedExplanation,
                                    color = TextSlate300,
                                    fontSize = 12.sp,
                                    lineHeight = 19.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Action Buttons Bar (Instagram Story Card & Share)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ElevatedButton(
                                onClick = { onOpenStoryCard(currentFact) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("create_story_card_button"),
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = SleekPurpleDark,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = "Story Card",
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Story Card", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            FilledTonalButton(
                                onClick = { onShareText(currentFact) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("share_fact_button"),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = SleekGlassWhite,
                                    contentColor = SleekBlue
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Share", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        // Inspiring Quote Card
        item {
            CosmicGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = SleekBorderSubtle,
                backgroundColor = SleekCardSurface
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatQuote,
                        contentDescription = "Quote",
                        tint = SleekPurple,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "\"${currentFact.quote}\"",
                        color = TextSlate300,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Navigation Controls (Previous, Shuffle, Next)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onPreviousFact,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSlate300),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(SleekBorder, SleekBlue)))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous Fact",
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Prev", fontSize = 12.sp)
                }

                ElevatedButton(
                    onClick = onRandomFact,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = SleekCardElevated,
                        contentColor = SleekGold
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = "Random Fact",
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Shuffle", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onNextFact,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSlate300),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(SleekBlue, SleekBorder)))
                ) {
                    Text(text = "Next", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next Fact",
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }

        // Facts Archive Carousel
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "EXPLORE ARCHIVED FACTS",
                    color = TextSlate500,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(allFacts) { fact ->
                        val isSelected = fact.id == currentFact.id
                        Box(
                            modifier = Modifier
                                .width(190.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) SleekCardElevated else SleekCardSurface)
                                .border(
                                    width = if (isSelected) 1.dp else 0.5.dp,
                                    color = if (isSelected) SleekBlue else SleekBorderSubtle,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { onSelectFact(fact) }
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = fact.category.uppercase(),
                                    color = if (isSelected) SleekBlue else TextSlate600,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = fact.title,
                                    color = if (isSelected) TextSlate100 else TextSlate400,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
