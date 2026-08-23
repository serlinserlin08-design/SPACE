package com.example.data.model

import java.util.UUID

enum class EpistemicStatus(val label: String, val description: String) {
    CONFIRMED_OBSERVATION("Confirmed Science", "Empirically verified by telescopes, direct measurement, or gravitational detectors."),
    THEORETICAL_PHYSICS("Mathematical Theory", "Rigorous physical prediction derived from General Relativity or Quantum Mechanics; awaiting direct observation."),
    COMPUTATIONAL_SIMULATION("Scientific Simulation", "Supercomputer N-body hydrodynamical simulation based on known laws of physics."),
    ESTIMATION_BOUND("Empirical Estimate", "Derived from cosmological statistics, standard candles, and observational confidence intervals."),
    MYTH_VS_REALITY("Debunked / Sci-Fi Concept", "Contrasting popular science fiction concepts with actual astrophysics.")
}

enum class VisualWidgetType {
    NONE,
    EVENT_HORIZON,
    GRAVITATIONAL_TIME_DILATION,
    NEUTRON_STAR_STRUCTURE,
    STELLAR_EVOLUTION,
    GALAXY_SCALE_COMPARISON,
    LIGHT_TRAVEL_CALCULATOR,
    TON_618_SUPERMASSIVE,
    GALAXY_COLLISION,
    SOLAR_SYSTEM_ORBITS,
    VOYAGER_DISTANCE,
    GENERIC_CELESTIAL
}

data class TelemetryHighlight(
    val label: String,
    val value: String,
    val unitOrContext: String = ""
)

enum class SpaceVisualClassification(val badge: String, val disclaimer: String) {
    AI_VISUALIZATION(
        "AI-Generated Visualization",
        "Generated using physics-based visual parameters • Not a direct optical photograph"
    ),
    ARTIST_IMPRESSION(
        "Artist's Impression",
        "Scientific illustration based on known spectral and orbital data"
    ),
    TELESCOPE_SIMULATION(
        "Simulated Telescope View",
        "Modeled optics & diffraction spikes representing JWST / Hubble instrument view"
    ),
    PHYSICS_3D_RENDER(
        "3D Relativistic Physics Render",
        "Ray-traced spacetime curvature and relativistic Doppler distortion"
    ),
    CONFIRMED_TELESCOPE_PHOTO(
        "Direct Observation Reference",
        "Direct radio/optical synthesis data (e.g., EHT, JWST, Hubble)"
    )
}

enum class SpaceImageStyle(val displayName: String, val promptModifier: String) {
    PHOTOREALISTIC(
        "Photorealistic",
        "ultra-photorealistic deep space capture, 8k resolution, authentic physically accurate astronomical lighting, high dynamic range"
    ),
    SCIENTIFIC_ILLUSTRATION(
        "Scientific Illustration",
        "NASA technical scientific illustration, clean cutaway annotations, precise astrophysical structure, educational diagrammatic clarity"
    ),
    CINEMATIC(
        "Cinematic",
        "epic cinematic IMAX composition, dramatic volumetric nebular lighting, rich depth of field, blockbuster interstellar scale"
    ),
    NASA_STYLE(
        "NASA-style Visualization",
        "official NASA/JPL/ESA scientific visualization, authentic spacecraft telemetry aesthetics, accurate planetary science colors"
    ),
    TELESCOPE_STYLE(
        "Telescope-style",
        "James Webb Space Telescope (JWST) NIRCam / Hubble deep field imaging, 6-point diffraction spikes, narrowband cosmic filter palette"
    ),
    RENDER_3D(
        "3D Scientific Render",
        "Octane 3D ray-traced relativistic physics simulation, volumetric particle scattering, mathematically accurate Schwarzschild metrics"
    ),
    ARTISTIC(
        "Artistic",
        "breathtaking celestial fine concept art, ethereal cosmic color harmonies, vibrant glowing interstellar dust and starlight"
    );

    companion object {
        fun fromString(styleStr: String?): SpaceImageStyle {
            if (styleStr.isNullOrBlank()) return PHOTOREALISTIC
            val lower = styleStr.lowercase()
            return when {
                lower.contains("photo") -> PHOTOREALISTIC
                lower.contains("illustrat") -> SCIENTIFIC_ILLUSTRATION
                lower.contains("cinema") -> CINEMATIC
                lower.contains("nasa") || lower.contains("esa") -> NASA_STYLE
                lower.contains("telescope") || lower.contains("jwst") || lower.contains("hubble") -> TELESCOPE_STYLE
                lower.contains("3d") || lower.contains("render") -> RENDER_3D
                lower.contains("art") || lower.contains("paint") -> ARTISTIC
                else -> PHOTOREALISTIC
            }
        }
    }
}

data class AssistantMessage(
    val id: String = UUID.randomUUID().toString(),
    val isUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val directAnswer: String? = null,
    val deepExplanation: String? = null,
    val epistemicStatus: EpistemicStatus = EpistemicStatus.CONFIRMED_OBSERVATION,
    val visualType: VisualWidgetType = VisualWidgetType.NONE,
    val visualTitle: String? = null,
    val visualCaption: String? = null,
    val visualSourceType: String? = null, // e.g. "Artist Impression (NASA/ESA)", "Direct Radio Synthesis (EHT)"
    val telemetryHighlights: List<TelemetryHighlight> = emptyList(),
    val distanceIntuition: String? = null,
    val sourcesCited: List<String> = emptyList(),
    val followUpQuestions: List<String> = emptyList(),
    // Image Generation Fields
    val generatedImageBase64: String? = null,
    val generatedImagePrompt: String? = null,
    val generatedImageStyle: SpaceImageStyle? = null,
    val imageClassification: SpaceVisualClassification = SpaceVisualClassification.AI_VISUALIZATION,
    val isImageGenerating: Boolean = false,
    val imageGenerationError: String? = null,
    val space3DQueryTarget: String? = null,
    val isError: Boolean = false,
    val isLoading: Boolean = false
)

data class SuggestedPrompt(
    val category: String,
    val icon: String,
    val question: String
)
