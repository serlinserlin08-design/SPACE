package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.datasource.AuthSessionManager
import com.example.data.datasource.CosmicDataSources
import com.example.data.datasource.Space3DModelGenerator
import com.example.ui.components.StarfieldBackground
import com.example.ui.screens.DailyFactScreen
import com.example.ui.screens.ExplorerScreen
import com.example.ui.screens.FactStoryCardDialog
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.Space3DViewerScreen
import com.example.ui.screens.SpaceAssistantScreen
import com.example.ui.screens.SpaceObjectDetailDialog
import com.example.ui.screens.TimeLabScreen
import com.example.ui.theme.CosmicTimeTheme
import com.example.ui.theme.SleekBlack
import com.example.ui.theme.SleekBlue
import com.example.ui.theme.SleekBlueDark
import com.example.ui.theme.SleekBorderSubtle
import com.example.ui.theme.SleekCardSurface
import com.example.ui.theme.SleekNavBackground
import com.example.ui.theme.SleekNavy
import com.example.ui.theme.SleekPurple
import com.example.ui.theme.TextSlate100
import com.example.ui.theme.TextSlate300
import com.example.ui.theme.TextSlate400
import com.example.ui.theme.TextSlate500
import com.example.ui.viewmodel.AppNavTab
import com.example.ui.viewmodel.CosmicViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: CosmicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authManager = AuthSessionManager(applicationContext)
        setContent {
            CosmicTimeTheme {
                val currentUser by authManager.currentUser.collectAsStateWithLifecycle()

                if (currentUser == null) {
                    LoginScreen(
                        authManager = authManager,
                        onLoginSuccess = { /* StateFlow updates automatically */ }
                    )
                } else {
                    CosmicApp(
                        viewModel = viewModel,
                        authManager = authManager
                    )
                }
            }
        }
    }
}

private data class NavItemData(
    val tab: AppNavTab,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun CosmicApp(
    viewModel: CosmicViewModel,
    authManager: AuthSessionManager
) {
    val coroutineScope = rememberCoroutineScope()
    val currentUser by authManager.currentUser.collectAsStateWithLifecycle()
    var showUserMenu by remember { mutableStateOf(false) }

    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val currentFact by viewModel.currentFact.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val favoriteIds = remember(favorites) { favorites.map { it.id }.toSet() }

    val selectedObjectCategory by viewModel.selectedObjectCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedSpaceObject by viewModel.selectedSpaceObject.collectAsStateWithLifecycle()

    val timeLabTab by viewModel.timeLabTab.collectAsStateWithLifecycle()
    val lightTimeState by viewModel.lightTimeState.collectAsStateWithLifecycle()
    val timeDilationState by viewModel.timeDilationState.collectAsStateWithLifecycle()

    val quizState by viewModel.quizState.collectAsStateWithLifecycle()
    val quizScoreHistory by viewModel.quizScoreHistory.collectAsStateWithLifecycle()

    val storyCardFact by viewModel.storyCardFact.collectAsStateWithLifecycle()

    val assistantMessages by viewModel.assistantMessages.collectAsStateWithLifecycle()
    val isAssistantLoading by viewModel.isAssistantLoading.collectAsStateWithLifecycle()

    val active3DModelTarget by viewModel.active3DModelTarget.collectAsStateWithLifecycle()

    val isCurrentFactFav = remember(currentFact.id, favoriteIds) {
        favoriteIds.contains(currentFact.id)
    }

    val navItems = listOf(
        NavItemData(AppNavTab.DAILY_FACT, Icons.Filled.Today, Icons.Outlined.Today, "nav_daily_fact"),
        NavItemData(AppNavTab.EXPLORER, Icons.Filled.Explore, Icons.Outlined.Explore, "nav_explorer"),
        NavItemData(AppNavTab.ASSISTANT, Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome, "nav_assistant"),
        NavItemData(AppNavTab.TIME_LAB, Icons.Filled.Science, Icons.Outlined.Science, "nav_time_lab"),
        NavItemData(AppNavTab.QUIZ, Icons.Filled.Psychology, Icons.Outlined.Psychology, "nav_quiz"),
        NavItemData(AppNavTab.SAVED, Icons.Filled.Bookmark, Icons.Filled.BookmarkBorder, "nav_favorites")
    )

    // Pulsing Node Indicator Transition
    val infiniteTransition = rememberInfiniteTransition(label = "ObserverNodePulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Dynamic Animated Starfield Backdrop
        StarfieldBackground(modifier = Modifier.fillMaxSize())

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                // Sleek Top Header with Official COSMOAI Logo & Observer Node / User Profile
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Official COSMOAI Logo (scaled preserving exact 1:1 aspect ratio)
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(9.dp))
                                .background(SleekCardSurface)
                                .border(1.dp, SleekBlue.copy(alpha = 0.5f), RoundedCornerShape(9.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_cosmoai_logo),
                                contentDescription = "COSMOAI Official Logo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(9.dp)),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "COSMOAI",
                                style = TextStyle(
                                    brush = Brush.linearGradient(listOf(SleekBlue, SleekPurple)),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Text(
                                text = "EXPLORE • DISCOVER • VISUALIZE",
                                color = TextSlate500,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.1.sp
                            )
                        }
                    }

                    // User Profile / Node Indicator with Dropdown Menu for Logout
                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                                .clickable { showUserMenu = true }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("user_profile_button")
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(SleekBlue.copy(alpha = pulseAlpha))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = currentUser?.displayName?.take(10) ?: "Pilot",
                                color = TextSlate300,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        DropdownMenu(
                            expanded = showUserMenu,
                            onDismissRequest = { showUserMenu = false },
                            modifier = Modifier
                                .background(SleekNavy)
                                .border(1.dp, SleekBorderSubtle, RoundedCornerShape(14.dp))
                                .padding(4.dp)
                        ) {
                            DropdownMenuItem(
                                leadingIcon = {
                                    Image(
                                        painter = painterResource(id = R.drawable.img_cosmoai_logo),
                                        contentDescription = "COSMOAI Logo",
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(RoundedCornerShape(6.dp)),
                                        contentScale = ContentScale.Fit
                                    )
                                },
                                text = {
                                    Column {
                                        Text(
                                            text = currentUser?.displayName ?: "Cosmic Explorer",
                                            color = TextSlate100,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = currentUser?.email ?: "COSMOAI Observer Account",
                                            color = TextSlate400,
                                            fontSize = 10.5.sp
                                        )
                                    }
                                },
                                onClick = { /* Dismiss */ }
                            )
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Logout,
                                        contentDescription = "Sign Out",
                                        tint = Color(0xFFFB7185),
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                text = {
                                    Text(
                                        text = "Sign Out",
                                        color = Color(0xFFFB7185),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp
                                    )
                                },
                                onClick = {
                                    showUserMenu = false
                                    coroutineScope.launch {
                                        authManager.signOut()
                                    }
                                }
                            )
                        }
                    }
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = SleekNavBackground.copy(alpha = 0.94f),
                    contentColor = SleekBlue,
                    tonalElevation = 0.dp,
                    windowInsets = WindowInsets.navigationBars,
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .border(
                            width = 1.dp,
                            color = SleekBorderSubtle,
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        )
                ) {
                    navItems.forEach { item ->
                        val selected = currentTab == item.tab
                        NavigationBarItem(
                            selected = selected,
                            onClick = { viewModel.setNavTab(item.tab) },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.tab.title,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = item.tab.title,
                                    fontSize = 10.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SleekBlue,
                                selectedTextColor = SleekBlue,
                                unselectedIconColor = TextSlate500,
                                unselectedTextColor = TextSlate500,
                                indicatorColor = SleekBlueDark.copy(alpha = 0.22f)
                            ),
                            modifier = Modifier.testTag(item.testTag)
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Crossfade(targetState = currentTab, label = "TabCrossfade") { targetTab ->
                    when (targetTab) {
                        AppNavTab.DAILY_FACT -> {
                            DailyFactScreen(
                                currentFact = currentFact,
                                allFacts = viewModel.allFacts,
                                isFavorite = isCurrentFactFav,
                                onToggleFavorite = { fact, isFav ->
                                    viewModel.toggleFavoriteFact(fact, isFav)
                                },
                                onNextFact = { viewModel.nextFact() },
                                onPreviousFact = { viewModel.previousFact() },
                                onRandomFact = { viewModel.randomFact() },
                                onSelectFact = { fact -> viewModel.selectFact(fact) },
                                onOpenStoryCard = { fact -> viewModel.openStoryCard(fact) },
                                onShareText = { fact -> viewModel.shareFactText(fact) }
                            )
                        }
                        AppNavTab.EXPLORER -> {
                            ExplorerScreen(
                                objects = viewModel.getFilteredObjects(),
                                selectedCategory = selectedObjectCategory,
                                searchQuery = searchQuery,
                                favoriteIds = favoriteIds,
                                onSelectCategory = { viewModel.setObjectCategory(it) },
                                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                onSelectObject = { viewModel.selectSpaceObject(it) },
                                onToggleFavorite = { obj, isFav ->
                                    viewModel.toggleFavoriteObject(obj, isFav)
                                }
                            )
                        }
                        AppNavTab.ASSISTANT -> {
                            SpaceAssistantScreen(
                                messages = assistantMessages,
                                isLoading = isAssistantLoading,
                                onSendMessage = { viewModel.askSpaceAssistant(it) },
                                onRegenerateImageStyle = { id, style -> viewModel.regenerateMessageImage(id, style) },
                                onOpen3DViewer = { targetQuery -> viewModel.open3DViewer(targetQuery) },
                                onClearChat = { viewModel.clearAssistantChat() }
                            )
                        }
                        AppNavTab.TIME_LAB -> {
                            TimeLabScreen(
                                currentTab = timeLabTab,
                                onTabSelected = { viewModel.setTimeLabTab(it) },
                                lightTimeState = lightTimeState,
                                onLightTimeInputChange = { viewModel.setLightTimeInput(it) },
                                onLightTimeUnitChange = { viewModel.setLightTimeUnit(it) },
                                onSelectLightTimePreset = { viewModel.selectLightTimePreset(it) },
                                timeDilationState = timeDilationState,
                                onSetDilationMode = { viewModel.setDilationMode(it) },
                                onSetGravityRatio = { viewModel.setGravityRatio(it) },
                                onSetVelocityFraction = { viewModel.setVelocityFraction(it) },
                                onSelectGravityPreset = { viewModel.selectGravityPreset(it) },
                                onSelectVelocityPreset = { viewModel.selectVelocityPreset(it) },
                                onToggleClockRunning = { viewModel.toggleClockRunning() },
                                onResetClock = { viewModel.resetDilationClock() }
                            )
                        }
                        AppNavTab.QUIZ -> {
                            QuizScreen(
                                quizState = quizState,
                                scoreHistory = quizScoreHistory,
                                onStartQuiz = { diff -> viewModel.startQuiz(diff) },
                                onSelectOption = { viewModel.selectQuizOption(it) },
                                onSubmitAnswer = { viewModel.submitQuizAnswer() },
                                onNextQuestion = { viewModel.nextQuizQuestion() },
                                onResetQuiz = { viewModel.resetQuiz() }
                            )
                        }
                        AppNavTab.SAVED -> {
                            FavoritesScreen(
                                favorites = favorites,
                                allFacts = viewModel.allFacts,
                                allObjects = CosmicDataSources.spaceObjects,
                                onRemoveFavorite = { viewModel.removeFavorite(it) },
                                onOpenStoryCard = { fact -> viewModel.openStoryCard(fact) },
                                onSelectObject = { obj ->
                                    viewModel.selectSpaceObject(obj)
                                },
                                onSelectFact = { fact ->
                                    viewModel.selectFact(fact)
                                    viewModel.setNavTab(AppNavTab.DAILY_FACT)
                                }
                            )
                        }
                    }
                }
            }
        }

        // Modals & Overlays
        selectedSpaceObject?.let { obj ->
            val isFav = favoriteIds.contains(obj.id)
            SpaceObjectDetailDialog(
                obj = obj,
                isFavorite = isFav,
                onDismiss = { viewModel.selectSpaceObject(null) },
                onToggleFavorite = { targetObj, currentFav ->
                    viewModel.toggleFavoriteObject(targetObj, currentFav)
                },
                onShare = { viewModel.shareObjectText(it) },
                onView3D = { targetObj ->
                    viewModel.open3DViewer(targetObj.name)
                }
            )
        }

        storyCardFact?.let { fact ->
            FactStoryCardDialog(
                fact = fact,
                onDismiss = { viewModel.closeStoryCard() },
                onShare = { viewModel.shareFactText(it) }
            )
        }

        // ================= INTERACTIVE 3D VIEWER FULLSCREEN OVERLAY =================
        active3DModelTarget?.let { query ->
            val modelData = remember(query) {
                Space3DModelGenerator.getModelForQuery(query)
            }
            Space3DViewerScreen(
                model = modelData,
                onBack = { viewModel.close3DViewer() }
            )
        }
    }
}

