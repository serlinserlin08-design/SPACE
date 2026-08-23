package com.example.ui.viewmodel

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiSpaceService
import com.example.data.datasource.LightTimePresetsData
import com.example.data.local.CosmicDatabase
import com.example.data.local.entity.FavoriteEntity
import com.example.data.local.entity.QuizScoreEntity
import com.example.data.datasource.SpaceImageGenerator
import com.example.data.model.AssistantMessage
import com.example.data.model.DistanceUnit
import com.example.data.model.GravityPreset
import com.example.data.model.LightTimePreset
import com.example.data.model.ObjectCategory
import com.example.data.model.QuizQuestion
import com.example.data.model.RelativityEngine
import com.example.data.model.SpaceFact
import com.example.data.model.SpaceImageStyle
import com.example.data.model.SpaceObject
import com.example.data.model.SpaceVisualClassification
import com.example.data.model.VelocityPreset
import com.example.data.repository.CosmicRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class AppNavTab(val title: String) {
    DAILY_FACT("Daily Fact"),
    EXPLORER("Explorer"),
    ASSISTANT("Space AI"),
    TIME_LAB("Time Lab"),
    QUIZ("Quiz"),
    SAVED("Saved")
}

enum class TimeLabTab {
    TIME_DILATION,
    LIGHT_TIME
}

enum class DilationMode {
    GRAVITATIONAL,
    VELOCITY
}

data class LightTimeState(
    val inputValue: String = "2500000",
    val selectedUnit: DistanceUnit = DistanceUnit.LIGHT_YEARS,
    val selectedPreset: LightTimePreset? = null,
    val computedTravelSeconds: Double = 0.0,
    val formattedDuration: String = "",
    val earthEra: String = ""
)

data class TimeDilationState(
    val mode: DilationMode = DilationMode.GRAVITATIONAL,
    val gravityRatio: Float = 0.35f, // r_s / r
    val velocityFraction: Float = 0.90f, // v / c
    val selectedGravityPreset: GravityPreset? = RelativityEngine.gravityPresets[3], // Neutron star default
    val selectedVelocityPreset: VelocityPreset? = null,
    val dilationFactor: Double = 1.2403,
    val isClockRunning: Boolean = true,
    val travelerElapsedSeconds: Double = 0.0,
    val observerElapsedSeconds: Double = 0.0
)

data class QuizState(
    val isQuizActive: Boolean = false,
    val isQuizFinished: Boolean = false,
    val selectedDifficulty: String = "All",
    val currentQuestions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val isCorrect: Boolean = false,
    val currentScore: Int = 0,
    val currentStreak: Int = 0,
    val highestStreak: Int = 0
)

class CosmicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CosmicRepository

    init {
        val database = CosmicDatabase.getDatabase(application)
        repository = CosmicRepository(database.cosmicDao())
    }

    val favorites: StateFlow<List<FavoriteEntity>> = repository.allFavorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quizScoreHistory: StateFlow<List<QuizScoreEntity>> = repository.allQuizScores
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentTab = MutableStateFlow(AppNavTab.DAILY_FACT)
    val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

    // Daily Fact State
    private val _currentFact = MutableStateFlow(repository.getTodayFact())
    val currentFact: StateFlow<SpaceFact> = _currentFact.asStateFlow()

    val allFacts: List<SpaceFact> = repository.getAllFacts()

    // Explorer State
    private val _selectedObjectCategory = MutableStateFlow(ObjectCategory.ALL)
    val selectedObjectCategory: StateFlow<ObjectCategory> = _selectedObjectCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSpaceObject = MutableStateFlow<SpaceObject?>(null)
    val selectedSpaceObject: StateFlow<SpaceObject?> = _selectedSpaceObject.asStateFlow()

    // Time Lab State
    private val _timeLabTab = MutableStateFlow(TimeLabTab.TIME_DILATION)
    val timeLabTab: StateFlow<TimeLabTab> = _timeLabTab.asStateFlow()

    private val _lightTimeState = MutableStateFlow(LightTimeState())
    val lightTimeState: StateFlow<LightTimeState> = _lightTimeState.asStateFlow()

    private val _timeDilationState = MutableStateFlow(TimeDilationState())
    val timeDilationState: StateFlow<TimeDilationState> = _timeDilationState.asStateFlow()

    // Quiz State
    private val _quizState = MutableStateFlow(QuizState())
    val quizState: StateFlow<QuizState> = _quizState.asStateFlow()

    // Story Card Dialog State
    private val _storyCardFact = MutableStateFlow<SpaceFact?>(null)
    val storyCardFact: StateFlow<SpaceFact?> = _storyCardFact.asStateFlow()

    // Space Assistant State
    private val geminiService = GeminiSpaceService()
    private val _assistantMessages = MutableStateFlow<List<AssistantMessage>>(emptyList())
    val assistantMessages: StateFlow<List<AssistantMessage>> = _assistantMessages.asStateFlow()

    private val _isAssistantLoading = MutableStateFlow(false)
    val isAssistantLoading: StateFlow<Boolean> = _isAssistantLoading.asStateFlow()

    // Interactive 3D Viewer State
    private val _active3DModelTarget = MutableStateFlow<String?>(null)
    val active3DModelTarget: StateFlow<String?> = _active3DModelTarget.asStateFlow()

    fun open3DViewer(targetQuery: String) {
        _active3DModelTarget.value = targetQuery
    }

    fun close3DViewer() {
        _active3DModelTarget.value = null
    }

    init {
        // Initial calculation for light time
        calculateLightTime()
        recomputeDilationFactor()
        startTimeDilationClockLoop()
    }

    fun setNavTab(tab: AppNavTab) {
        _currentTab.value = tab
    }

    // --- Space Assistant Actions ---
    fun askSpaceAssistant(query: String) {
        if (query.isBlank()) return
        val userMsg = AssistantMessage(isUser = true, text = query.trim())
        _assistantMessages.update { it + userMsg }
        _isAssistantLoading.value = true

        viewModelScope.launch {
            try {
                val response = geminiService.askSpaceAssistant(query.trim())
                _assistantMessages.update { it + response }
            } catch (e: Exception) {
                val errorMsg = AssistantMessage(
                    isUser = false,
                    text = "Astrophysical query processing encountered an error: ${e.message}",
                    isError = true
                )
                _assistantMessages.update { it + errorMsg }
            } finally {
                _isAssistantLoading.value = false
            }
        }
    }

    fun regenerateMessageImage(messageId: String, style: SpaceImageStyle) {
        val targetMsg = _assistantMessages.value.find { it.id == messageId } ?: return
        val subject = targetMsg.visualTitle ?: targetMsg.directAnswer ?: "Astronomical phenomenon"

        // Update message state to show generating indicator
        _assistantMessages.update { list ->
            list.map { msg ->
                if (msg.id == messageId) {
                    msg.copy(isImageGenerating = true, generatedImageStyle = style, imageGenerationError = null)
                } else msg
            }
        }

        viewModelScope.launch {
            try {
                val (base64, prompt) = geminiService.generateSpaceImage(subject, style)
                val classification = SpaceImageGenerator.determineClassification(subject, style)
                _assistantMessages.update { list ->
                    list.map { msg ->
                        if (msg.id == messageId) {
                            msg.copy(
                                generatedImageBase64 = base64,
                                generatedImagePrompt = prompt,
                                generatedImageStyle = style,
                                imageClassification = classification,
                                isImageGenerating = false
                            )
                        } else msg
                    }
                }
            } catch (e: Exception) {
                _assistantMessages.update { list ->
                    list.map { msg ->
                        if (msg.id == messageId) {
                            msg.copy(
                                isImageGenerating = false,
                                imageGenerationError = "Visualization failed: ${e.message}"
                            )
                        } else msg
                    }
                }
            }
        }
    }

    fun clearAssistantChat() {
        _assistantMessages.value = emptyList()
    }

    // --- Daily Fact Actions ---
    fun selectFact(fact: SpaceFact) {
        _currentFact.value = fact
    }

    fun nextFact() {
        val currentIndex = allFacts.indexOfFirst { it.id == _currentFact.value.id }
        val nextIndex = if (currentIndex in 0 until allFacts.size - 1) currentIndex + 1 else 0
        _currentFact.value = allFacts[nextIndex]
    }

    fun previousFact() {
        val currentIndex = allFacts.indexOfFirst { it.id == _currentFact.value.id }
        val prevIndex = if (currentIndex > 0) currentIndex - 1 else allFacts.size - 1
        _currentFact.value = allFacts[prevIndex]
    }

    fun randomFact() {
        val otherFacts = allFacts.filter { it.id != _currentFact.value.id }
        if (otherFacts.isNotEmpty()) {
            _currentFact.value = otherFacts.random()
        }
    }

    fun openStoryCard(fact: SpaceFact) {
        _storyCardFact.value = fact
    }

    fun closeStoryCard() {
        _storyCardFact.value = null
    }

    // --- Explorer Actions ---
    fun setObjectCategory(category: ObjectCategory) {
        _selectedObjectCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getFilteredObjects(): List<SpaceObject> {
        return repository.getSpaceObjects(_selectedObjectCategory.value, _searchQuery.value)
    }

    fun selectSpaceObject(obj: SpaceObject?) {
        _selectedSpaceObject.value = obj
    }

    // --- Time Lab Actions ---
    fun setTimeLabTab(tab: TimeLabTab) {
        _timeLabTab.value = tab
    }

    fun setLightTimeInput(value: String) {
        _lightTimeState.update { it.copy(inputValue = value, selectedPreset = null) }
        calculateLightTime()
    }

    fun setLightTimeUnit(unit: DistanceUnit) {
        _lightTimeState.update { it.copy(selectedUnit = unit, selectedPreset = null) }
        calculateLightTime()
    }

    fun selectLightTimePreset(preset: LightTimePreset) {
        _lightTimeState.update {
            it.copy(
                selectedPreset = preset,
                inputValue = preset.distanceKm.toString(),
                selectedUnit = DistanceUnit.KM,
                computedTravelSeconds = preset.lightTravelSeconds,
                formattedDuration = preset.lightTravelFormatted,
                earthEra = preset.historicalEarthAnchor
            )
        }
    }

    private fun calculateLightTime() {
        val state = _lightTimeState.value
        val num = state.inputValue.toDoubleOrNull()
        if (num != null && num > 0) {
            val seconds = RelativityEngine.computeLightTravelSecondsFromUnit(num, state.selectedUnit)
            val years = seconds / (365.25 * 86400.0)
            val formatted = RelativityEngine.formatDuration(seconds)
            val era = RelativityEngine.getEarthEraDescription(years)
            _lightTimeState.update {
                it.copy(
                    computedTravelSeconds = seconds,
                    formattedDuration = formatted,
                    earthEra = era
                )
            }
        }
    }

    // --- Time Dilation Actions ---
    fun setDilationMode(mode: DilationMode) {
        _timeDilationState.update { it.copy(mode = mode) }
        recomputeDilationFactor()
    }

    fun setGravityRatio(ratio: Float) {
        _timeDilationState.update { it.copy(gravityRatio = ratio, selectedGravityPreset = null) }
        recomputeDilationFactor()
    }

    fun setVelocityFraction(fraction: Float) {
        _timeDilationState.update { it.copy(velocityFraction = fraction, selectedVelocityPreset = null) }
        recomputeDilationFactor()
    }

    fun selectGravityPreset(preset: GravityPreset) {
        _timeDilationState.update {
            it.copy(
                selectedGravityPreset = preset,
                gravityRatio = preset.ratioRsOverR.toFloat(),
                dilationFactor = preset.dilationFactor
            )
        }
    }

    fun selectVelocityPreset(preset: VelocityPreset) {
        _timeDilationState.update {
            it.copy(
                selectedVelocityPreset = preset,
                velocityFraction = preset.fractionOfC.toFloat(),
                dilationFactor = preset.lorentzFactor
            )
        }
    }

    fun toggleClockRunning() {
        _timeDilationState.update { it.copy(isClockRunning = !it.isClockRunning) }
    }

    fun resetDilationClock() {
        _timeDilationState.update {
            it.copy(travelerElapsedSeconds = 0.0, observerElapsedSeconds = 0.0)
        }
    }

    private fun recomputeDilationFactor() {
        val state = _timeDilationState.value
        val factor = if (state.mode == DilationMode.GRAVITATIONAL) {
            RelativityEngine.computeGravitationalDilation(state.gravityRatio.toDouble())
        } else {
            RelativityEngine.computeVelocityLorentzFactor(state.velocityFraction.toDouble())
        }
        _timeDilationState.update { it.copy(dilationFactor = factor) }
    }

    private fun startTimeDilationClockLoop() {
        viewModelScope.launch {
            while (isActive) {
                delay(100) // 10 ticks per second
                if (_timeDilationState.value.isClockRunning) {
                    val dt = 0.1
                    val factor = _timeDilationState.value.dilationFactor
                    _timeDilationState.update {
                        it.copy(
                            travelerElapsedSeconds = it.travelerElapsedSeconds + dt,
                            observerElapsedSeconds = it.observerElapsedSeconds + (dt * factor)
                        )
                    }
                }
            }
        }
    }

    // --- Quiz Actions ---
    fun startQuiz(difficulty: String = "All") {
        val questions = repository.getQuizQuestions(if (difficulty == "All") null else difficulty).shuffled()
        _quizState.value = QuizState(
            isQuizActive = true,
            isQuizFinished = false,
            selectedDifficulty = difficulty,
            currentQuestions = questions,
            currentIndex = 0,
            selectedOptionIndex = null,
            isAnswerSubmitted = false,
            isCorrect = false,
            currentScore = 0,
            currentStreak = 0,
            highestStreak = 0
        )
    }

    fun selectQuizOption(index: Int) {
        if (!_quizState.value.isAnswerSubmitted) {
            _quizState.update { it.copy(selectedOptionIndex = index) }
        }
    }

    fun submitQuizAnswer() {
        val state = _quizState.value
        val option = state.selectedOptionIndex ?: return
        if (state.isAnswerSubmitted) return

        val currentQ = state.currentQuestions.getOrNull(state.currentIndex) ?: return
        val isRight = (option == currentQ.correctIndex)
        val newScore = if (isRight) state.currentScore + 1 else state.currentScore
        val newStreak = if (isRight) state.currentStreak + 1 else 0
        val newHighest = maxOf(newStreak, state.highestStreak)

        _quizState.update {
            it.copy(
                isAnswerSubmitted = true,
                isCorrect = isRight,
                currentScore = newScore,
                currentStreak = newStreak,
                highestStreak = newHighest
            )
        }
    }

    fun nextQuizQuestion() {
        val state = _quizState.value
        val nextIdx = state.currentIndex + 1
        if (nextIdx < state.currentQuestions.size) {
            _quizState.update {
                it.copy(
                    currentIndex = nextIdx,
                    selectedOptionIndex = null,
                    isAnswerSubmitted = false,
                    isCorrect = false
                )
            }
        } else {
            // Quiz finished
            _quizState.update { it.copy(isQuizActive = false, isQuizFinished = true) }
            viewModelScope.launch {
                repository.recordQuizScore(
                    score = state.currentScore,
                    total = state.currentQuestions.size,
                    difficulty = state.selectedDifficulty
                )
            }
        }
    }

    fun resetQuiz() {
        _quizState.value = QuizState()
    }

    // --- Favorites Actions ---
    fun toggleFavoriteFact(fact: SpaceFact, isFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(
                id = fact.id,
                itemType = "FACT",
                title = fact.title,
                subtitle = "${fact.category} • ${fact.certainty.label}",
                category = fact.category,
                detailText = fact.summary,
                isCurrentlyFavorite = isFav
            )
        }
    }

    fun toggleFavoriteObject(obj: SpaceObject, isFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(
                id = obj.id,
                itemType = "OBJECT",
                title = obj.name,
                subtitle = "${obj.designation} • ${obj.distanceDisplay}",
                category = obj.category.label,
                detailText = obj.overview,
                isCurrentlyFavorite = isFav
            )
        }
    }

    fun removeFavorite(id: String) {
        viewModelScope.launch {
            repository.removeFavorite(id)
        }
    }

    // --- Sharing Action ---
    fun shareFactText(fact: SpaceFact) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "✨ Cosmic Time Fact ✨\n\n${fact.title}\n\n${fact.summary}\n\n🔭 Observation Source: ${fact.observationalSource}\n📜 \"${fact.quote}\"\n\nExplored with Cosmic Time App"
            )
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Cosmic Fact")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplication<Application>().startActivity(shareIntent)
    }

    fun shareObjectText(obj: SpaceObject) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "🌌 Cosmic Object: ${obj.name} (${obj.designation})\n\n${obj.overview}\n\n• Distance: ${obj.distanceDisplay}\n• Mass: ${obj.massDisplay}\n• Gravity: ${obj.gravitationalPullDisplay}\n\n🔬 ${obj.fascinatingMechanics}\n\nExplored with Cosmic Time App"
            )
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Cosmic Object")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplication<Application>().startActivity(shareIntent)
    }
}
