package com.example.data.model

enum class ScientificCertainty(val label: String, val badgeColorHex: Long) {
    CONFIRMED_OBSERVATION("Confirmed Observation", 0xFF10B981),
    ROBUST_THEORY("Robust Theory", 0xFF38BDF8),
    THEORETICAL_MODEL("Theoretical / Estimated", 0xFFF59E0B)
}

data class SpaceFact(
    val id: String,
    val title: String,
    val summary: String,
    val detailedExplanation: String,
    val category: String,
    val certainty: ScientificCertainty,
    val observationalSource: String,
    val quote: String,
    val dayOfYear: Int
)

enum class ObjectCategory(val label: String) {
    ALL("All Objects"),
    BLACK_HOLE("Black Holes"),
    NEUTRON_STAR_PULSAR("Neutron Stars & Pulsars"),
    GALAXY("Galaxies"),
    EXOPLANET("Exoplanets"),
    QUASAR_NEBULA("Quasars & Nebulae")
}

enum class ScientificStatus(val label: String) {
    OBSERVED_CONFIRMED("Observed & Confirmed"),
    CANDIDATE_STUDY("Under Active Study"),
    THEORETICAL_EXTRAPOLATION("Theoretical Extrapolation")
}

data class SpaceObject(
    val id: String,
    val name: String,
    val designation: String,
    val category: ObjectCategory,
    val status: ScientificStatus,
    val distanceLightYears: Double,
    val distanceDisplay: String,
    val massDisplay: String,
    val radiusDisplay: String,
    val temperatureDisplay: String,
    val gravitationalPullDisplay: String,
    val discoveryYear: String,
    val overview: String,
    val fascinatingMechanics: String,
    val imageDrawableRes: Int? = null,
    val tags: List<String>
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val scientificExplanation: String,
    val category: String,
    val difficulty: String
)

data class LightTimePreset(
    val id: String,
    val name: String,
    val distanceDisplay: String,
    val distanceKm: Double,
    val lightTravelSeconds: Double,
    val lightTravelFormatted: String,
    val historicalEarthAnchor: String,
    val scientificNote: String
)
