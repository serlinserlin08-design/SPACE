package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import com.example.data.datasource.SpaceAssistantOfflineKnowledge
import com.example.data.datasource.SpaceImageGenerator
import com.example.data.model.AssistantMessage
import com.example.data.model.EpistemicStatus
import com.example.data.model.SpaceImageStyle
import com.example.data.model.SpaceVisualClassification
import com.example.data.model.TelemetryHighlight
import com.example.data.model.VisualWidgetType
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// --- Gemini Request / Response DTOs ---

@JsonClass(generateAdapter = true)
data class GeminiGenerateRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null,
    val generationConfig: GeminiGenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    val parts: List<GeminiPart>,
    val role: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    val text: String? = null,
    val inlineData: GeminiInlineData? = null
)

@JsonClass(generateAdapter = true)
data class GeminiInlineData(
    val mimeType: String? = null,
    val data: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    val temperature: Float? = null,
    val topP: Float? = null,
    val topK: Int? = null,
    val responseModalities: List<String>? = null,
    val imageConfig: GeminiImageConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiImageConfig(
    val aspectRatio: String = "16:9",
    val imageSize: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiGenerateResponse(
    val candidates: List<GeminiCandidate>? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    val content: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class StructuredSpacePayload(
    val directAnswer: String = "",
    val deepExplanation: String = "",
    val epistemicStatus: String = "CONFIRMED_OBSERVATION",
    val visualType: String = "GENERIC_CELESTIAL",
    val visualTitle: String = "",
    val visualCaption: String = "",
    val visualSourceType: String = "Astrophysics Simulation & Model",
    val isImageRequested: Boolean = false,
    val imageSubject: String? = null,
    val imagePrompt: String? = null,
    val imageStyle: String? = null,
    val imageClassification: String? = null,
    val telemetryHighlights: List<StructuredTelemetry>? = null,
    val distanceIntuition: String? = null,
    val sourcesCited: List<String>? = null,
    val followUpQuestions: List<String>? = null
)

@JsonClass(generateAdapter = true)
data class StructuredTelemetry(
    val label: String,
    val value: String,
    val unitOrContext: String = ""
)

interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiGenerateRequest
    ): GeminiGenerateResponse

    @POST("v1beta/models/gemini-2.5-flash-image:generateContent")
    suspend fun generateImage(
        @Query("key") apiKey: String,
        @Body request: GeminiGenerateRequest
    ): GeminiGenerateResponse
}

class GeminiSpaceService {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(45, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .writeTimeout(45, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val api = retrofit.create(GeminiApi::class.java)

    private val systemPrompt = """
        You are an intelligent, visual, scientifically rigorous SPACE EXPLORATION ASSISTANT for the "Cosmic Time" Android app.
        
        CORE PRINCIPLES:
        1. Understand any query even with typos, casual slang, mixed languages, or conversational tone.
        2. Deliver a clear, friendly, and authoritative answer first, followed by a deeper scientific breakdown.
        3. Explain complex physics (relativity, quantum phenomena, orbital mechanics, thermodynamics, cosmological expansion) in accessible, beginner-friendly language WITHOUT sacrificing scientific precision.
        4. Never invent scientific facts. If theoretical, disputed, or unknown, explicitly say so.
        5. For numerical/distance questions, include intuitive analogies (light-seconds, light-minutes, AU, light-years).
        6. Cite authoritative scientific bodies (NASA, ESA, Event Horizon Telescope, JWST, LIGO, Hubble, Planck).
        
        IMAGE GENERATION PROTOCOL:
        When the user asks to GENERATE, CREATE, DRAW, VISUALIZE, SHOW, RENDER, or asks WHAT SOMETHING LOOKS LIKE for ANY space-related object, event, location, or concept:
        - Set "isImageRequested": true
        - Set "imageSubject": The precise astronomical object (e.g. "Rotating Kerr Black Hole", "Supermassive Black Hole TON 618", "Colliding Galaxies Milky Way and Andromeda", "Neutron Star Pulsar", "Quasar with Relativistic Jets", "Exoplanet with Dense Rings", "Supernova Remnant Shockwave").
        - Set "imagePrompt": A highly detailed, scientifically grounded image prompt describing the subject with accurate physics (accretion disk, gravitational lensing, photon sphere, Doppler beaming, magnetic field lines, relativistic jets, tidal tails, atmospheric haze, lighting, composition, 8k quality).
        - Set "imageStyle": The requested style ("Photorealistic", "Scientific Illustration", "Cinematic", "NASA-style Visualization", "Telescope-style", "3D Scientific Render", "Artistic"). If not specified, default to "Photorealistic".
        - Set "imageClassification": "AI_VISUALIZATION" | "ARTIST_IMPRESSION" | "TELESCOPE_SIMULATION" | "PHYSICS_3D_RENDER".
        
        CRITICAL OUTPUT FORMAT:
        You MUST respond ONLY with valid JSON conforming to this structure:
        {
          "directAnswer": "Short 1-2 sentence core answer",
          "deepExplanation": "Structured explanation with bullet points and bold section headers (e.g. • **Section Name**: Description). Define technical terms upon first use.",
          "epistemicStatus": "CONFIRMED_OBSERVATION" | "THEORETICAL_PHYSICS" | "COMPUTATIONAL_SIMULATION" | "ESTIMATION_BOUND" | "MYTH_VS_REALITY",
          "visualType": "EVENT_HORIZON" | "GRAVITATIONAL_TIME_DILATION" | "NEUTRON_STAR_STRUCTURE" | "STELLAR_EVOLUTION" | "GALAXY_SCALE_COMPARISON" | "LIGHT_TRAVEL_CALCULATOR" | "TON_618_SUPERMASSIVE" | "GALAXY_COLLISION" | "SOLAR_SYSTEM_ORBITS" | "VOYAGER_DISTANCE" | "GENERIC_CELESTIAL" | "NONE",
          "visualTitle": "Concise title for visual simulation or diagram",
          "visualCaption": "Caption explaining what the diagram/simulation represents",
          "visualSourceType": "Artist Impression (NASA/ESA)" | "Direct Observation (EHT/JWST)" | "Theoretical Model (General Relativity)" | "N-Body Simulation",
          "isImageRequested": true,
          "imageSubject": "Exact subject name",
          "imagePrompt": "Detailed optimized image generation prompt",
          "imageStyle": "Photorealistic" | "Scientific Illustration" | "Cinematic" | "NASA-style Visualization" | "Telescope-style" | "3D Scientific Render" | "Artistic",
          "imageClassification": "AI_VISUALIZATION" | "ARTIST_IMPRESSION" | "TELESCOPE_SIMULATION" | "PHYSICS_3D_RENDER",
          "telemetryHighlights": [
            {"label": "Metric Name", "value": "123 Units", "unitOrContext": "Context comparison"}
          ],
          "distanceIntuition": "Intuitive comparison for distance or scale",
          "sourcesCited": ["NASA", "ESA", "Event Horizon Telescope"],
          "followUpQuestions": ["Question 1?", "Question 2?", "Question 3?"]
        }
        
        Do not include markdown code block backticks around the json if possible, or provide standard json.
    """.trimIndent()

    suspend fun askSpaceAssistant(userQuery: String): AssistantMessage = withContext(Dispatchers.IO) {
        val cleanQuery = userQuery.trim()
        val isExplicitImageQuery = SpaceImageGenerator.isImageRequest(cleanQuery)
        val requestedStyle = SpaceImageGenerator.detectStyle(cleanQuery)

        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        var baseMessage: AssistantMessage? = null
        var imagePromptToUse: String? = null
        var subjectToUse: String = cleanQuery
        var styleToUse: SpaceImageStyle = requestedStyle
        var classificationToUse: SpaceVisualClassification = SpaceVisualClassification.AI_VISUALIZATION
        var shouldGenerateImage = isExplicitImageQuery

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val request = GeminiGenerateRequest(
                    contents = listOf(
                        GeminiContent(
                            parts = listOf(GeminiPart(text = "User Question: $cleanQuery\n\nGenerate structured space assistant JSON response:"))
                        )
                    ),
                    systemInstruction = GeminiContent(
                        parts = listOf(GeminiPart(text = systemPrompt))
                    ),
                    generationConfig = GeminiGenerationConfig(temperature = 0.2f)
                )

                val response = api.generateContent(apiKey, request)
                val rawText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text

                if (rawText != null) {
                    val parsed = parseJsonResponse(rawText)
                    if (parsed != null) {
                        baseMessage = parsed.message
                        if (parsed.payload.isImageRequested || isExplicitImageQuery) {
                            shouldGenerateImage = true
                            subjectToUse = parsed.payload.imageSubject?.ifBlank { cleanQuery } ?: cleanQuery
                            styleToUse = SpaceImageStyle.fromString(parsed.payload.imageStyle)
                            imagePromptToUse = parsed.payload.imagePrompt
                            classificationToUse = SpaceImageGenerator.determineClassification(subjectToUse, styleToUse)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("GeminiSpaceService", "Gemini API text call error: ${e.message}", e)
            }
        }

        // If no base message yet, fallback to offline knowledge or generic answer
        if (baseMessage == null) {
            val offline = SpaceAssistantOfflineKnowledge.findKnowledge(cleanQuery)
            baseMessage = offline ?: fallbackGenericAnswer(cleanQuery)
        }

        // If image generation was requested, synthesize image
        if (shouldGenerateImage) {
            if (imagePromptToUse.isNullOrBlank()) {
                imagePromptToUse = SpaceImageGenerator.buildOptimizedImagePrompt(subjectToUse, styleToUse)
            }
            classificationToUse = SpaceImageGenerator.determineClassification(subjectToUse, styleToUse)

            val generatedImageBase64 = generateImageWithFallback(
                apiKey = apiKey,
                prompt = imagePromptToUse,
                subject = subjectToUse,
                style = styleToUse
            )

            return@withContext baseMessage.copy(
                generatedImageBase64 = generatedImageBase64,
                generatedImagePrompt = imagePromptToUse,
                generatedImageStyle = styleToUse,
                imageClassification = classificationToUse
            )
        }

        return@withContext baseMessage
    }

    /**
     * Generates a space image in the requested style for a specific subject or prompt.
     */
    suspend fun generateSpaceImage(
        subject: String,
        style: SpaceImageStyle = SpaceImageStyle.PHOTOREALISTIC
    ): Pair<String, String> = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }
        val prompt = SpaceImageGenerator.buildOptimizedImagePrompt(subject, style)
        val imageBase64 = generateImageWithFallback(
            apiKey = apiKey,
            prompt = prompt,
            subject = subject,
            style = style
        )
        Pair(imageBase64, prompt)
    }

    private suspend fun generateImageWithFallback(
        apiKey: String,
        prompt: String,
        subject: String,
        style: SpaceImageStyle
    ): String = withContext(Dispatchers.IO) {
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val imageRequest = GeminiGenerateRequest(
                    contents = listOf(
                        GeminiContent(
                            parts = listOf(GeminiPart(text = prompt))
                        )
                    ),
                    generationConfig = GeminiGenerationConfig(
                        responseModalities = listOf("TEXT", "IMAGE"),
                        imageConfig = GeminiImageConfig(aspectRatio = "16:9")
                    )
                )

                val imageResponse = api.generateImage(apiKey, imageRequest)
                val base64Data = imageResponse.candidates
                    ?.firstOrNull()
                    ?.content
                    ?.parts
                    ?.firstOrNull { it.inlineData?.data != null }
                    ?.inlineData
                    ?.data

                if (!base64Data.isNullOrBlank()) {
                    return@withContext base64Data
                }
            } catch (e: Exception) {
                Log.w("GeminiSpaceService", "Image generation API error: ${e.message}, utilizing procedural synthesis fallback")
            }
        }

        // Procedural Physics & Astronomical Bitmap Synthesis Fallback
        val bitmap = SpaceImageGenerator.generateProceduralSpaceBitmap(subject, style)
        return@withContext SpaceImageGenerator.bitmapToBase64(bitmap)
    }

    private data class ParsedResult(
        val message: AssistantMessage,
        val payload: StructuredSpacePayload
    )

    private fun parseJsonResponse(raw: String): ParsedResult? {
        try {
            var cleaned = raw.trim()
            if (cleaned.startsWith("```json")) {
                cleaned = cleaned.removePrefix("```json").trim()
            }
            if (cleaned.startsWith("```")) {
                cleaned = cleaned.removePrefix("```").trim()
            }
            if (cleaned.endsWith("```")) {
                cleaned = cleaned.removeSuffix("```").trim()
            }

            val adapter = moshi.adapter(StructuredSpacePayload::class.java)
            val payload = adapter.fromJson(cleaned) ?: return null

            val status = try {
                EpistemicStatus.valueOf(payload.epistemicStatus)
            } catch (e: Exception) {
                EpistemicStatus.CONFIRMED_OBSERVATION
            }

            val visual = try {
                VisualWidgetType.valueOf(payload.visualType)
            } catch (e: Exception) {
                VisualWidgetType.GENERIC_CELESTIAL
            }

            val telemetries = payload.telemetryHighlights?.map {
                TelemetryHighlight(it.label, it.value, it.unitOrContext)
            } ?: emptyList()

            val msg = AssistantMessage(
                isUser = false,
                text = "",
                directAnswer = payload.directAnswer,
                deepExplanation = payload.deepExplanation,
                epistemicStatus = status,
                visualType = visual,
                visualTitle = payload.visualTitle.ifEmpty { "Astrophysical Analysis" },
                visualCaption = payload.visualCaption,
                visualSourceType = payload.visualSourceType,
                telemetryHighlights = telemetries,
                distanceIntuition = payload.distanceIntuition,
                sourcesCited = payload.sourcesCited ?: listOf("NASA / ESA Astrophysics Archive"),
                followUpQuestions = payload.followUpQuestions ?: emptyList()
            )

            return ParsedResult(msg, payload)
        } catch (e: Exception) {
            Log.w("GeminiSpaceService", "JSON parsing failed, falling back to raw formatting: ${e.message}")
            return null
        }
    }

    private fun fallbackGenericAnswer(query: String, errorNote: String? = null): AssistantMessage {
        val cleanQuery = query.trim()
        val offline = SpaceAssistantOfflineKnowledge.findKnowledge(cleanQuery)
        if (offline != null) return offline

        return AssistantMessage(
            isUser = false,
            text = "",
            directAnswer = "Astrophysical insight regarding: \"$cleanQuery\"",
            deepExplanation = "• **Universal Principles**: In our standard cosmological model (Lambda-CDM), physical phenomena are governed by General Relativity on macro cosmic scales and the Standard Model on quantum scales.\n• **Observation & Data**: Modern astronomical discoveries utilize space observatories like the James Webb Space Telescope (JWST) and ground-based interferometers to decode celestial phenomena.\n• **Continuous Investigation**: Space science advances rapidly with multi-messenger astronomy (photons, gravitational waves, and high-energy neutrinos).",
            epistemicStatus = EpistemicStatus.CONFIRMED_OBSERVATION,
            visualType = VisualWidgetType.GENERIC_CELESTIAL,
            visualTitle = "Cosmic Phenomenon Observation",
            visualCaption = "Relativistic spacetime and celestial mechanics representation.",
            visualSourceType = "Standard Astrophysical Model",
            telemetryHighlights = listOf(
                TelemetryHighlight("Speed of Light (c)", "299,792 km/s", "Universal Limit"),
                TelemetryHighlight("Cosmic Age", "13.787 ± 0.020 Gyr", "Planck Collaboration"),
                TelemetryHighlight("Observable Diameter", "~93 Billion ly", "Metric Expansion")
            ),
            distanceIntuition = "The universe is continuously expanding, and light travels across cosmic voids over billions of years.",
            sourcesCited = listOf("NASA Astrophysics Division", "European Southern Observatory (ESO)", "Planck Cosmological Collaboration"),
            followUpQuestions = listOf(
                "How does general relativity curve space?",
                "What is the fate of the universe?",
                "How do telescopes see into the past?"
            )
        )
    }
}
