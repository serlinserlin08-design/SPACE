package com.example.data.model

import kotlin.math.sqrt

data class GravityPreset(
    val id: String,
    val name: String,
    val description: String,
    val ratioRsOverR: Double, // r_s / r (0.0 to 0.99)
    val dilationFactor: Double, // 1 / sqrt(1 - r_s/r)
    val humanComparison: String
)

data class VelocityPreset(
    val id: String,
    val name: String,
    val fractionOfC: Double, // v/c
    val lorentzFactor: Double, // gamma = 1 / sqrt(1 - v^2/c^2)
    val humanComparison: String
)

object RelativityEngine {

    val gravityPresets: List<GravityPreset> = listOf(
        GravityPreset(
            id = "earth",
            name = "Earth Surface (1 g)",
            description = "Earth's gravitational well is mild. Clocks on Earth tick only slightly slower than clocks in deep space.",
            ratioRsOverR = 0.0000000014, // (r_s ~ 9mm / 6371km)
            dilationFactor = 1.0000000007,
            humanComparison = "1 hour on Earth = 1 hour + 2.5 picoseconds in deep space (adds up to 0.02 seconds over a human lifetime)."
        ),
        GravityPreset(
            id = "sun",
            name = "Solar Surface",
            description = "The Sun's mass curves local spacetime noticeably. Tested by Shapiro time-delay experiments.",
            ratioRsOverR = 0.00000424,
            dilationFactor = 1.00000212,
            humanComparison = "1 hour on the Sun = 1 hour + 7.6 milliseconds far away."
        ),
        GravityPreset(
            id = "white_dwarf",
            name = "White Dwarf (Sirius B)",
            description = "Earth-sized core of a dead star holding the mass of our entire Sun.",
            ratioRsOverR = 0.0006,
            dilationFactor = 1.000300,
            humanComparison = "1 hour on Sirius B = 1 hour + 1.08 seconds for a distant observer."
        ),
        GravityPreset(
            id = "neutron_star",
            name = "Neutron Star Surface",
            description = "Extremely dense matter where 1.4 solar masses are compressed into a 12 km ball.",
            ratioRsOverR = 0.35,
            dilationFactor = 1.2403,
            humanComparison = "1 hour on a neutron star = 1 hour 14 minutes and 25 seconds on Earth."
        ),
        GravityPreset(
            id = "event_horizon_near",
            name = "Near Event Horizon (1.1 rs)",
            description = "Hovering just outside the Schwarzschild radius of a supermassive black hole.",
            ratioRsOverR = 0.909,
            dilationFactor = 3.316,
            humanComparison = "1 hour hovering here = 3 hours 19 minutes on Earth."
        ),
        GravityPreset(
            id = "extreme_gargantua",
            name = "Extreme Orbit (Miller's Planet)",
            description = "Deep in the gravitational well of a rapidly spinning supermassive black hole.",
            ratioRsOverR = 0.99999999997,
            dilationFactor = 61320.0,
            humanComparison = "1 hour on this planet = 7 full Earth years! (Every second is ~17 hours on Earth)."
        )
    )

    val velocityPresets: List<VelocityPreset> = listOf(
        VelocityPreset(
            id = "iss",
            name = "ISS Orbital Speed (7.66 km/s)",
            fractionOfC = 0.0000255,
            lorentzFactor = 1.0000000003,
            humanComparison = "Astronauts age ~0.005 seconds less than Earth-bound humans after a 6-month mission."
        ),
        VelocityPreset(
            id = "parker_probe",
            name = "Parker Solar Probe (192 km/s)",
            fractionOfC = 0.00064,
            lorentzFactor = 1.0000002,
            humanComparison = "Fastest human-made craft; time dilation is measurable by on-board atomic clocks."
        ),
        VelocityPreset(
            id = "half_c",
            name = "Relativistic Cruise (0.50 c)",
            fractionOfC = 0.50,
            lorentzFactor = 1.1547,
            humanComparison = "1 hour at half light speed = 1 hour 9 minutes and 17 seconds on Earth."
        ),
        VelocityPreset(
            id = "high_c",
            name = "Interstellar Fast-Travel (0.90 c)",
            fractionOfC = 0.90,
            lorentzFactor = 2.2941,
            humanComparison = "1 hour at 0.90c = 2 hours 17 minutes on Earth. You age less than half as fast!"
        ),
        VelocityPreset(
            id = "ultra_c",
            name = "Extreme Relativistic Sail (0.99 c)",
            fractionOfC = 0.99,
            lorentzFactor = 7.0888,
            humanComparison = "1 hour aboard = 7 hours 5 minutes on Earth. A 5-year journey covers 35 Earth years."
        ),
        VelocityPreset(
            id = "photon_border",
            name = "Near-Photon Limit (0.9999 c)",
            fractionOfC = 0.9999,
            lorentzFactor = 70.712,
            humanComparison = "1 hour aboard = 2 days and 22 hours on Earth. Time for you is stretched over 70x."
        )
    )

    fun computeGravitationalDilation(ratioRsOverR: Double): Double {
        val safeRatio = ratioRsOverR.coerceIn(0.0, 0.9999999)
        val denom = sqrt(1.0 - safeRatio)
        return if (denom > 0.0) 1.0 / denom else 100000.0
    }

    fun computeVelocityLorentzFactor(fractionOfC: Double): Double {
        val beta = fractionOfC.coerceIn(0.0, 0.9999999)
        val denom = sqrt(1.0 - (beta * beta))
        return if (denom > 0.0) 1.0 / denom else 100000.0
    }

    fun formatDuration(seconds: Double): String {
        return when {
            seconds < 0.000001 -> String.format("%.2f nanoseconds", seconds * 1e9)
            seconds < 0.001 -> String.format("%.2f microseconds", seconds * 1e6)
            seconds < 1.0 -> String.format("%.2f milliseconds", seconds * 1e3)
            seconds < 60.0 -> String.format("%.2f seconds", seconds)
            seconds < 3600.0 -> {
                val mins = (seconds / 60.0).toInt()
                val remSec = (seconds % 60.0).toInt()
                "$mins min $remSec sec"
            }
            seconds < 86400.0 -> {
                val hrs = (seconds / 3600.0).toInt()
                val mins = ((seconds % 3600.0) / 60.0).toInt()
                "$hrs hr $mins min"
            }
            seconds < 31557600.0 -> {
                val days = (seconds / 86400.0).toInt()
                val hrs = ((seconds % 86400.0) / 3600.0).toInt()
                "$days days $hrs hrs"
            }
            seconds < 31557600.0 * 1000.0 -> {
                val years = seconds / 31557600.0
                String.format("%.2f years", years)
            }
            seconds < 31557600.0 * 1000000.0 -> {
                val millennia = seconds / (31557600.0 * 1000.0)
                String.format("%.2f thousand years", millennia)
            }
            else -> {
                val millionYears = seconds / (31557600.0 * 1000000.0)
                if (millionYears >= 1000.0) {
                    String.format("%.2f billion years", millionYears / 1000.0)
                } else {
                    String.format("%.2f million years", millionYears)
                }
            }
        }
    }

    fun computeLightTravelSecondsFromUnit(value: Double, unit: DistanceUnit): Double {
        val km = when (unit) {
            DistanceUnit.KM -> value
            DistanceUnit.AU -> value * 149597870.7
            DistanceUnit.LIGHT_YEARS -> value * 9.460730472e12
            DistanceUnit.PARSECS -> value * 3.085677581e13
        }
        val speedOfLightKmPerSec = 299792.458
        return km / speedOfLightKmPerSec
    }

    fun getEarthEraDescription(lightTravelYears: Double): String {
        return when {
            lightTravelYears < 0.0001 -> "Present moment — happening almost simultaneously with human awareness."
            lightTravelYears < 1.0 -> "Within the last few months on Earth."
            lightTravelYears < 10.0 -> "Earlier in your current decade on Earth."
            lightTravelYears < 100.0 -> "During the 20th century (aviation, space race, computers born)."
            lightTravelYears < 500.0 -> "During the Renaissance and early exploration era on Earth."
            lightTravelYears < 2000.0 -> "Around the Roman Empire and classical antiquity."
            lightTravelYears < 5000.0 -> "Ancient civilizations building the Great Pyramids of Giza."
            lightTravelYears < 12000.0 -> "Dawn of human agriculture at the end of the last Ice Age."
            lightTravelYears < 50000.0 -> "Neanderthals and early Homo sapiens sharing Eurasia."
            lightTravelYears < 300000.0 -> "Emergence of anatomically modern Homo sapiens in Africa."
            lightTravelYears < 2500000.0 -> "Early hominids (Australopithecus) walking the African savanna."
            lightTravelYears < 66000000.0 -> "The Cretaceous period — Tyrannosaurus Rex and Triceratops roamed Earth."
            lightTravelYears < 250000000.0 -> "Early dinosaurs emerging across the supercontinent Pangea."
            lightTravelYears < 540000000.0 -> "The Cambrian Explosion of complex marine life."
            lightTravelYears < 4500000000.0 -> "The formation of Earth from the solar protoplanetary disk."
            else -> "The cosmic dark ages and cosmic dawn, shortly after the Big Bang."
        }
    }
}

enum class DistanceUnit(val symbol: String, val label: String) {
    KM("km", "Kilometers"),
    AU("AU", "Astronomical Units"),
    LIGHT_YEARS("ly", "Light-Years"),
    PARSECS("pc", "Parsecs")
}
