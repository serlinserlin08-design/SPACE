package com.example.data.model

import java.util.UUID

enum class Space3DObjectType(val label: String) {
    PLANET_TERRESTRIAL("Terrestrial Planet"),
    PLANET_GAS_GIANT("Gas Giant Planet"),
    MOON("Natural Satellite / Moon"),
    STAR("Stellar Body / Star"),
    BLACK_HOLE("Black Hole / Singularity"),
    NEUTRON_STAR_PULSAR("Neutron Star / Pulsar"),
    GALAXY_SPIRAL("Spiral Galaxy"),
    GALAXY_ELLIPTICAL("Elliptical Galaxy"),
    NEBULA("Diffuse Nebula / Supernova Remnant"),
    ASTEROID_COMET("Asteroid / Comet"),
    SPACECRAFT_STATION("Spacecraft / Orbital Station"),
    EXOPLANET("Extrasolar Planet")
}

data class Space3DHotspot(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val u: Float, // Longitude angle in degrees (0..360) or X offset
    val v: Float, // Latitude angle in degrees (-90..90) or Y offset
    val radiusRatio: Float = 1.05f
)

data class Space3DModelData(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val scientificDesignation: String = "",
    val objectType: Space3DObjectType,
    val isConfirmedObservationalData: Boolean = true,
    val classificationLabel: String = if (isConfirmedObservationalData) "Scientific 3D Model" else "Scientific visualization / Artist’s 3D model",
    val classificationDisclaimer: String = if (isConfirmedObservationalData) 
        "Constructed using real planetary topography, radar altimetry, and photometric maps."
    else 
        "Astrophysical 3D simulation derived from spectral signatures and General Relativity equations. Not an optical photograph.",
    // Visual Parameters
    val baseRadius: Float = 120f,
    val primaryColorHex: Long = 0xFF3B82F6,
    val secondaryColorHex: Long = 0xFF1E3A8A,
    val accentColorHex: Long = 0xFF93C5FD,
    val texturePattern: String = "smooth", // "continents", "bands", "cratered", "ice_cracks", "granulation", "plasma", "spiral_arms", "filaments", "polygonal_rock", "tech_hull"
    // Atmospheric & Glow
    val hasAtmosphere: Boolean = false,
    val atmosphereColorHex: Long = 0xFF60A5FA,
    val atmosphereThickness: Float = 18f,
    val axialTiltDegrees: Float = 0f,
    val rotationSpeed: Float = 1f,
    // Rings
    val hasRings: Boolean = false,
    val ringInnerRatio: Float = 1.35f,
    val ringOuterRatio: Float = 2.4f,
    val ringPrimaryColorHex: Long = 0xFFE2E8F0,
    val ringSecondaryColorHex: Long = 0xFF94A3B8,
    val ringTiltDegrees: Float = 27f,
    // Black Hole & Relativistic Features
    val hasAccretionDisk: Boolean = false,
    val hasPhotonSphere: Boolean = false,
    val hasRelativisticJets: Boolean = false,
    val jetColorHex: Long = 0xFF38BDF8,
    // Spacecraft Elements
    val hasSolarPanels: Boolean = false,
    val hasHighGainAntenna: Boolean = false,
    val hasHexagonalMirrors: Boolean = false, // JWST
    val hasModuleModules: Boolean = false, // ISS
    // Cometary
    val hasCometTail: Boolean = false,
    // Interactive Hotspots
    val hotspots: List<Space3DHotspot> = emptyList(),
    // Telemetry & Scientific Breakdown
    val distanceDisplay: String = "N/A",
    val radiusDisplay: String = "N/A",
    val massDisplay: String = "N/A",
    val surfaceTemperatureDisplay: String = "N/A",
    val gravityDisplay: String = "N/A",
    val orbitalPeriodDisplay: String = "N/A",
    val compositionOverview: String = "",
    val scientificOverview: String = "",
    val interestingFacts: List<String> = emptyList()
)
