package com.example.data.datasource

import com.example.data.model.Space3DHotspot
import com.example.data.model.Space3DModelData
import com.example.data.model.Space3DObjectType
import com.example.data.model.SpaceObject
import java.util.Locale

object Space3DModelGenerator {

    /**
     * Resolves or dynamically synthesizes an interactive 3D model configuration for ANY query.
     */
    fun getModelForQuery(query: String, spaceObject: SpaceObject? = null): Space3DModelData {
        val q = query.trim().lowercase(Locale.ROOT)

        // 1. Direct Presets for Common Celestial Objects
        when {
            // SATURN
            q.contains("saturn") -> return createSaturnModel()
            // MARS
            q.contains("mars") -> return createMarsModel()
            // JUPITER
            q.contains("jupiter") -> return createJupiterModel()
            // EARTH
            q.contains("earth") -> return createEarthModel()
            // MOON
            q == "moon" || q.contains("luna") -> return createMoonModel()
            // SUN
            q == "sun" || q.contains("sol") -> return createSunModel()
            // BLACK HOLE / TON 618 / SAGITTARIUS A* / M87*
            q.contains("ton 618") -> return createTon618Model()
            q.contains("sagittarius a") || q.contains("sgra") -> return createSagittariusAStarModel()
            q.contains("m87*") || q.contains("m87 black hole") -> return createM87BlackHoleModel()
            q.contains("black hole") || q.contains("singularity") || q.contains("event horizon") -> return createGenericBlackHoleModel(query)
            // ANDROMEDA GALAXY
            q.contains("andromeda") || q.contains("m31") -> return createAndromedaModel()
            // MILKY WAY
            q.contains("milky way") -> return createMilkyWayModel()
            // NEBULA / PILLARS OF CREATION / ORION
            q.contains("orion") && q.contains("nebula") -> return createOrionNebulaModel()
            q.contains("pillars of creation") || q.contains("eagle nebula") -> return createPillarsOfCreationModel()
            q.contains("crab nebula") -> return createCrabNebulaModel()
            // NEUTRON STAR / PULSAR
            q.contains("pulsar") || q.contains("neutron star") || q.contains("magnetar") -> return createPulsarModel(query)
            // SPACECRAFT / ISS / JWST / VOYAGER
            q.contains("iss") || q.contains("international space station") || q.contains("space station") -> return createISSModel()
            q.contains("jwst") || q.contains("james webb") -> return createJWSTModel()
            q.contains("voyager") -> return createVoyagerModel()
            q.contains("hubble") -> return createHubbleModel()
            // EXOPLANETS
            q.contains("trappist") -> return createTrappist1eModel()
            q.contains("kepler") || q.contains("exoplanet") || q.contains("55 cancri") || q.contains("proxima b") -> return createExoplanetModel(query)
            // ASTEROIDS / COMETS
            q.contains("halley") -> return createHalleyCometModel()
            q.contains("bennu") || q.contains("ceres") || q.contains("vesta") || q.contains("asteroid") -> return createAsteroidModel(query)
            q.contains("comet") -> return createCometModel(query)
            // STARS
            q.contains("betelgeuse") -> return createBetelgeuseModel()
            q.contains("sirius") -> return createSiriusModel()
            q.contains("proxima centauri") -> return createProximaCentauriModel()
        }

        // 2. If a SpaceObject from local database matches, convert intelligently
        if (spaceObject != null) {
            return fromSpaceObject(spaceObject)
        }

        // 3. Dynamic Procedural Synthesis for ANY arbitrary search string
        return synthesizeDynamicModel(query)
    }

    private fun createSaturnModel(): Space3DModelData {
        return Space3DModelData(
            id = "saturn_3d",
            name = "Saturn",
            scientificDesignation = "Sol VI • Gas Giant",
            objectType = Space3DObjectType.PLANET_GAS_GIANT,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "High-resolution 3D topography and photometric ring data derived from Cassini-Huygens mission telemetry.",
            baseRadius = 110f,
            primaryColorHex = 0xFFE2C992,
            secondaryColorHex = 0xFFC9A86A,
            accentColorHex = 0xFFFDF6E2,
            texturePattern = "bands",
            hasAtmosphere = true,
            atmosphereColorHex = 0xFFFDE68A,
            atmosphereThickness = 14f,
            axialTiltDegrees = 26.73f,
            rotationSpeed = 0.9f,
            hasRings = true,
            ringInnerRatio = 1.35f,
            ringOuterRatio = 2.45f,
            ringPrimaryColorHex = 0xFFE6D5B8,
            ringSecondaryColorHex = 0xFFA89474,
            ringTiltDegrees = 26.73f,
            distanceDisplay = "1.43 Billion km (9.58 AU)",
            radiusDisplay = "58,232 km (9.13x Earth)",
            massDisplay = "5.683 × 10²⁶ kg (95.2x Earth)",
            surfaceTemperatureDisplay = "-140 °C (Cloud tops)",
            gravityDisplay = "10.44 m/s² (1.06 g)",
            orbitalPeriodDisplay = "29.45 Earth Years",
            compositionOverview = "96.3% Molecular Hydrogen, 3.25% Helium, traces of Methane and Ammonia ice crystals.",
            scientificOverview = "Saturn is the sixth planet from the Sun and the second-largest in the Solar System. Its iconic ring system spans up to 282,000 km across while only averaging ~10 to 30 meters in thickness, composed of 99% pure water ice particles.",
            interestingFacts = listOf(
                "Saturn's density is so low (0.687 g/cm³) that it would float in a giant cosmic bathtub of water.",
                "The rings have a prominent 4,800 km wide gap called the Cassini Division caused by gravitational resonance with moon Mimas.",
                "A persistent hexagonal storm cloud pattern rotates around Saturn's north pole."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Cassini Division", description = "4,800 km gap in rings caused by Mimas orbital resonance.", u = 45f, v = 0f, radiusRatio = 1.95f),
                Space3DHotspot(name = "North Polar Hexagon", description = "Massive six-sided jet stream storm spanning 29,000 km.", u = 0f, v = 78f, radiusRatio = 1.05f),
                Space3DHotspot(name = "Ring A (Outer)", description = "Densely packed water ice boulders ranging from pebbles to meters.", u = 210f, v = 0f, radiusRatio = 2.2f),
                Space3DHotspot(name = "Equatorial Ammonia Bands", description = "Zonal atmospheric jet streams blowing at up to 1,800 km/h.", u = 180f, v = 5f, radiusRatio = 1.04f)
            )
        )
    }

    private fun createMarsModel(): Space3DModelData {
        return Space3DModelData(
            id = "mars_3d",
            name = "Mars",
            scientificDesignation = "Sol IV • Terrestrial Planet",
            objectType = Space3DObjectType.PLANET_TERRESTRIAL,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "Real 3D MOLA elevation and Viking/MRO color albedo maps provided by NASA / USGS.",
            baseRadius = 115f,
            primaryColorHex = 0xFFC84C21,
            secondaryColorHex = 0xFF8B2500,
            accentColorHex = 0xFFF3D2C1,
            texturePattern = "continents",
            hasAtmosphere = true,
            atmosphereColorHex = 0xFFFCA5A5,
            atmosphereThickness = 8f,
            axialTiltDegrees = 25.19f,
            rotationSpeed = 0.8f,
            distanceDisplay = "225 Million km (Avg)",
            radiusDisplay = "3,389.5 km (0.53x Earth)",
            massDisplay = "6.417 × 10²³ kg (0.107x Earth)",
            surfaceTemperatureDisplay = "-65 °C (Avg surface)",
            gravityDisplay = "3.72 m/s² (0.38 g)",
            orbitalPeriodDisplay = "687 Earth Days (1.88 Earth Years)",
            compositionOverview = "Silicate rock crust oxidized by Iron(III) oxide (rust), thin 95% Carbon Dioxide atmosphere.",
            scientificOverview = "Mars is the fourth planet from the Sun, featuring the Solar System's largest volcano (Olympus Mons) and deepest canyon network (Valles Marineris). Evidence indicates liquid water oceans covered its northern lowlands ~3.8 billion years ago.",
            interestingFacts = listOf(
                "Olympus Mons stands 21.9 km high—nearly 2.5 times the height of Mount Everest.",
                "Valles Marineris stretches over 4,000 km long and reaches 7 km deep, dwarfing the Grand Canyon.",
                "Mars possesses two irregularly shaped captured asteroid moons: Phobos and Deimos."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Olympus Mons", description = "Largest shield volcano in the Solar System (21.9 km elevation).", u = 227f, v = 18f, radiusRatio = 1.08f),
                Space3DHotspot(name = "Valles Marineris", description = "Grand canyon system spanning 4,000 km across Martian equator.", u = 290f, v = -14f, radiusRatio = 1.05f),
                Space3DHotspot(name = "Planum Boreum (North Ice Cap)", description = "Perennial water ice cap capped with seasonal dry ice (CO2).", u = 0f, v = 82f, radiusRatio = 1.05f),
                Space3DHotspot(name = "Jezero Crater", description = "Ancient river delta paleolake site explored by the Perseverance rover.", u = 77f, v = 18f, radiusRatio = 1.05f)
            )
        )
    }

    private fun createTon618Model(): Space3DModelData {
        return Space3DModelData(
            id = "ton618_3d",
            name = "TON 618",
            scientificDesignation = "Hyperluminous Quasar & Ultramassive Black Hole",
            objectType = Space3DObjectType.BLACK_HOLE,
            isConfirmedObservationalData = false,
            classificationLabel = "Scientific visualization / Artist’s 3D model",
            classificationDisclaimer = "3D relativistic raymarched Kerr metric visualization based on General Relativity equations and Lyman-alpha spectral lines. Direct optical resolution of this singularity does not exist.",
            baseRadius = 90f,
            primaryColorHex = 0xFF000000, // Pitch black event horizon
            secondaryColorHex = 0xFFEA580C,
            accentColorHex = 0xFF38BDF8,
            texturePattern = "plasma",
            hasAtmosphere = false,
            axialTiltDegrees = 45f,
            rotationSpeed = 1.4f,
            hasAccretionDisk = true,
            hasPhotonSphere = true,
            hasRelativisticJets = true,
            jetColorHex = 0xFF38BDF8,
            distanceDisplay = "18.2 Billion Light-Years (z = 2.219)",
            radiusDisplay = "Schwarzschild Radius: 1,300 AU (~195 Billion km)",
            massDisplay = "6.6 × 10¹⁰ M☉ (66 Billion Solar Masses)",
            surfaceTemperatureDisplay = "Accretion Disk: > 100,000,000 K",
            gravityDisplay = "Extreme relativistic spacetime singularity",
            orbitalPeriodDisplay = "Event horizon ISCO orbit ~ 1.2 weeks",
            compositionOverview = "Gravitational singularity enclosed by a photon sphere and superheated relativistically spinning gas accretion disk.",
            scientificOverview = "TON 618 is an ultramassive black hole powering a hyperluminous quasar. Shining with the luminosity of 140 trillion suns, its mass is equal to 66 billion Suns—exceeding the mass of all stars in the Milky Way combined.",
            interestingFacts = listOf(
                "Its event horizon alone spans 390 billion km in diameter—over 40 times the orbit of Neptune.",
                "The accretion disk radiates more light than 100 entire galaxies combined.",
                "Light observed from TON 618 today began its journey 10.8 billion years ago during cosmic noon."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Event Horizon", description = "Boundary of no return spanning 390 billion km across.", u = 0f, v = 0f, radiusRatio = 1.0f),
                Space3DHotspot(name = "Photon Sphere", description = "Unstable orbit where photons circle the black hole in geometric loops.", u = 40f, v = 0f, radiusRatio = 1.5f),
                Space3DHotspot(name = "Relativistic Doppler Accretion Disk", description = "Gas orbiting at 7,000 km/s glowing blue in approaching quadrant.", u = 120f, v = 15f, radiusRatio = 2.8f),
                Space3DHotspot(name = "Relativistic Synchrotron Jet", description = "Magnetic plasma collimated at 99.9% the speed of light.", u = 0f, v = 88f, radiusRatio = 3.2f)
            )
        )
    }

    private fun createAndromedaModel(): Space3DModelData {
        return Space3DModelData(
            id = "andromeda_3d",
            name = "Andromeda Galaxy (M31)",
            scientificDesignation = "Messier 31 • NGC 224",
            objectType = Space3DObjectType.GALAXY_SPIRAL,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "3D logarithmic spiral density model constructed from HST, Spitzer infrared, and GALEX UV star cluster surveys.",
            baseRadius = 140f,
            primaryColorHex = 0xFF60A5FA,
            secondaryColorHex = 0xFFFCD34D,
            accentColorHex = 0xFFC084FC,
            texturePattern = "spiral_arms",
            hasAtmosphere = false,
            axialTiltDegrees = 77.5f,
            rotationSpeed = 0.4f,
            distanceDisplay = "2.537 Million Light-Years",
            radiusDisplay = "110,000 Light-Years (Diameter ~220,000 ly)",
            massDisplay = "1.5 × 10¹² M☉ (1 Trillion Stars)",
            surfaceTemperatureDisplay = "Core: 10,000 K • Stellar disc: 3K-50,000K",
            gravityDisplay = "Binds Local Group with Milky Way",
            orbitalPeriodDisplay = "Galactic rotation period: ~250 Million Years",
            compositionOverview = "One trillion stars, interstellar gas, globular clusters, dark matter halo (80%).",
            scientificOverview = "The Andromeda Galaxy is the largest galaxy in our Local Group. It is approaching the Milky Way at roughly 110 km/s, headed for a spectacular galactic merger in ~4.5 billion years.",
            interestingFacts = listOf(
                "Contains approximately 1 trillion stars—more than double the Milky Way's population.",
                "Has a double nucleus containing a supermassive black hole of 140 million solar masses.",
                "Will merge with the Milky Way to form an giant elliptical galaxy named 'Milkomeda'."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Galactic Nucleus (M31*)", description = "Double nucleus housing a 140M M☉ supermassive black hole.", u = 0f, v = 0f, radiusRatio = 0.3f),
                Space3DHotspot(name = "10-kpc Star Formation Ring", description = "Infrared dusty ring undergoing active bursts of star formation.", u = 90f, v = 0f, radiusRatio = 1.6f),
                Space3DHotspot(name = "M32 Satellite Dwarf", description = "Compact elliptical galaxy orbiting closely and stripping gas.", u = 210f, v = 35f, radiusRatio = 2.1f)
            )
        )
    }

    private fun createJupiterModel(): Space3DModelData {
        return Space3DModelData(
            id = "jupiter_3d",
            name = "Jupiter",
            scientificDesignation = "Sol V • Gas Giant",
            objectType = Space3DObjectType.PLANET_GAS_GIANT,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "High-fidelity mapping based on NASA Juno and Voyager optical telemetry.",
            baseRadius = 125f,
            primaryColorHex = 0xFFD97706,
            secondaryColorHex = 0xFF78350F,
            accentColorHex = 0xFFFDE68A,
            texturePattern = "bands",
            hasAtmosphere = true,
            atmosphereColorHex = 0xFFFBBF24,
            atmosphereThickness = 12f,
            axialTiltDegrees = 3.13f,
            rotationSpeed = 1.8f,
            distanceDisplay = "778 Million km (5.2 AU)",
            radiusDisplay = "69,911 km (11x Earth)",
            massDisplay = "1.898 × 10²⁷ kg (318x Earth)",
            surfaceTemperatureDisplay = "-110 °C (Cloud deck)",
            gravityDisplay = "24.79 m/s² (2.53 g)",
            orbitalPeriodDisplay = "11.86 Earth Years",
            compositionOverview = "90% Hydrogen, 10% Helium, traces of Ammonia, Water, and Methane.",
            scientificOverview = "Jupiter is the most massive planet in the Solar System. Its immense gravitational sphere shields the inner rocky planets by deflecting and capturing cometary impactors.",
            interestingFacts = listOf(
                "The Great Red Spot is an anticyclonic storm that has raged for over 350 years.",
                "Rotates faster than any other planet—a day on Jupiter is just 9.9 hours long.",
                "Possesses 95 recognized moons including the ocean world Europa and volcanic Io."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Great Red Spot", description = "Anticyclonic storm larger than Earth with 430 km/h winds.", u = 150f, v = -22f, radiusRatio = 1.05f),
                Space3DHotspot(name = "North Equatorial Belt", description = "Dark cyclonic cloud band rich in sulfur and phosphorus compounds.", u = 60f, v = 15f, radiusRatio = 1.03f),
                Space3DHotspot(name = "Juno Aurora Rings", description = "Powerful magnetospheric auroras driven by Io's volcanic plasma.", u = 0f, v = 82f, radiusRatio = 1.06f)
            )
        )
    }

    private fun createEarthModel(): Space3DModelData {
        return Space3DModelData(
            id = "earth_3d",
            name = "Earth",
            scientificDesignation = "Sol III • Terrestrial Oasis",
            objectType = Space3DObjectType.PLANET_TERRESTRIAL,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "NASA Blue Marble / MODIS 3D surface topography with dynamic cloud layers.",
            baseRadius = 115f,
            primaryColorHex = 0xFF2563EB,
            secondaryColorHex = 0xFF16A34A,
            accentColorHex = 0xFFFFFFFF,
            texturePattern = "continents",
            hasAtmosphere = true,
            atmosphereColorHex = 0xFF93C5FD,
            atmosphereThickness = 14f,
            axialTiltDegrees = 23.44f,
            rotationSpeed = 1.0f,
            distanceDisplay = "149.6 Million km (1.0 AU)",
            radiusDisplay = "6,371 km",
            massDisplay = "5.972 × 10²⁴ kg (1.0 M⊕)",
            surfaceTemperatureDisplay = "+15 °C (Global Mean)",
            gravityDisplay = "9.81 m/s² (1.0 g)",
            orbitalPeriodDisplay = "365.25 Days",
            compositionOverview = "78% Nitrogen, 21% Oxygen, 71% surface liquid Water, Iron-Nickel liquid core.",
            scientificOverview = "Earth is the only known celestial body in the universe confirmed to harbor life and active surface plate tectonics.",
            interestingFacts = listOf(
                "Earth's magnetic field is generated by convective dynamo action in its molten iron outer core.",
                "The atmosphere shields the surface from cosmic rays and vaporizes millions of meteors daily.",
                "Earth's Moon stabilizes its axial tilt, preventing catastrophic climatic chaos."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Pacific Ring of Fire", description = "Subduction zone containing 75% of world's active volcanoes.", u = 160f, v = 5f, radiusRatio = 1.04f),
                Space3DHotspot(name = "Mariana Trench", description = "Deepest oceanic trench reaching 10,994 meters below sea level.", u = 142f, v = 11f, radiusRatio = 1.03f),
                Space3DHotspot(name = "Atmospheric Troposphere", description = "Nitrogen-Oxygen veil protecting surface life from solar UV radiation.", u = 0f, v = 45f, radiusRatio = 1.10f)
            )
        )
    }

    private fun createSunModel(): Space3DModelData {
        return Space3DModelData(
            id = "sun_3d",
            name = "The Sun (Sol)",
            scientificDesignation = "G2V Yellow Dwarf Star",
            objectType = Space3DObjectType.STAR,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "SDO (Solar Dynamics Observatory) magnetogram and AIA multi-wavelength telemetry.",
            baseRadius = 125f,
            primaryColorHex = 0xFFF59E0B,
            secondaryColorHex = 0xFFDC2626,
            accentColorHex = 0xFFFEF08A,
            texturePattern = "granulation",
            hasAtmosphere = true,
            atmosphereColorHex = 0xFFFDE047,
            atmosphereThickness = 24f,
            axialTiltDegrees = 7.25f,
            rotationSpeed = 0.6f,
            distanceDisplay = "149.6 Million km from Earth (8.3 Light-Minutes)",
            radiusDisplay = "696,340 km (109x Earth)",
            massDisplay = "1.989 × 10³⁰ kg (333,000x Earth)",
            surfaceTemperatureDisplay = "Photosphere: 5,500 °C • Core: 15,000,000 °C",
            gravityDisplay = "274 m/s² (28 g)",
            orbitalPeriodDisplay = "Galactic year: ~230 Million Years",
            compositionOverview = "73.4% Hydrogen, 25.0% Helium, 1.6% heavier elements (metals).",
            scientificOverview = "The Sun contains 99.86% of all mass in the Solar System. Every second, its core fuses 600 million tons of hydrogen into helium, converting 4 million tons of matter into pure radiant energy.",
            interestingFacts = listOf(
                "Photons generated in the core take ~100,000 years to diffuse out to the surface before racing to Earth in 8 minutes.",
                "The solar corona reaches millions of degrees, vastly hotter than the visible photosphere below it.",
                "Solar flares and coronal mass ejections can trigger geomagnetic storms and auroras on Earth."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Solar Core", description = "Thermonuclear fusion engine generating 3.8 × 10²⁶ Watts.", u = 0f, v = 0f, radiusRatio = 0.2f),
                Space3DHotspot(name = "Convective Granulation", description = "Plasma convection cells the size of Texas boiling at 5,778 K.", u = 60f, v = 20f, radiusRatio = 1.04f),
                Space3DHotspot(name = "Coronal Prominence Loop", description = "Magnetic flux tubes suspending 100,000 km loops of glowing plasma.", u = 240f, v = 45f, radiusRatio = 1.25f)
            )
        )
    }

    private fun createISSModel(): Space3DModelData {
        return Space3DModelData(
            id = "iss_3d",
            name = "International Space Station (ISS)",
            scientificDesignation = "LEO Space Laboratory • NORAD 25544",
            objectType = Space3DObjectType.SPACECRAFT_STATION,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "Engineering telemetry and CAD structure from NASA, Roscosmos, ESA, and JAXA.",
            baseRadius = 90f,
            primaryColorHex = 0xFFE2E8F0,
            secondaryColorHex = 0xFF3B82F6,
            accentColorHex = 0xFFF59E0B,
            texturePattern = "tech_hull",
            axialTiltDegrees = 51.64f,
            rotationSpeed = 1.2f,
            hasSolarPanels = true,
            hasModuleModules = true,
            distanceDisplay = "Low Earth Orbit (~418 km altitude)",
            radiusDisplay = "109 m × 73 m (Football field size)",
            massDisplay = "450,000 kg (990,000 lbs)",
            surfaceTemperatureDisplay = "-120 °C in shadow to +120 °C in sunlight",
            gravityDisplay = "Microgravity (0.89g in freefall)",
            orbitalPeriodDisplay = "92.68 Minutes (15.5 Orbits/day)",
            compositionOverview = "Aluminum-lithium pressurized modules, titanium truss, silicon-gallium solar arrays.",
            scientificOverview = "The ISS is humanity's longest continuously inhabited orbital research outpost, orbiting at 27,600 km/h and enabling microgravity research across biology, physics, and astronomy.",
            interestingFacts = listOf(
                "Astronauts aboard experience 16 sunrises and 16 sunsets every 24 hours.",
                "Its solar arrays generate 120 kilowatts of usable electrical power.",
                "Contains the Cupola observatory—a seven-window module offering panoramic views of Earth."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Cupola Observation Window", description = "7-window panoramic observatory looking directly down at Earth.", u = 0f, v = -15f, radiusRatio = 1.05f),
                Space3DHotspot(name = "UltraFlex Solar Arrays", description = "Rotates dynamically to track the Sun across all orbital planes.", u = 90f, v = 0f, radiusRatio = 2.4f),
                Space3DHotspot(name = "Destiny Laboratory", description = "Primary US microgravity research module for materials & medicine.", u = 180f, v = 10f, radiusRatio = 1.1f),
                Space3DHotspot(name = "Canadarm2", description = "17-meter robotic arm used for vehicle berthing and EVAs.", u = 270f, v = 25f, radiusRatio = 1.35f)
            )
        )
    }

    private fun createJWSTModel(): Space3DModelData {
        return Space3DModelData(
            id = "jwst_3d",
            name = "James Webb Space Telescope (JWST)",
            scientificDesignation = "Flagship Infrared Observatory",
            objectType = Space3DObjectType.SPACECRAFT_STATION,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "Official NASA / ESA / CSA 3D design schematics and deployment structure.",
            baseRadius = 95f,
            primaryColorHex = 0xFFFCD34D, // Gold beryllium mirrors
            secondaryColorHex = 0xFF94A3B8,
            accentColorHex = 0xFFE2E8F0,
            texturePattern = "tech_hull",
            axialTiltDegrees = 15f,
            rotationSpeed = 0.7f,
            hasHexagonalMirrors = true,
            hasSolarPanels = true,
            hasHighGainAntenna = true,
            distanceDisplay = "1.5 Million km (Sun-Earth L2 Lagrange Point)",
            radiusDisplay = "Primary Mirror: 6.5 meters diameter",
            massDisplay = "6,161 kg",
            surfaceTemperatureDisplay = "Cold side: -233 °C (40 K) • Sun side: +85 °C",
            gravityDisplay = "Gravitational saddle point (Lagrange L2)",
            orbitalPeriodDisplay = "Halo orbit around L2 every 6 months",
            compositionOverview = "18 gold-coated beryllium hexagonal segments, 5-layer Kapton sunshield, NIRCam & MIRI instruments.",
            scientificOverview = "JWST is humanity's premier space science observatory, designed to peer back over 13.5 billion years to observe the very first stars and galaxies formed after the Big Bang.",
            interestingFacts = listOf(
                "Its 18 mirror segments are aligned to an accuracy of a fraction of a wavelength of light.",
                "The 5-layer Kapton sunshield has a tennis-court size and provides SPF 1,000,000 protection.",
                "Operates at an icy 40 Kelvin (-233 °C) to detect ultra-faint infrared heat signatures."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "18 Gold Beryllium Mirrors", description = "6.5m aperture plated with 100nm of vaporized gold.", u = 0f, v = 10f, radiusRatio = 1.15f),
                Space3DHotspot(name = "5-Layer Kapton Sunshield", description = "Dissipates solar heat, dropping temperature by over 300°C.", u = 0f, v = -35f, radiusRatio = 1.9f),
                Space3DHotspot(name = "NIRCam & MIRI Instruments", description = "Cryogenic cameras imaging high-redshift universe.", u = 180f, v = 0f, radiusRatio = 1.1f)
            )
        )
    }

    private fun createMoonModel(): Space3DModelData {
        return Space3DModelData(
            id = "moon_3d",
            name = "The Moon (Luna)",
            scientificDesignation = "Earth I • Natural Satellite",
            objectType = Space3DObjectType.MOON,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "LOLA (Lunar Orbiter Laser Altimeter) 3D elevation and Clementine albedo mapping.",
            baseRadius = 110f,
            primaryColorHex = 0xFF94A3B8,
            secondaryColorHex = 0xFF475569,
            accentColorHex = 0xFFE2E8F0,
            texturePattern = "cratered",
            axialTiltDegrees = 6.68f,
            rotationSpeed = 0.5f,
            distanceDisplay = "384,400 km (1.28 Light-Seconds)",
            radiusDisplay = "1,737.4 km (0.27x Earth)",
            massDisplay = "7.342 × 10²² kg (0.0123x Earth)",
            surfaceTemperatureDisplay = "-130 °C (Night) to +120 °C (Day)",
            gravityDisplay = "1.62 m/s² (0.166 g)",
            orbitalPeriodDisplay = "27.3 Earth Days (Tidally Locked)",
            compositionOverview = "Anorthosite crust, basaltic maria lava plains, titanium and iron-rich regolith.",
            scientificOverview = "The Moon is Earth's only permanent natural satellite and the fifth-largest moon in the Solar System. Tidally locked to Earth, we always see the same lunar face from our planet.",
            interestingFacts = listOf(
                "Formed ~4.5 billion years ago when a Mars-sized protoplanet named Theia collided with early Earth.",
                "Permanently shadowed craters at the lunar south pole contain billions of tons of water ice.",
                "Footprints left by Apollo astronauts will remain undisturbed for millions of years due to lack of wind and rain."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Mare Tranquillitatis", description = "Apollo 11 landing site where humanity first walked on the Moon.", u = 31f, v = 8f, radiusRatio = 1.04f),
                Space3DHotspot(name = "Tycho Crater", description = "Prominent 85 km crater with bright impact ejecta rays spanning 1,500 km.", u = 348f, v = -43f, radiusRatio = 1.05f),
                Space3DHotspot(name = "Shackleton Crater (South Pole)", description = "Permanently shadowed crater hosting pure water ice deposits.", u = 0f, v = -89f, radiusRatio = 1.04f)
            )
        )
    }

    private fun createPillarsOfCreationModel(): Space3DModelData {
        return Space3DModelData(
            id = "pillars_of_creation_3d",
            name = "Pillars of Creation (Eagle Nebula)",
            scientificDesignation = "M16 • NGC 6611",
            objectType = Space3DObjectType.NEBULA,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "3D volumetric gas simulation reconstructing JWST NIRCam and Hubble 3D spatial tomography.",
            baseRadius = 130f,
            primaryColorHex = 0xFF0284C7,
            secondaryColorHex = 0xFFD97706,
            accentColorHex = 0xFFC084FC,
            texturePattern = "filaments",
            distanceDisplay = "6,500 Light-Years (Serpens Constellation)",
            radiusDisplay = "Largest pillar is 4-5 Light-Years tall (~40 Trillion km)",
            massDisplay = "~200 Solar Masses of cold gas & dust",
            surfaceTemperatureDisplay = "10 to 100 Kelvin (-263 °C)",
            gravityDisplay = "Undergoing gravitational collapse in EGG globules",
            orbitalPeriodDisplay = "Dispersing over ~3 Million Years",
            compositionOverview = "Molecular hydrogen gas (H2), carbonaceous polycyclic aromatic hydrocarbons (PAHs), and cosmic dust.",
            scientificOverview = "The Pillars of Creation are towering columns of interstellar hydrogen gas and dust in the Eagle Nebula. Massive newborn stars carve and sculpt the pillars with intense ultraviolet photoevaporation.",
            interestingFacts = listOf(
                "The largest central pillar stands 4 light-years high—equal to the distance from our Sun to Proxima Centauri.",
                "Dense pockets called EGGs (Evaporating Gaseous Globules) harbor actively incubating proto-stars.",
                "JWST's infrared vision penetrates the dusty veils to reveal thousands of previously hidden baby stars."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Apex Protostar EGG", description = "Evaporating gaseous globule actively collapsing into a new star.", u = 0f, v = 60f, radiusRatio = 1.25f),
                Space3DHotspot(name = "Middle Pillar", description = "Dense pillar resisting UV photoevaporation from NGC 6611 cluster.", u = 70f, v = 15f, radiusRatio = 1.1f),
                Space3DHotspot(name = "Stellar Wind Shockwave", description = "Supersonic ionization front glowing in hot sulfur and hydrogen.", u = 210f, v = -30f, radiusRatio = 1.35f)
            )
        )
    }

    private fun createTrappist1eModel(): Space3DModelData {
        return Space3DModelData(
            id = "trappist1e_3d",
            name = "TRAPPIST-1e",
            scientificDesignation = "2MASS J23062928-0502285 e",
            objectType = Space3DObjectType.EXOPLANET,
            isConfirmedObservationalData = false,
            classificationLabel = "Scientific visualization / Artist’s 3D model",
            classificationDisclaimer = "3D climate & planetary model based on transit spectroscopy, orbital mass, and planetary density. Surface features are artistically modeled based on rocky-ocean geophysical constraints.",
            baseRadius = 112f,
            primaryColorHex = 0xFF0284C7,
            secondaryColorHex = 0xFF065F46,
            accentColorHex = 0xFFE0F2FE,
            texturePattern = "continents",
            hasAtmosphere = true,
            atmosphereColorHex = 0xFF38BDF8,
            atmosphereThickness = 12f,
            axialTiltDegrees = 0f, // Tidally locked
            rotationSpeed = 0.4f,
            distanceDisplay = "39.46 Light-Years (Aquarius Constellation)",
            radiusDisplay = "5,861 km (0.92x Earth)",
            massDisplay = "4.13 × 10²⁴ kg (0.69x Earth)",
            surfaceTemperatureDisplay = "-27 °C to +15 °C (Equilibrium Temp)",
            gravityDisplay = "8.04 m/s² (0.82 g)",
            orbitalPeriodDisplay = "6.10 Earth Days",
            compositionOverview = "Dense iron-rich core, silicate mantle, liquid water ocean, nitrogen/CO2 atmosphere candidate.",
            scientificOverview = "TRAPPIST-1e is an Earth-sized exoplanet orbiting within the habitable zone of the ultracool red dwarf TRAPPIST-1. It is considered one of the most promising candidates for atmospheric biosignature detection.",
            interestingFacts = listOf(
                "Likely tidally locked, with one side in permanent daylight and the other in perpetual night.",
                "Other TRAPPIST planets appear up to twice the size of the full Moon in its sky.",
                "Has an Earth Similarity Index (ESI) of 0.85, making it remarkably close to Earth in physical characteristics."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Sub-Stellar Ocean Basin", description = "Permanent daylight ocean receiving steady red dwarf warmth.", u = 0f, v = 0f, radiusRatio = 1.04f),
                Space3DHotspot(name = "Twilight Habitable Ring", description = "Terminator band between light and dark with moderate climate.", u = 90f, v = 0f, radiusRatio = 1.04f),
                Space3DHotspot(name = "Nightside Ice Shield", description = "Frozen glacier expanse on the permanent dark hemisphere.", u = 180f, v = 0f, radiusRatio = 1.04f)
            )
        )
    }

    private fun createMilkyWayModel(): Space3DModelData {
        return Space3DModelData(
            id = "milkyway_3d",
            name = "Milky Way Galaxy",
            scientificDesignation = "Barred Spiral Galaxy • Sb/Sbc",
            objectType = Space3DObjectType.GALAXY_SPIRAL,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "Gaia astrometric survey star map combined with radio neutral hydrogen (HI) kinematic tomography.",
            baseRadius = 140f,
            primaryColorHex = 0xFF38BDF8,
            secondaryColorHex = 0xFFF59E0B,
            accentColorHex = 0xFF818CF8,
            texturePattern = "spiral_arms",
            axialTiltDegrees = 60f,
            rotationSpeed = 0.5f,
            distanceDisplay = "We reside 26,000 ly from the Galactic Core",
            radiusDisplay = "50,000 Light-Years (Diameter ~100,000 ly)",
            massDisplay = "1.15 × 10¹² M☉ (100-400 Billion Stars)",
            surfaceTemperatureDisplay = "Cosmic background to stellar cores",
            gravityDisplay = "Hosts Sagittarius A* (4.15M M☉)",
            orbitalPeriodDisplay = "Solar system orbits every ~230 Million Years",
            compositionOverview = "Four major spiral arms (Perseus, Scutum-Centaurus, Sagittarius, Outer), central stellar bar, dark matter halo.",
            scientificOverview = "The Milky Way is our home barred spiral galaxy. The Solar System is situated on the inner edge of the Orion-Cygnus Arm, orbiting the supermassive black hole Sagittarius A* at ~220 km/s.",
            interestingFacts = listOf(
                "Traveling at the speed of light, it would take 100,000 years to cross the galactic disc.",
                "Our Sun has orbited the galactic center approximately 20 times since its formation.",
                "Over 90% of the Milky Way's total mass consists of mysterious invisible dark matter."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Sagittarius A* (Core)", description = "4.15 million solar mass supermassive black hole at galactic center.", u = 0f, v = 0f, radiusRatio = 0.2f),
                Space3DHotspot(name = "Orion Spur (Our Location)", description = "Location of the Solar System ~26,000 light-years from the core.", u = 120f, v = 0f, radiusRatio = 1.3f),
                Space3DHotspot(name = "Perseus Spiral Arm", description = "Major spiral arm rich in massive star-forming giant molecular clouds.", u = 240f, v = 0f, radiusRatio = 1.9f)
            )
        )
    }

    private fun createSagittariusAStarModel(): Space3DModelData {
        return Space3DModelData(
            id = "sgra_3d",
            name = "Sagittarius A*",
            scientificDesignation = "Milky Way Supermassive Singularity",
            objectType = Space3DObjectType.BLACK_HOLE,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "Derived directly from Event Horizon Telescope (EHT) millimetric radio interferometry.",
            baseRadius = 90f,
            primaryColorHex = 0xFF000000,
            secondaryColorHex = 0xFFF97316,
            accentColorHex = 0xFFFBBF24,
            texturePattern = "plasma",
            axialTiltDegrees = 30f,
            rotationSpeed = 1.6f,
            hasAccretionDisk = true,
            hasPhotonSphere = true,
            distanceDisplay = "26,673 Light-Years from Earth",
            radiusDisplay = "Shadow diameter: ~51.8 microarcseconds (~44M km)",
            massDisplay = "4.154 × 10⁶ M☉ (4.15 Million Solar Masses)",
            surfaceTemperatureDisplay = "Accretion Flow: Millions of Kelvin",
            gravityDisplay = "Dominates central parsec S-star orbits",
            orbitalPeriodDisplay = "ISCO orbit duration: ~4 minutes",
            compositionOverview = "Kerr rotating black hole surrounded by magnetized synchrotron-emitting plasma ring.",
            scientificOverview = "Sagittarius A* is the supermassive black hole at the center of the Milky Way. Its silhouette and turbulent glowing accretion ring were famously imaged by the Event Horizon Telescope in 2022.",
            interestingFacts = listOf(
                "S-stars orbit Sagittarius A* at up to 8% the speed of light (24,000 km/s).",
                "Its event horizon would fit snugly inside the orbit of Mercury.",
                "Consumes only a tiny fraction of gas orbiting near it, radiating as a relatively quiet singularity."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "EHT Shadow Silhouette", description = "Central darkness caused by light capture within the photon ring.", u = 0f, v = 0f, radiusRatio = 1.0f),
                Space3DHotspot(name = "Doppler Beaming Hotspot", description = "Approaching side of accretion ring boosted in brightness by relativity.", u = 60f, v = 10f, radiusRatio = 1.6f),
                Space3DHotspot(name = "S2 Star Periapsis", description = "Star executing a relativistic Schwarzschild precession orbit.", u = 200f, v = 45f, radiusRatio = 2.6f)
            )
        )
    }

    private fun createM87BlackHoleModel(): Space3DModelData {
        return Space3DModelData(
            id = "m87_3d",
            name = "M87* Black Hole",
            scientificDesignation = "Messier 87 Singularity",
            objectType = Space3DObjectType.BLACK_HOLE,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "Event Horizon Telescope 2019 direct shadow synthesis and relativistic jet polarization telemetry.",
            baseRadius = 92f,
            primaryColorHex = 0xFF000000,
            secondaryColorHex = 0xFFEA580C,
            accentColorHex = 0xFF38BDF8,
            texturePattern = "plasma",
            hasAccretionDisk = true,
            hasPhotonSphere = true,
            hasRelativisticJets = true,
            jetColorHex = 0xFF60A5FA,
            distanceDisplay = "53.5 Million Light-Years (Virgo Cluster)",
            radiusDisplay = "Event Horizon: ~38 Billion km (~255 AU)",
            massDisplay = "6.5 × 10⁹ M☉ (6.5 Billion Solar Masses)",
            surfaceTemperatureDisplay = "Relativistic plasma: > 10 Billion Kelvin",
            gravityDisplay = "Ultra-intense Kerr gravitational field",
            orbitalPeriodDisplay = "ISCO orbit: ~9 hours",
            compositionOverview = "Singularity, photon capture ring, magnetically arrested disk (MAD), 5,000 ly relativistic jet.",
            scientificOverview = "M87* was the first black hole ever directly imaged in human history. It launches a colossal 5,000-light-year-long relativistic jet powered by magnetic frame-dragging (Blandford-Znajek process).",
            interestingFacts = listOf(
                "Its mass is 6.5 billion times that of our Sun—larger than our entire Solar System.",
                "The relativistic jet travels at 99% the speed of light, producing apparent superluminal motion.",
                "Its magnetic field lines swirl in spiral patterns mapped by polarimetric EHT arrays."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "First Imaged Black Hole Shadow", description = "Historic 2019 EHT image confirming Einstein's General Relativity.", u = 0f, v = 0f, radiusRatio = 1.0f),
                Space3DHotspot(name = "5,000-ly Relativistic Jet", description = "Collimated synchrotron jet bursting into intergalactic space.", u = 0f, v = 85f, radiusRatio = 3.5f),
                Space3DHotspot(name = "Magnetized Accretion Ring", description = "Submillimeter synchrotron ring polarized by magnetic fields.", u = 120f, v = 0f, radiusRatio = 1.8f)
            )
        )
    }

    private fun createPulsarModel(name: String): Space3DModelData {
        return Space3DModelData(
            id = "pulsar_3d",
            name = if (name.isNotBlank()) name.capitalizeFirst() else "Crab Pulsar (PSR B0531+21)",
            scientificDesignation = "Rapidly Rotating Neutron Star",
            objectType = Space3DObjectType.NEUTRON_STAR_PULSAR,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "NICER and Fermi gamma-ray telescope surface hotspot & magnetic dipole models.",
            baseRadius = 65f,
            primaryColorHex = 0xFF38BDF8,
            secondaryColorHex = 0xFF818CF8,
            accentColorHex = 0xFFFFFFFF,
            texturePattern = "plasma",
            axialTiltDegrees = 30f,
            rotationSpeed = 3.5f,
            hasAtmosphere = true,
            atmosphereColorHex = 0xFF67E8F9,
            atmosphereThickness = 30f,
            hasRelativisticJets = true,
            jetColorHex = 0xFFE0F2FE,
            distanceDisplay = "6,500 Light-Years (Crab Nebula)",
            radiusDisplay = "10 to 12 km (City-sized sphere)",
            massDisplay = "1.4 to 2.1 M☉ (Crushed into 12 km)",
            surfaceTemperatureDisplay = "1,000,000 Kelvin (1 Million °C)",
            gravityDisplay = "2 × 10¹¹ g (200 Billion times Earth)",
            orbitalPeriodDisplay = "Spins 30 times every single second (30 Hz)",
            compositionOverview = "Solid iron crystal crust, superfluid neutron core, degenerate nuclear matter (nuclear pasta).",
            scientificOverview = "A pulsar is a magnetized rotating neutron star formed from the collapsed core of a massive supernova. It sweeps beams of intense electromagnetic radiation across space like a cosmic lighthouse.",
            interestingFacts = listOf(
                "A single teaspoon of neutron star matter would weigh 1 billion tons on Earth (as much as Mount Everest).",
                "Spins up to 716 times per second (PSR J1748-2446ad) without tearing itself apart.",
                "Surface escape velocity exceeds 150,000 km/s—half the speed of light."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Magnetic Pole Synchrotron Beam", description = "Relativistic radio and gamma-ray beam sweeping across observer lines of sight.", u = 0f, v = 85f, radiusRatio = 2.8f),
                Space3DHotspot(name = "Ultra-Dense Neutron Crust", description = "Iron nuclei crushed into a crystalline lattice 100 billion times stronger than steel.", u = 45f, v = 15f, radiusRatio = 1.05f),
                Space3DHotspot(name = "Magnetic Dipole Torus", description = "Toroidal magnetic field reaching 10¹² Gauss (trillions of times Earth's).", u = 180f, v = 0f, radiusRatio = 1.9f)
            )
        )
    }

    private fun createVoyagerModel(): Space3DModelData {
        return Space3DModelData(
            id = "voyager_3d",
            name = "Voyager 1",
            scientificDesignation = "Interstellar Probe • NASA JPL",
            objectType = Space3DObjectType.SPACECRAFT_STATION,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "NASA JPL engineering CAD and high-gain antenna architecture.",
            baseRadius = 85f,
            primaryColorHex = 0xFFF1F5F9,
            secondaryColorHex = 0xFF64748B,
            accentColorHex = 0xFFF59E0B,
            texturePattern = "tech_hull",
            axialTiltDegrees = 20f,
            rotationSpeed = 0.5f,
            hasHighGainAntenna = true,
            distanceDisplay = "24.5 Billion km (164 AU • 22.7 Light-Hours)",
            radiusDisplay = "3.7-meter High-Gain Dish Antenna",
            massDisplay = "722 kg (Original launch mass 815 kg)",
            surfaceTemperatureDisplay = "Interstellar space (~3 Kelvin)",
            gravityDisplay = "Escaping Solar System at ~17 km/s (61,200 km/h)",
            orbitalPeriodDisplay = "Hyperbolic escape trajectory",
            compositionOverview = "Aluminum bus, 3 Plutonium-238 RTGs (Radioisotope Thermoelectric Generators), Golden Record.",
            scientificOverview = "Voyager 1 is the most distant human-made object in existence. Launched in 1977, it crossed the heliopause in 2012 to become humanity's first spacecraft to enter interstellar space.",
            interestingFacts = listOf(
                "Radio signals traveling at light speed take over 22.5 hours to travel from Earth to Voyager 1.",
                "Carries the Golden Record containing sounds, music, and images of Earth for extraterrestrial intelligence.",
                "Powered by decaying Plutonium-238, expected to operate instruments until the late 2020s."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "3.7m High-Gain Parabolic Dish", description = "Kept permanently pointed at Earth to transmit 160-bit/s data across 24 billion km.", u = 0f, v = 45f, radiusRatio = 1.25f),
                Space3DHotspot(name = "Plutonium-238 RTG Boom", description = "Generates heat and electricity from radioactive decay of plutonium.", u = 120f, v = -20f, radiusRatio = 1.9f),
                Space3DHotspot(name = "The Golden Record", description = "Gold-plated copper phonograph record carrying greetings in 55 languages.", u = 240f, v = 0f, radiusRatio = 1.1f),
                Space3DHotspot(name = "Magnetometer Boom", description = "13-meter fiberglass boom detecting the interstellar magnetic field.", u = 300f, v = -35f, radiusRatio = 2.4f)
            )
        )
    }

    private fun createHubbleModel(): Space3DModelData {
        return Space3DModelData(
            id = "hubble_3d",
            name = "Hubble Space Telescope",
            scientificDesignation = "HST • Low Earth Orbit Observatory",
            objectType = Space3DObjectType.SPACECRAFT_STATION,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "NASA / ESA engineering schematics and operational optical specs.",
            baseRadius = 90f,
            primaryColorHex = 0xFFE2E8F0,
            secondaryColorHex = 0xFF3B82F6,
            accentColorHex = 0xFFF59E0B,
            texturePattern = "tech_hull",
            axialTiltDegrees = 28.5f,
            rotationSpeed = 0.8f,
            hasSolarPanels = true,
            distanceDisplay = "540 km Low Earth Orbit altitude",
            radiusDisplay = "13.2 meters long • 2.4m Primary Mirror",
            massDisplay = "11,110 kg",
            surfaceTemperatureDisplay = "-100 °C to +100 °C in orbit",
            gravityDisplay = "Microgravity in LEO freefall",
            orbitalPeriodDisplay = "95 Minutes",
            compositionOverview = "Ritchey-Chrétien Cassegrain optical telescope, silicon solar panels, fine guidance sensors.",
            scientificOverview = "Launched in 1990, the Hubble Space Telescope has made over 1.5 million observations, determining the expansion rate of the universe and confirming supermassive black holes.",
            interestingFacts = listOf(
                "Has traveled more than 6.5 billion km in low Earth orbit since launch.",
                "Repaired and upgraded in space 5 times by Space Shuttle astronaut crews.",
                "Its pointing accuracy is equivalent to shining a laser beam on a dime 320 km away."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Aperture Door & Baffle", description = "Shields primary optics from stray Earth and Sun light.", u = 0f, v = 50f, radiusRatio = 1.2f),
                Space3DHotspot(name = "Dual Solar Arrays", description = "Flexible silicon panels generating 2,800 Watts of orbital power.", u = 90f, v = 0f, radiusRatio = 2.1f),
                Space3DHotspot(name = "2.4m Primary Mirror", description = "Ultra-smooth primary mirror ground to within 10 nanometers.", u = 180f, v = 0f, radiusRatio = 1.05f)
            )
        )
    }

    private fun createHalleyCometModel(): Space3DModelData {
        return Space3DModelData(
            id = "halley_3d",
            name = "1P/Halley (Halley's Comet)",
            scientificDesignation = "Periodic Comet 1P/1682 Q1",
            objectType = Space3DObjectType.ASTEROID_COMET,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "ESA Giotto spacecraft flyby close-up nucleus topography.",
            baseRadius = 85f,
            primaryColorHex = 0xFF334155,
            secondaryColorHex = 0xFF0F172A,
            accentColorHex = 0xFF38BDF8,
            texturePattern = "polygonal_rock",
            axialTiltDegrees = 18f,
            rotationSpeed = 0.9f,
            hasCometTail = true,
            distanceDisplay = "Currently near aphelion (~35 AU, beyond Neptune)",
            radiusDisplay = "Nucleus: 15 km × 8 km (Peanut shaped)",
            massDisplay = "2.2 × 10¹⁴ kg",
            surfaceTemperatureDisplay = "Near Sun: +80 °C • Outer Solar System: -200 °C",
            gravityDisplay = "0.001 m/s² (Extremely weak)",
            orbitalPeriodDisplay = "75 to 76 Earth Years (Returns in 2061)",
            compositionOverview = "Water ice (80%), carbon monoxide (10%), methane, dark organic tholins ('dirty snowball').",
            scientificOverview = "Halley's Comet is the only known short-period comet that is regularly visible to the naked eye from Earth, recorded by human astronomers since at least 240 BC.",
            interestingFacts = listOf(
                "Its surface albedo is darker than charcoal (reflects only 4% of light).",
                "Its ion and dust tails stretch up to 100 million kilometers through space.",
                "Produces two annual meteor showers on Earth: the Eta Aquariids and the Orionids."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Sublimation Gas Jet", description = "Solar heating vaporizes subsurface ice into supersonic gas geysers.", u = 0f, v = 30f, radiusRatio = 1.3f),
                Space3DHotspot(name = "Dark Organic Crust", description = "Carbon-rich refractory organic mantle insulating interior water ice.", u = 120f, v = -10f, radiusRatio = 1.05f),
                Space3DHotspot(name = "Dual Ion & Dust Tail", description = "Solar wind pushes ionized gas and dust particles millions of km behind.", u = 180f, v = -45f, radiusRatio = 2.8f)
            )
        )
    }

    private fun createBetelgeuseModel(): Space3DModelData {
        return Space3DModelData(
            id = "betelgeuse_3d",
            name = "Betelgeuse (Alpha Orionis)",
            scientificDesignation = "Red Supergiant Star • M1-2 Ia-ab",
            objectType = Space3DObjectType.STAR,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "ALMA and ESO VLT SPHERE direct interferometric surface reconstructions.",
            baseRadius = 135f,
            primaryColorHex = 0xFFDC2626,
            secondaryColorHex = 0xFFB45309,
            accentColorHex = 0xFFFBBF24,
            texturePattern = "granulation",
            hasAtmosphere = true,
            atmosphereColorHex = 0xFFEA580C,
            atmosphereThickness = 32f,
            axialTiltDegrees = 10f,
            rotationSpeed = 0.3f,
            distanceDisplay = "642.5 Light-Years (Orion Constellation)",
            radiusDisplay = "764 Solar Radii (~530 Million km, would engulf Jupiter's orbit)",
            massDisplay = "16.5 to 19 Solar Masses",
            surfaceTemperatureDisplay = "3,500 Kelvin (Cool reddish star)",
            gravityDisplay = "Extremely low surface gravity (puffy supergiant)",
            orbitalPeriodDisplay = "Expected to explode as Supernova within 100,000 years",
            compositionOverview = "Evolved fusion shells (Hydrogen, Helium, Carbon, Oxygen, Neon, Silicon).",
            scientificOverview = "Betelgeuse is a semi-regular pulsating red supergiant star in the shoulder of Orion. If placed at the center of our Solar System, its bloated surface would extend past the orbit of Jupiter.",
            interestingFacts = listOf(
                "In 2019-2020, Betelgeuse dramatically dimmed by 60% due to a massive ejection of surface dust.",
                "When it goes supernova, it will shine as bright as the Full Moon for over 3 months, visible in broad daylight.",
                "Surface giant convective cells can be larger than our entire Sun."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Giant Convective Cell", description = "Boiling plasma convection cell spanning hundreds of millions of km.", u = 30f, v = 20f, radiusRatio = 1.08f),
                Space3DHotspot(name = "2019 Dust Ejection Cloud", description = "Cooled surface plasma forming obscuring silicate dust grains.", u = 150f, v = -35f, radiusRatio = 1.4f),
                Space3DHotspot(name = "Outer Pulsating Shell", description = "Pulsates in size and brightness every 420 days.", u = 270f, v = 10f, radiusRatio = 1.2f)
            )
        )
    }

    private fun createSiriusModel(): Space3DModelData {
        return Space3DModelData(
            id = "sirius_3d",
            name = "Sirius A (Alpha Canis Majoris)",
            scientificDesignation = "The Dog Star • A1V Main Sequence",
            objectType = Space3DObjectType.STAR,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "High-resolution astrometric and spectral measurements from Hipparcos & Hubble.",
            baseRadius = 120f,
            primaryColorHex = 0xFF60A5FA,
            secondaryColorHex = 0xFF3B82F6,
            accentColorHex = 0xFFFFFFFF,
            texturePattern = "granulation",
            hasAtmosphere = true,
            atmosphereColorHex = 0xFF93C5FD,
            atmosphereThickness = 22f,
            axialTiltDegrees = 8f,
            rotationSpeed = 1.2f,
            distanceDisplay = "8.611 Light-Years from Earth",
            radiusDisplay = "1.71 Solar Radii (1.19 Million km)",
            massDisplay = "2.063 Solar Masses",
            surfaceTemperatureDisplay = "9,940 Kelvin (Blazing Blue-White)",
            gravityDisplay = "225 m/s²",
            orbitalPeriodDisplay = "Sirius A and B orbit each other every 50.1 years",
            compositionOverview = "Hydrogen core undergoing CNO fusion cycle, high metallicity.",
            scientificOverview = "Sirius is the brightest star in Earth's night sky, with an apparent magnitude of -1.46. It is a binary star system consisting of the luminous main-sequence star Sirius A and a faint white dwarf companion, Sirius B.",
            interestingFacts = listOf(
                "Shines over 25 times more luminous than our Sun.",
                "Its white dwarf companion Sirius B has the mass of the Sun packed into a sphere the size of Earth.",
                "Known to ancient Egyptians as Sopdet, whose helical rising signaled the annual flooding of the Nile."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "CNO Hydrogen Fusion Core", description = "Hot core generating 25.4 times the radiant luminosity of the Sun.", u = 0f, v = 0f, radiusRatio = 0.3f),
                Space3DHotspot(name = "Fast Rotational Bulge", description = "Rotates at 16 km/s causing slight oblate equatorial flattening.", u = 90f, v = 0f, radiusRatio = 1.05f),
                Space3DHotspot(name = "Sirius B White Dwarf Companion", description = "Earth-sized dense degenerate star companion orbiting every 50 years.", u = 240f, v = 40f, radiusRatio = 2.4f)
            )
        )
    }

    private fun createProximaCentauriModel(): Space3DModelData {
        return Space3DModelData(
            id = "proxima_3d",
            name = "Proxima Centauri",
            scientificDesignation = "Alpha Centauri C • Red Dwarf Flare Star",
            objectType = Space3DObjectType.STAR,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "ESO VLT and HARPS spectrograph stellar characterization.",
            baseRadius = 100f,
            primaryColorHex = 0xFFDC2626,
            secondaryColorHex = 0xFF7F1D1D,
            accentColorHex = 0xFFFCA5A5,
            texturePattern = "plasma",
            hasAtmosphere = true,
            atmosphereColorHex = 0xFFEF4444,
            atmosphereThickness = 16f,
            axialTiltDegrees = 5f,
            rotationSpeed = 0.5f,
            distanceDisplay = "4.2465 Light-Years (Closest Star to the Sun)",
            radiusDisplay = "0.154 Solar Radii (107,280 km, slightly larger than Jupiter)",
            massDisplay = "0.122 Solar Masses (12% the mass of the Sun)",
            surfaceTemperatureDisplay = "3,042 Kelvin (Deep Crimson Red)",
            gravityDisplay = "1,450 m/s² (148 g)",
            orbitalPeriodDisplay = "Orbits Alpha Centauri AB binary every ~550,000 Years",
            compositionOverview = "Fully convective hydrogen-helium red dwarf plasma.",
            scientificOverview = "Proxima Centauri is the closest known star to the Solar System. Despite its proximity, it is too dim to be seen with the naked eye. It hosts at least two confirmed exoplanets, including the habitable-zone world Proxima Centauri b.",
            interestingFacts = listOf(
                "Because it is fully convective, it will burn its hydrogen fuel for over 4 trillion years (300 times the current age of the universe).",
                "Experiences violent superflares that can increase its brightness by a factor of 100 in minutes.",
                "A probe traveling at 20% the speed of light (Breakthrough Starshot) could reach Proxima in just 20 years."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Superflare Eruption Zone", description = "Magnetic reconnection releasing violent flares & high-energy X-rays.", u = 40f, v = 25f, radiusRatio = 1.25f),
                Space3DHotspot(name = "Fully Convective Plasma", description = "Hydrogen circulates continuously from core to surface without radiative dead zones.", u = 160f, v = -10f, radiusRatio = 1.04f),
                Space3DHotspot(name = "Proxima b Orbit Vector", description = "Direction towards Earth-mass planet orbiting every 11.2 days.", u = 280f, v = 0f, radiusRatio = 2.1f)
            )
        )
    }

    private fun createOrionNebulaModel(): Space3DModelData {
        return Space3DModelData(
            id = "orion_nebula_3d",
            name = "Orion Nebula (M42)",
            scientificDesignation = "Messier 42 • NGC 1976",
            objectType = Space3DObjectType.NEBULA,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "3D volumetric gas simulation derived from Hubble Space Telescope Treasury Survey.",
            baseRadius = 135f,
            primaryColorHex = 0xFF0284C7,
            secondaryColorHex = 0xFFC084FC,
            accentColorHex = 0xFFF43F5E,
            texturePattern = "filaments",
            distanceDisplay = "1,344 Light-Years from Earth",
            radiusDisplay = "12 Light-Years across (~24 Light-Years diameter)",
            massDisplay = "~2,000 Solar Masses of interstellar gas and dust",
            surfaceTemperatureDisplay = "Ionized HII gas: ~10,000 Kelvin",
            gravityDisplay = "Turbulent star-forming molecular cloud",
            orbitalPeriodDisplay = "Formed within the last 3 Million Years",
            compositionOverview = "Hydrogen (90%), Helium (9%), Oxygen, Nitrogen, complex organic prebiotic molecules.",
            scientificOverview = "The Orion Nebula is the closest massive star-forming region to Earth, visible to the naked eye as the middle 'star' in Orion's sword. It contains the Trapezium cluster—a tight quartet of blazing young O-type stars.",
            interestingFacts = listOf(
                "Contains over 700 young stars in various stages of formation and protoplanetary discs (proplyds).",
                "Lit up primarily by Theta-1 Orionis C, an O-type star 250,000 times brighter than our Sun.",
                "Infrared spectroscopy has identified water, methanol, carbon monoxide, and amino acid precursors within its clouds."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Trapezium Cluster", description = "Four massive young O/B stars whose ultraviolet light ionizes the entire nebula.", u = 0f, v = 0f, radiusRatio = 0.3f),
                Space3DHotspot(name = "Protoplanetary Disc (Proplyd)", description = "Accretion disc around a newborn star forming a new planetary system.", u = 90f, v = 30f, radiusRatio = 1.25f),
                Space3DHotspot(name = "Orion Bar Ionization Front", description = "Dense ridge where UV radiation strikes cold neutral molecular gas.", u = 210f, v = -25f, radiusRatio = 1.4f)
            )
        )
    }

    private fun createCrabNebulaModel(): Space3DModelData {
        return Space3DModelData(
            id = "crab_nebula_3d",
            name = "Crab Nebula (M1)",
            scientificDesignation = "Messier 1 • Supernova Remnant 1054",
            objectType = Space3DObjectType.NEBULA,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "3D kinematic expansion mapping based on Hubble, Chandra X-ray, and Keck observatory.",
            baseRadius = 130f,
            primaryColorHex = 0xFF06B6D4,
            secondaryColorHex = 0xFFEA580C,
            accentColorHex = 0xFFE11D48,
            texturePattern = "filaments",
            distanceDisplay = "6,500 Light-Years (Taurus Constellation)",
            radiusDisplay = "5.5 Light-Years across (~11 ly diameter)",
            massDisplay = "~4.6 Solar Masses of ejected stellar envelope",
            surfaceTemperatureDisplay = "Filament gas: 11,000 K to millions of K",
            gravityDisplay = "Powered by central Crab Pulsar (30 Hz)",
            orbitalPeriodDisplay = "Expanding outward at 1,500 km/s (0.5% speed of light)",
            compositionOverview = "Gaseous filaments of ionized hydrogen and helium with synchrotron pulsar wind nebula at core.",
            scientificOverview = "The Crab Nebula is the remnant of a core-collapse supernova observed by Chinese, Japanese, and Arab astronomers in the year 1054 AD. At its center lies the Crab Pulsar, spinning 30 times every second.",
            interestingFacts = listOf(
                "The supernova of 1054 was so bright it could be seen in broad daylight for 23 days.",
                "The nebula continues to expand outward into space at 1,500 km/second (3.3 million mph).",
                "Its synchrotron radiation spans the entire electromagnetic spectrum from radio to ultra-high-energy gamma rays."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Crab Pulsar Core", description = "Neutron star spinning 30 times per second powering the entire nebula.", u = 0f, v = 0f, radiusRatio = 0.2f),
                Space3DHotspot(name = "X-Ray Torus & Inner Ring", description = "Relativistic plasma shockwave mapped by Chandra X-Ray Observatory.", u = 70f, v = 15f, radiusRatio = 1.15f),
                Space3DHotspot(name = "Supersonic Gas Filament", description = "Tangled filamentary web of hydrogen and oxygen gas expanding into void.", u = 200f, v = -35f, radiusRatio = 1.45f)
            )
        )
    }

    private fun createGenericBlackHoleModel(name: String): Space3DModelData {
        return Space3DModelData(
            id = "black_hole_${System.currentTimeMillis()}",
            name = if (name.isNotBlank()) name.capitalizeFirst() else "Stellar Black Hole",
            scientificDesignation = "Gravitational Singularity • General Relativity",
            objectType = Space3DObjectType.BLACK_HOLE,
            isConfirmedObservationalData = false,
            classificationLabel = "Scientific visualization / Artist’s 3D model",
            classificationDisclaimer = "3D relativistic physics render using Schwarzschild & Kerr metrics. Mathematical model of spacetime curvature; direct optical photograph is physically impossible.",
            baseRadius = 90f,
            primaryColorHex = 0xFF000000,
            secondaryColorHex = 0xFFEA580C,
            accentColorHex = 0xFF38BDF8,
            texturePattern = "plasma",
            axialTiltDegrees = 35f,
            rotationSpeed = 1.4f,
            hasAccretionDisk = true,
            hasPhotonSphere = true,
            hasRelativisticJets = true,
            distanceDisplay = "Deep Cosmic Space",
            radiusDisplay = "Schwarzschild Radius: R_s = 2GM / c²",
            massDisplay = "5 to 50 Billion Solar Masses",
            surfaceTemperatureDisplay = "Accretion Flow: Millions of Kelvin",
            gravityDisplay = "Infinite gravitational curvature at r = 0",
            orbitalPeriodDisplay = "Frame dragging at ergosphere boundary",
            compositionOverview = "Gravitational singularity enclosed within an event horizon, surrounded by a relativistic accretion disk.",
            scientificOverview = "A black hole is a region of spacetime where gravity is so strong that nothing—not even particles or electromagnetic radiation such as light—has enough energy to escape its boundary.",
            interestingFacts = listOf(
                "Time slows down infinitely at the event horizon relative to a distant observer (gravitational time dilation).",
                "Light passing near the black hole is bent into gravitational lensing rings around the singularity shadow.",
                "Hawking radiation causes black holes to slowly lose mass over unfathomable timescales."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Event Horizon", description = "Boundary of no return where escape velocity equals the speed of light.", u = 0f, v = 0f, radiusRatio = 1.0f),
                Space3DHotspot(name = "Photon Sphere (r = 1.5 R_s)", description = "Region where gravity is strong enough to force photons into circular orbits.", u = 35f, v = 10f, radiusRatio = 1.5f),
                Space3DHotspot(name = "Accretion Disk", description = "Matter spiraling into the black hole at relativistic velocities.", u = 120f, v = 0f, radiusRatio = 2.4f)
            )
        )
    }

    private fun createExoplanetModel(name: String): Space3DModelData {
        return Space3DModelData(
            id = "exoplanet_${System.currentTimeMillis()}",
            name = if (name.isNotBlank()) name.capitalizeFirst() else "Alien Exoplanet",
            scientificDesignation = "Extrasolar Planetary World",
            objectType = Space3DObjectType.EXOPLANET,
            isConfirmedObservationalData = false,
            classificationLabel = "Scientific visualization / Artist’s 3D model",
            classificationDisclaimer = "3D atmospheric and surface model reconstructed from transit light curves and radial velocity spectroscopy. Artist's impression of unmapped extrasolar surface.",
            baseRadius = 114f,
            primaryColorHex = 0xFF0284C7,
            secondaryColorHex = 0xFF7C3AED,
            accentColorHex = 0xFF38BDF8,
            texturePattern = "continents",
            hasAtmosphere = true,
            atmosphereColorHex = 0xFF818CF8,
            atmosphereThickness = 14f,
            axialTiltDegrees = 18f,
            rotationSpeed = 0.8f,
            hasRings = name.lowercase().contains("ring"),
            ringInnerRatio = 1.35f,
            ringOuterRatio = 2.4f,
            ringPrimaryColorHex = 0xFFCBD5E1,
            ringSecondaryColorHex = 0xFF64748B,
            distanceDisplay = "Dozens to thousands of Light-Years from Earth",
            radiusDisplay = "0.8x to 15x Earth Radius",
            massDisplay = "Terrestrial Rocky to Super-Jupiter Mass",
            surfaceTemperatureDisplay = "Dependent on stellar distance & atmospheric greenhouse",
            gravityDisplay = "0.5g to 3.5g",
            orbitalPeriodDisplay = "Days to decades around host star",
            compositionOverview = "Silicate rock, iron core, water oceans, atmospheric trace gases (CH4, CO2, H2O).",
            scientificOverview = "An exoplanet or extrasolar planet is a planet outside the Solar System. Over 5,500 exoplanets have been confirmed by NASA Kepler, TESS, and ground observatories.",
            interestingFacts = listOf(
                "Some exoplanets rain molten iron (WASP-76b) or have clouds made of silicate glass and rubies.",
                "Super-Earths are rocky planets up to twice the size of Earth, which do not exist in our Solar System.",
                "JWST uses transmission spectroscopy to sniff out carbon dioxide, water vapor, and methane in alien atmospheres."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Atmospheric Terminator", description = "Exoplanet boundary layer analyzed during stellar transit spectroscopy.", u = 90f, v = 0f, radiusRatio = 1.1f),
                Space3DHotspot(name = "Exotic Surface Ocean", description = "Supercritical fluid or liquid water ocean maintained by planetary heat.", u = 180f, v = 20f, radiusRatio = 1.04f),
                Space3DHotspot(name = "Stellar Flare Shield", description = "Planetary magnetic field deflecting host star coronal mass ejections.", u = 310f, v = 45f, radiusRatio = 1.25f)
            )
        )
    }

    private fun createAsteroidModel(name: String): Space3DModelData {
        return Space3DModelData(
            id = "asteroid_${System.currentTimeMillis()}",
            name = if (name.isNotBlank()) name.capitalizeFirst() else "Asteroid (C-Type)",
            scientificDesignation = "Minor Planet • Asteroid Belt",
            objectType = Space3DObjectType.ASTEROID_COMET,
            isConfirmedObservationalData = true,
            classificationLabel = "Scientific 3D Model",
            classificationDisclaimer = "3D polygonal radar shape model and spacecraft rendezvous mapping.",
            baseRadius = 88f,
            primaryColorHex = 0xFF64748B,
            secondaryColorHex = 0xFF334155,
            accentColorHex = 0xFF94A3B8,
            texturePattern = "polygonal_rock",
            axialTiltDegrees = 22f,
            rotationSpeed = 1.1f,
            distanceDisplay = "Main Asteroid Belt (~2.1 to 3.3 AU from Sun)",
            radiusDisplay = "500 meters to 470 km (Ceres)",
            massDisplay = "Porous rubble pile to solid nickel-iron",
            surfaceTemperatureDisplay = "-100 °C to -30 °C",
            gravityDisplay = "Microgravity (0.001 to 0.03 g)",
            orbitalPeriodDisplay = "3 to 6 Earth Years",
            compositionOverview = "Carbonaceous chondrites, silicates, nickel-iron metals, trapped volatile ices.",
            scientificOverview = "Asteroids are rocky remnants left over from the early formation of our Solar System about 4.6 billion years ago. Most orbit in the Main Asteroid Belt between Mars and Jupiter.",
            interestingFacts = listOf(
                "Many asteroids are 'rubble piles'—loose collections of boulders held together by weak gravity.",
                "NASA's OSIRIS-REx successfully collected and returned a pristine sample from asteroid Bennu in 2023.",
                "Asteroid 16 Psyche contains enough nickel and gold to exceed the global world economy."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Impact Basin", description = "Ancient impact crater revealing pristine subsurface regolith minerals.", u = 40f, v = 15f, radiusRatio = 1.05f),
                Space3DHotspot(name = "Rubble Pile Boulders", description = "Massive boulders loosely aggregated across the low-gravity surface.", u = 170f, v = -30f, radiusRatio = 1.08f),
                Space3DHotspot(name = "Spacecraft Sample Site", description = "Touch-and-Go sampling site where sample return probes collect dust.", u = 290f, v = 25f, radiusRatio = 1.05f)
            )
        )
    }

    private fun createCometModel(name: String): Space3DModelData {
        return createHalleyCometModel().copy(
            id = "comet_${System.currentTimeMillis()}",
            name = if (name.isNotBlank()) name.capitalizeFirst() else "Active Comet"
        )
    }

    /**
     * Converts an existing local database SpaceObject into a rich interactive 3D model.
     */
    private fun fromSpaceObject(obj: SpaceObject): Space3DModelData {
        val q = obj.name.lowercase(Locale.ROOT)
        val baseModel = getModelForQuery(q)

        return baseModel.copy(
            name = obj.name,
            scientificDesignation = "${obj.designation} • ${obj.category.label}",
            distanceDisplay = obj.distanceDisplay,
            radiusDisplay = obj.radiusDisplay,
            massDisplay = obj.massDisplay,
            surfaceTemperatureDisplay = obj.temperatureDisplay,
            gravityDisplay = obj.gravitationalPullDisplay,
            scientificOverview = obj.overview,
            interestingFacts = listOf(obj.fascinatingMechanics)
        )
    }

    /**
     * Dynamically synthesizes a physically plausible 3D model configuration for ANY unknown search query.
     */
    private fun synthesizeDynamicModel(query: String): Space3DModelData {
        val q = query.trim().lowercase(Locale.ROOT)

        val objectType = when {
            q.contains("planet") || q.contains("mercury") || q.contains("venus") || q.contains("uranus") || q.contains("neptune") || q.contains("pluto") -> Space3DObjectType.PLANET_TERRESTRIAL
            q.contains("moon") || q.contains("europa") || q.contains("titan") || q.contains("enceladus") || q.contains("io") || q.contains("ganymede") || q.contains("callisto") || q.contains("triton") -> Space3DObjectType.MOON
            q.contains("star") || q.contains("sun") || q.contains("dwarf") || q.contains("giant") || q.contains("supergiant") || q.contains("hypergiant") -> Space3DObjectType.STAR
            q.contains("black hole") || q.contains("singularity") || q.contains("quasar") || q.contains("agn") || q.contains("ton") -> Space3DObjectType.BLACK_HOLE
            q.contains("pulsar") || q.contains("neutron") || q.contains("magnetar") -> Space3DObjectType.NEUTRON_STAR_PULSAR
            q.contains("galaxy") || q.contains("spiral") || q.contains("elliptical") -> Space3DObjectType.GALAXY_SPIRAL
            q.contains("nebula") || q.contains("supernova") || q.contains("cloud") -> Space3DObjectType.NEBULA
            q.contains("asteroid") || q.contains("meteor") -> Space3DObjectType.ASTEROID_COMET
            q.contains("comet") -> Space3DObjectType.ASTEROID_COMET
            q.contains("spacecraft") || q.contains("satellite") || q.contains("station") || q.contains("probe") || q.contains("telescope") || q.contains("rover") -> Space3DObjectType.SPACECRAFT_STATION
            else -> Space3DObjectType.EXOPLANET
        }

        val isConfirmed = q.contains("earth") || q.contains("mars") || q.contains("moon") || q.contains("sun") || q.contains("jupiter") || q.contains("saturn") || q.contains("iss") || q.contains("jwst")

        val (primaryColor, secondaryColor, accentColor, texture) = when (objectType) {
            Space3DObjectType.PLANET_TERRESTRIAL -> Tuple4(0xFFB45309, 0xFF78350F, 0xFFFDE68A, "continents")
            Space3DObjectType.PLANET_GAS_GIANT -> Tuple4(0xFFD97706, 0xFF92400E, 0xFFFCD34D, "bands")
            Space3DObjectType.MOON -> Tuple4(0xFF94A3B8, 0xFF475569, 0xFFE2E8F0, "cratered")
            Space3DObjectType.STAR -> Tuple4(0xFFF59E0B, 0xFFDC2626, 0xFFFEF08A, "granulation")
            Space3DObjectType.BLACK_HOLE -> Tuple4(0xFF000000, 0xFFEA580C, 0xFF38BDF8, "plasma")
            Space3DObjectType.NEUTRON_STAR_PULSAR -> Tuple4(0xFF38BDF8, 0xFF818CF8, 0xFFFFFFFF, "plasma")
            Space3DObjectType.GALAXY_SPIRAL, Space3DObjectType.GALAXY_ELLIPTICAL -> Tuple4(0xFF38BDF8, 0xFFF59E0B, 0xFF818CF8, "spiral_arms")
            Space3DObjectType.NEBULA -> Tuple4(0xFF0284C7, 0xFFC084FC, 0xFFF43F5E, "filaments")
            Space3DObjectType.ASTEROID_COMET -> Tuple4(0xFF64748B, 0xFF334155, 0xFF94A3B8, "polygonal_rock")
            Space3DObjectType.SPACECRAFT_STATION -> Tuple4(0xFFE2E8F0, 0xFF3B82F6, 0xFFF59E0B, "tech_hull")
            Space3DObjectType.EXOPLANET -> Tuple4(0xFF0284C7, 0xFF059669, 0xFF38BDF8, "continents")
        }

        return Space3DModelData(
            id = "dynamic_${System.currentTimeMillis()}",
            name = query.trim().capitalizeWords(),
            scientificDesignation = "Celestial Target • ${objectType.label}",
            objectType = objectType,
            isConfirmedObservationalData = isConfirmed,
            classificationLabel = if (isConfirmed) "Scientific 3D Model" else "Scientific visualization / Artist’s 3D model",
            classificationDisclaimer = if (isConfirmed) 
                "Constructed from confirmed observational and radar telemetry." 
            else 
                "Scientific visualization / Artist’s 3D model • Modeled dynamically from astrophysical parameters and spectral classifications.",
            baseRadius = 110f,
            primaryColorHex = primaryColor,
            secondaryColorHex = secondaryColor,
            accentColorHex = accentColor,
            texturePattern = texture,
            hasAtmosphere = objectType == Space3DObjectType.PLANET_TERRESTRIAL || objectType == Space3DObjectType.PLANET_GAS_GIANT || objectType == Space3DObjectType.EXOPLANET || objectType == Space3DObjectType.STAR,
            atmosphereColorHex = if (objectType == Space3DObjectType.STAR) 0xFFFDE047 else 0xFF60A5FA,
            atmosphereThickness = 14f,
            axialTiltDegrees = 15f,
            rotationSpeed = 0.9f,
            hasRings = q.contains("ring") || q.contains("saturn"),
            hasAccretionDisk = objectType == Space3DObjectType.BLACK_HOLE,
            hasPhotonSphere = objectType == Space3DObjectType.BLACK_HOLE,
            hasRelativisticJets = objectType == Space3DObjectType.BLACK_HOLE || objectType == Space3DObjectType.NEUTRON_STAR_PULSAR,
            hasSolarPanels = objectType == Space3DObjectType.SPACECRAFT_STATION,
            hasCometTail = q.contains("comet"),
            distanceDisplay = "Deep Cosmic Coordinates",
            radiusDisplay = "Astrophysically Scaled",
            massDisplay = "Dynamic Gravitational Mass",
            surfaceTemperatureDisplay = "Astrophysical Thermal Profile",
            gravityDisplay = "Consistent with Relativistic Metrics",
            orbitalPeriodDisplay = "Cosmic Trajectory",
            compositionOverview = "Synthesized celestial matter distribution based on astrophysical classification.",
            scientificOverview = "Interactive 3D model dynamically rendered for '$query'. The visualization simulates physical geometry, surface lighting, atmospheric scattering, and feature hotspots based on known astronomy principles.",
            interestingFacts = listOf(
                "Rendered dynamically using real-time 3D Canvas geometry with full 360° interactive rotation, pitch, and zoom.",
                "Astrophysical parameters are synthesized based on celestial body classification and spectral laws."
            ),
            hotspots = listOf(
                Space3DHotspot(name = "Central Feature", description = "Core astrophysical structural zone of $query.", u = 0f, v = 0f, radiusRatio = 1.05f),
                Space3DHotspot(name = "Equatorial Band", description = "Rotational momentum plane and atmospheric flow.", u = 90f, v = 15f, radiusRatio = 1.05f),
                Space3DHotspot(name = "Polar Region", description = "Magnetic or axial rotational coordinate point.", u = 270f, v = 75f, radiusRatio = 1.05f)
            )
        )
    }

    private fun String.capitalizeFirst(): String {
        return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }

    private fun String.capitalizeWords(): String {
        return split(" ").joinToString(" ") { it.capitalizeFirst() }
    }

    private data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)
}
