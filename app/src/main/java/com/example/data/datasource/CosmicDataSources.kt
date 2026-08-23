package com.example.data.datasource

import com.example.R
import com.example.data.model.LightTimePreset
import com.example.data.model.ObjectCategory
import com.example.data.model.QuizQuestion
import com.example.data.model.ScientificCertainty
import com.example.data.model.ScientificStatus
import com.example.data.model.SpaceFact
import com.example.data.model.SpaceObject

object CosmicDataSources {

    val spaceFacts: List<SpaceFact> = listOf(
        SpaceFact(
            id = "fact_1",
            title = "A Day on Venus Outlasts Its Entire Year",
            summary = "Venus rotates so slowly on its axis that one full rotation takes 243 Earth days, while its orbit around the Sun takes only 225 Earth days.",
            detailedExplanation = "Venus also spins retrograde (clockwise), meaning the Sun rises in the west and sets in the east. Scientists theorize this extreme slow and backwards rotation was caused by massive asteroid impacts during the early solar system or strong atmospheric tidal friction.",
            category = "Solar System",
            certainty = ScientificCertainty.CONFIRMED_OBSERVATION,
            observationalSource = "Magellan Radar & Planetary Radar Observations",
            quote = "Venus is an Earth-sized mirror of what planetary atmospheric runaway can become.",
            dayOfYear = 1
        ),
        SpaceFact(
            id = "fact_2",
            title = "Neutron Star Sugar Cubes Weigh a Billion Tons",
            summary = "A single sugar-cube volume (~1 cm³) of neutron star material would weigh approximately 1 billion metric tons on Earth.",
            detailedExplanation = "Neutron stars are remnants of massive collapsed stars. The gravitational collapse crushes electrons into protons, forming closely packed neutrons. The density is roughly equal to compressing the entire human population into the size of a single sugar cube.",
            category = "Stellar Physics",
            certainty = ScientificCertainty.CONFIRMED_OBSERVATION,
            observationalSource = "NICER X-Ray Timing & Radio Pulsar Timing",
            quote = "In neutron stars, matter is compressed to the atomic nucleus limit.",
            dayOfYear = 2
        ),
        SpaceFact(
            id = "fact_3",
            title = "Time Runs Slower Near Earth's Core than on Everest",
            summary = "Due to Einstein's General Relativity, Earth's center is approximately 2.5 years younger than its crust.",
            detailedExplanation = "Gravitational time dilation dictates that the closer an observer is to a massive body, the slower time ticks for them relative to a distant observer. Over Earth's 4.5 billion-year history, this infinitesimal difference accumulated to about 2.5 years.",
            category = "Relativistic Physics",
            certainty = ScientificCertainty.ROBUST_THEORY,
            observationalSource = "Atomic Clock Heights & Optical Lattice Clock Tests (NIST)",
            quote = "Gravity is not just a force pulling masses; it bends the very flow of time.",
            dayOfYear = 3
        ),
        SpaceFact(
            id = "fact_4",
            title = "The Observable Universe Spans 93 Billion Light-Years",
            summary = "Even though the universe is only 13.8 billion years old, the observable universe is 93 billion light-years across due to cosmic spatial expansion.",
            detailedExplanation = "Light emitted shortly after the Big Bang has traveled for 13.8 billion years, but the space through which it traveled has been stretching continuously. The points that emitted that ancient cosmic microwave background are now estimated to be ~46.5 billion light-years away in every direction.",
            category = "Cosmology",
            certainty = ScientificCertainty.ROBUST_THEORY,
            observationalSource = "Planck Satellite & WMAP Cosmic Microwave Background",
            quote = "Space can expand faster than the speed of light without violating Special Relativity.",
            dayOfYear = 4
        ),
        SpaceFact(
            id = "fact_5",
            title = "It Rains Molten Iron on Exoplanet WASP-76b",
            summary = "On the ultra-hot tidally locked gas giant WASP-76b, day-side temperatures vaporize iron, which blows to the night side and condenses into liquid metal rain.",
            detailedExplanation = "The day side reaches blazing temperatures above 2,400°C (4,350°F), splitting molecular bonds and turning iron into atmospheric vapor. High-altitude hypersonic winds carry this iron gas to the cooler 1,500°C night side where it liquifies and falls as molten rain droplets.",
            category = "Exoplanets",
            certainty = ScientificCertainty.CONFIRMED_OBSERVATION,
            observationalSource = "ESPRESSO Spectrograph on ESO's Very Large Telescope",
            quote = "Exoplanet weather systems showcase physics far beyond our solar system.",
            dayOfYear = 5
        ),
        SpaceFact(
            id = "fact_6",
            title = "Black Holes Evaporate Through Hawking Radiation",
            summary = "Over unthinkably long cosmological timescales, quantum fluctuations cause black holes to slowly leak energy and eventually vanish in a burst of gamma rays.",
            detailedExplanation = "Virtual particle-antiparticle pairs form continuously near the event horizon. When one particle falls into the black hole and the other escapes with positive energy, the black hole loses mass. A stellar-mass black hole takes around 10^67 years to completely evaporate.",
            category = "Quantum Space",
            certainty = ScientificCertainty.THEORETICAL_MODEL,
            observationalSource = "Stephen Hawking (1974) Theoretical Derivation",
            quote = "Black holes ain't as black as they are painted. They are not eternal prisons.",
            dayOfYear = 6
        ),
        SpaceFact(
            id = "fact_7",
            title = "Galactic Center Sagittarius A* Consumes Millions of Earths",
            summary = "The supermassive black hole at the center of our Milky Way contains the mass of roughly 4.15 million Suns packed within the radius of Mercury's orbit.",
            detailedExplanation = "Astronomers verified its mass by tracking stars like S2 orbiting it at speeds exceeding 7,000 km/s (nearly 3% the speed of light). In 2022, the Event Horizon Telescope captured the direct radio shadow of Sagittarius A*.",
            category = "Black Holes",
            certainty = ScientificCertainty.CONFIRMED_OBSERVATION,
            observationalSource = "Event Horizon Telescope (EHT) & Nobel Prize Team (Genzel & Ghez)",
            quote = "A gravitational anchor holding the choreography of 100 billion stars.",
            dayOfYear = 7
        ),
        SpaceFact(
            id = "fact_8",
            title = "You Are Looking Millions of Years into the Past",
            summary = "When you look at the Andromeda Galaxy through a telescope, you see light that left 2.5 million years ago, when early human ancestors were first shaping stone tools.",
            detailedExplanation = "Because light travels at a finite velocity (c ≈ 299,792 km/s), all astronomical observations are snapshots of the past. Looking deeper into space is fundamentally looking backward in cosmic time.",
            category = "Cosmology",
            certainty = ScientificCertainty.CONFIRMED_OBSERVATION,
            observationalSource = "Astrophysical Distance Ladder & Cepheid Variable Stars",
            quote = "A telescope is not just an amplifier of light; it is a time machine.",
            dayOfYear = 8
        ),
        SpaceFact(
            id = "fact_9",
            title = "Magnetars Have Magnetic Fields Strong Enough to Dissolve Atoms",
            summary = "A magnetar possesses a magnetic field of up to 10^11 Tesla—a quadrillion times stronger than Earth's magnetic field.",
            detailedExplanation = "At a distance of 1,000 km from a magnetar, its magnetic field gradient would distort the electron clouds of atoms in human tissue into needle-thin shapes, instantly destroying molecular biology. Starquakes on their solid crust release monstrous bursts of gamma radiation.",
            category = "Stellar Physics",
            certainty = ScientificCertainty.CONFIRMED_OBSERVATION,
            observationalSource = "NASA Swift & Fermi Gamma-Ray Space Telescope",
            quote = "Magnetars represent the most intense magnetic fields in the known universe.",
            dayOfYear = 9
        ),
        SpaceFact(
            id = "fact_10",
            title = "Diamonds May Rain in the Interiors of Neptune and Uranus",
            summary = "Extreme heat and atmospheric pressures thousands of kilometers below the ice giant clouds compress methane into solid crystalline diamond precipitation.",
            detailedExplanation = "High-pressure laser experiments mimicking the interiors of Neptune and Uranus have verified that hydrocarbons decompose into hydrogen and carbon, where the carbon precipitates as diamond 'hailstones' sinking toward the planetary core.",
            category = "Solar System",
            certainty = ScientificCertainty.ROBUST_THEORY,
            observationalSource = "SLAC National Accelerator Laboratory & Voyager 2 Data",
            quote = "Deep planetary mantle chemistry turns common organic gases into gems.",
            dayOfYear = 10
        ),
        SpaceFact(
            id = "fact_11",
            title = "The Universe Will End in Cold Silence: The Big Freeze",
            summary = "Current cosmological evidence indicates the universe will continue accelerating in expansion, eventually cooling until all star formation ceases.",
            detailedExplanation = "Over 100 trillion years from now, the last red dwarf stars will burn out. White dwarfs and neutron stars will cool into black dwarfs, and eventually only diluted photons, leptons, and black holes will remain in thermodynamic heat death.",
            category = "Cosmology",
            certainty = ScientificCertainty.ROBUST_THEORY,
            observationalSource = "Type Ia Supernovae Dark Energy Discovery (1998)",
            quote = "Some say the world will end in fire, some say in ice. Thermodynamics favors ice.",
            dayOfYear = 11
        ),
        SpaceFact(
            id = "fact_12",
            title = "Footprints on the Moon Will Last for Millions of Years",
            summary = "Because the Moon has no atmosphere, liquid water, or wind, Apollo astronaut footprints will remain intact for millions of years.",
            detailedExplanation = "The only natural forces eroding lunar surface features are micrometeorite impacts and solar wind bombardment, which erode lunar regolith at a glacial rate of only ~1 millimeter every 1 million years.",
            category = "Solar System",
            certainty = ScientificCertainty.CONFIRMED_OBSERVATION,
            observationalSource = "Apollo Lunar Regolith Samples & Lunar Reconnaissance Orbiter (LRO)",
            quote = "Our first steps beyond Earth are frozen in lunar dust across geological time.",
            dayOfYear = 12
        )
    )

    val spaceObjects: List<SpaceObject> = listOf(
        SpaceObject(
            id = "obj_sagittarius_a",
            name = "Sagittarius A*",
            designation = "Milky Way SMBH",
            category = ObjectCategory.BLACK_HOLE,
            status = ScientificStatus.OBSERVED_CONFIRMED,
            distanceLightYears = 26670.0,
            distanceDisplay = "26,670 light-years",
            massDisplay = "4.154 × 10⁶ Solar Masses",
            radiusDisplay = "~12 million km (Event Horizon)",
            temperatureDisplay = "1.5 × 10⁻¹⁴ K (Hawking Temp)",
            gravitationalPullDisplay = "Extreme (Relativistic Infall)",
            discoveryYear = "1974 (EHT Direct Image: 2022)",
            overview = "The supermassive black hole anchoring our galaxy. In May 2022, the Event Horizon Telescope revealed the glowing ring of plasma orbiting its event horizon.",
            fascinatingMechanics = "Time slows to a complete stop for a distant observer watching an object approach its Schwarzschild radius. The surrounding gas rotates at relativistic fractions of light speed.",
            imageDrawableRes = R.drawable.img_black_hole,
            tags = listOf("Supermassive", "Milky Way Center", "Relativity", "Event Horizon")
        ),
        SpaceObject(
            id = "obj_ton_618",
            name = "TON 618",
            designation = "Ultramassive Quasar Black Hole",
            category = ObjectCategory.BLACK_HOLE,
            status = ScientificStatus.OBSERVED_CONFIRMED,
            distanceLightYears = 18200000000.0,
            distanceDisplay = "18.2 billion light-years (co-moving)",
            massDisplay = "66 × 10⁹ Solar Masses",
            radiusDisplay = "~390 billion km (40x Solar System)",
            temperatureDisplay = "Accretion Disk > 1,000,000 K",
            gravitationalPullDisplay = "Catastrophic Ultramassive",
            discoveryYear = "1957 (Mass confirmed 2004)",
            overview = "One of the most massive black holes ever discovered. It contains the equivalent mass of 66 billion suns and outshines its host galaxy by thousands of times.",
            fascinatingMechanics = "The accretion disk surrounding TON 618 emits 140 trillion times more light energy than our Sun. Its event horizon alone could comfortably swallow multiple entire solar systems.",
            imageDrawableRes = R.drawable.img_black_hole,
            tags = listOf("Ultramassive", "Quasar", "Accretion Engine", "Deep Space")
        ),
        SpaceObject(
            id = "obj_crab_pulsar",
            name = "Crab Pulsar",
            designation = "PSR B0531+21",
            category = ObjectCategory.NEUTRON_STAR_PULSAR,
            status = ScientificStatus.OBSERVED_CONFIRMED,
            distanceLightYears = 6500.0,
            distanceDisplay = "6,500 light-years",
            massDisplay = "1.4 - 2.0 Solar Masses",
            radiusDisplay = "~10 - 15 km",
            temperatureDisplay = "1.6 × 10⁶ K (Surface)",
            gravitationalPullDisplay = "10¹¹ g (Earth surface = 1g)",
            discoveryYear = "1968 (Supernova 1054 CE)",
            overview = "A rapidly spinning neutron star created in the historical supernova of 1054 CE, documented by ancient astronomers across Asia and the Americas.",
            fascinatingMechanics = "It spins at 30 rotations per second, sweeping twin beams of electromagnetic radiation across space like an ultra-precise cosmic lighthouse.",
            imageDrawableRes = R.drawable.img_pulsar_star,
            tags = listOf("Pulsar", "Supernova Remnant", "Extreme Density", "30 Hz Spin")
        ),
        SpaceObject(
            id = "obj_sgr_1806",
            name = "SGR 1806-20",
            designation = "Hyper-Magnetic Magnetar",
            category = ObjectCategory.NEUTRON_STAR_PULSAR,
            status = ScientificStatus.OBSERVED_CONFIRMED,
            distanceLightYears = 50000.0,
            distanceDisplay = "50,000 light-years",
            massDisplay = "~1.4 Solar Masses",
            radiusDisplay = "~10 km",
            temperatureDisplay = "10⁷ K (Core)",
            gravitationalPullDisplay = "10¹¹ g | B-Field: 10¹¹ Tesla",
            discoveryYear = "1979",
            overview = "The most magnetized known object in the universe. In 2004, a single starquake on its surface released more energy in 0.1s than our Sun emits in 150,000 years.",
            fascinatingMechanics = "Its magnetic field is so immense that vacuum polarization alters the speed of light depending on polarization direction, creating quantum optical birefringence.",
            imageDrawableRes = R.drawable.img_pulsar_star,
            tags = listOf("Magnetar", "Extreme B-Field", "Starquake", "Gamma-Ray Burst")
        ),
        SpaceObject(
            id = "obj_andromeda",
            name = "Andromeda Galaxy",
            designation = "Messier 31 / NGC 224",
            category = ObjectCategory.GALAXY,
            status = ScientificStatus.OBSERVED_CONFIRMED,
            distanceLightYears = 2500000.0,
            distanceDisplay = "2.5 million light-years",
            massDisplay = "~1.5 × 10¹² Solar Masses",
            radiusDisplay = "~110,000 light-years",
            temperatureDisplay = "Variable (Coronal gas: 10⁶ K)",
            gravitationalPullDisplay = "Local Group Dominant",
            discoveryYear = "964 CE (Abd al-Rahman al-Sufi)",
            overview = "The closest major spiral galaxy to the Milky Way, containing approximately 1 trillion stars. It is hurtling toward us at ~110 km/s.",
            fascinatingMechanics = "In roughly 4.5 billion years, Andromeda and the Milky Way will merge to create a giant elliptical galaxy dubbed 'Milkomeda'. Star collisions will be rare due to vast interstellar voids.",
            imageDrawableRes = R.drawable.img_hero_cosmos,
            tags = listOf("Spiral Galaxy", "Local Group", "Galactic Collision", "1 Trillion Stars")
        ),
        SpaceObject(
            id = "obj_jades_z14",
            name = "JADES-GS-z14-0",
            designation = "Cosmic Dawn Galaxy",
            category = ObjectCategory.GALAXY,
            status = ScientificStatus.OBSERVED_CONFIRMED,
            distanceLightYears = 33800000000.0,
            distanceDisplay = "33.8 billion light-years (Proper)",
            massDisplay = "~5 × 10⁸ Solar Masses",
            radiusDisplay = "~1,600 light-years",
            temperatureDisplay = "Active Starburst",
            gravitationalPullDisplay = "Early Cosmic Well",
            discoveryYear = "2024 (JWST)",
            overview = "Observed by the James Webb Space Telescope just 290 million years after the Big Bang, challenging existing models of how early galaxies formed so rapidly.",
            fascinatingMechanics = "Spectroscopic analysis reveals oxygen ionization, proving that multiple generations of massive stars had already enriched the gas in the newborn universe.",
            imageDrawableRes = R.drawable.img_hero_cosmos,
            tags = listOf("JWST", "Cosmic Dawn", "Redshift z=14.3", "Early Universe")
        ),
        SpaceObject(
            id = "obj_55_cancri_e",
            name = "55 Cancri e",
            designation = "Janssen (Super-Earth)",
            category = ObjectCategory.EXOPLANET,
            status = ScientificStatus.OBSERVED_CONFIRMED,
            distanceLightYears = 41.0,
            distanceDisplay = "41 light-years",
            massDisplay = "8.6 Earth Masses",
            radiusDisplay = "1.875 Earth Radii",
            temperatureDisplay = "~2,700 K (Substellar Point)",
            gravitationalPullDisplay = "2.4 g (Earth = 1g)",
            discoveryYear = "2004",
            overview = "A scorched super-Earth orbiting its parent star in just 18 hours. Its high carbon content and immense interior pressure suggest a mantle made largely of diamond.",
            fascinatingMechanics = "Its surface is covered by a global magma ocean. JWST observations in 2024 detected evidence of a secondary volatile atmosphere formed from boiling molten rock.",
            imageDrawableRes = R.drawable.img_black_hole,
            tags = listOf("Diamond Planet", "Lava Ocean", "Super-Earth", "18-Hour Orbit")
        ),
        SpaceObject(
            id = "obj_wasp_76b",
            name = "WASP-76b",
            designation = "Ultra-Hot Iron-Rain Jupiter",
            category = ObjectCategory.EXOPLANET,
            status = ScientificStatus.OBSERVED_CONFIRMED,
            distanceLightYears = 634.0,
            distanceDisplay = "634 light-years",
            massDisplay = "0.92 Jupiter Masses",
            radiusDisplay = "1.83 Jupiter Radii",
            temperatureDisplay = "2,670 K (Day) / 1,770 K (Night)",
            gravitationalPullDisplay = "0.68 g",
            discoveryYear = "2013",
            overview = "An inflated gas giant so close to its star that its day-side vaporizes metals. Strong atmospheric jet streams transport iron vapor to the night side where it rains down as molten iron.",
            fascinatingMechanics = "Tidal locking forces eternal day on one side and eternal night on the other, driving catastrophic 10,000 km/h thermal winds across the terminator line.",
            imageDrawableRes = R.drawable.img_black_hole,
            tags = listOf("Iron Rain", "Tidally Locked", "Ultra-Hot Jupiter", "Extreme Atmosphere")
        ),
        SpaceObject(
            id = "obj_pillars_creation",
            name = "Pillars of Creation",
            designation = "Eagle Nebula / Messier 16",
            category = ObjectCategory.QUASAR_NEBULA,
            status = ScientificStatus.OBSERVED_CONFIRMED,
            distanceLightYears = 6500.0,
            distanceDisplay = "6,500 light-years",
            massDisplay = "~200 Solar Masses (Gas/Dust)",
            radiusDisplay = "~4-5 light-years tall",
            temperatureDisplay = "10 - 100 K (Cold Molecular Gas)",
            gravitationalPullDisplay = "Protostellar Collapse Zones",
            discoveryYear = "1745 (Hubble Iconic: 1995)",
            overview = "Towering tendrils of interstellar hydrogen gas and dust in the Eagle Nebula. These colossal columns act as incubators where new protostars are actively condensing.",
            fascinatingMechanics = "Ultraviolet radiation from nearby hot young stars is evaporating the gas pillars away through photoevaporation, sculpting evaporating gaseous globules (EGGs).",
            imageDrawableRes = R.drawable.img_hero_cosmos,
            tags = listOf("Nebula", "Stellar Nursery", "Hubble / JWST Icon", "Protostars")
        ),
        SpaceObject(
            id = "obj_3c_273",
            name = "3C 273",
            designation = "First Discovered Quasar",
            category = ObjectCategory.QUASAR_NEBULA,
            status = ScientificStatus.OBSERVED_CONFIRMED,
            distanceLightYears = 2400000000.0,
            distanceDisplay = "2.4 billion light-years",
            massDisplay = "~886 million Solar Masses",
            radiusDisplay = "Relativistic Jet > 300,000 light-years",
            temperatureDisplay = "10¹⁰ K (Jet Plasma)",
            gravitationalPullDisplay = "Ultra-Relativistic",
            discoveryYear = "1963 (Maarten Schmidt)",
            overview = "The very first quasar identified. It shines with the luminosity of over 4 trillion suns and ejects a relativistic plasma jet extending 300,000 light-years across intergalactic space.",
            fascinatingMechanics = "Quasars are powered by supermassive black holes gorging on surrounding matter at peak Eddington limits, releasing immense gravitational binding energy as light.",
            imageDrawableRes = R.drawable.img_pulsar_star,
            tags = listOf("Quasar", "Active Galactic Nucleus", "4 Trillion Suns", "Plasma Jet")
        )
    )

    val quizQuestions: List<QuizQuestion> = listOf(
        QuizQuestion(
            id = "q1",
            question = "According to Einstein's General Relativity, what happens to the passage of time near a black hole's event horizon?",
            options = listOf(
                "Time speeds up dramatically",
                "Time slows down relative to a distant observer",
                "Time flows backwards in a closed loop",
                "Time is completely unaffected by gravity"
            ),
            correctIndex = 1,
            scientificExplanation = "Gravitational time dilation causes clocks in deeper gravitational potential wells to tick slower compared to clocks in weaker gravitational fields. Near an event horizon, this dilation factor approaches infinity.",
            category = "Relativistic Physics",
            difficulty = "Novice"
        ),
        QuizQuestion(
            id = "q2",
            question = "How long does sunlight take to reach Earth on average?",
            options = listOf(
                "Instantaneous (0 seconds)",
                "About 8 minutes and 20 seconds",
                "About 1 hour and 15 minutes",
                "24 hours"
            ),
            correctIndex = 1,
            scientificExplanation = "The average distance from the Sun to Earth (1 Astronomical Unit) is ~149.6 million kilometers. Traveling at 299,792 km/s, photons take roughly 499 seconds (8 minutes 19 seconds) to arrive.",
            category = "Light Speed",
            difficulty = "Novice"
        ),
        QuizQuestion(
            id = "q3",
            question = "What prevents a neutron star from collapsing further into a black hole?",
            options = listOf(
                "Electron degeneracy pressure",
                "Neutron degeneracy pressure",
                "Nuclear fusion in its core",
                "Dark energy repulsion"
            ),
            correctIndex = 1,
            scientificExplanation = "Neutron degeneracy pressure, governed by the Pauli Exclusion Principle, prevents neutrons from occupying the same quantum states. If the remnant mass exceeds the Tolman-Oppenheimer-Volkoff limit (~2.2 solar masses), it collapses into a black hole.",
            category = "Stellar Physics",
            difficulty = "Intermediate"
        ),
        QuizQuestion(
            id = "q4",
            question = "What was the very first black hole ever directly imaged in human history?",
            options = listOf(
                "Sagittarius A* in the Milky Way",
                "Messier 87* (M87*) in the Virgo Cluster",
                "Cygnus X-1 in the Cygnus constellation",
                "TON 618 in Canes Venatici"
            ),
            correctIndex = 1,
            scientificExplanation = "In April 2019, the Event Horizon Telescope (EHT) collaboration released the historic first image of M87*, a supermassive black hole 53 million light-years away with 6.5 billion solar masses.",
            category = "Black Holes",
            difficulty = "Intermediate"
        ),
        QuizQuestion(
            id = "q5",
            question = "Why does the observable universe have a radius of ~46.5 billion light-years when its age is only 13.8 billion years?",
            options = listOf(
                "Light traveled faster in the early universe",
                "The metric expansion of space stretched the distances",
                "Galaxies travel through space faster than light speed",
                "Cosmic dust bent the light paths into spirals"
            ),
            correctIndex = 1,
            scientificExplanation = "While no object can move through space faster than light, space itself can expand without limit. As light traveled for 13.8 billion years, the distance between the emitter and Earth expanded to 46.5 billion light-years.",
            category = "Cosmology",
            difficulty = "Astrophysicist"
        ),
        QuizQuestion(
            id = "q6",
            question = "What is the boundary around a black hole beyond which nothing—not even light—can escape called?",
            options = listOf(
                "Photon Sphere",
                "Event Horizon (Schwarzschild Radius)",
                "Ergosphere",
                "Accretion Boundary"
            ),
            correctIndex = 1,
            scientificExplanation = "The Event Horizon marks the point where the escape velocity equals the speed of light in vacuum. For a non-rotating black hole, this radius is given by r_s = 2GM/c².",
            category = "Black Holes",
            difficulty = "Novice"
        ),
        QuizQuestion(
            id = "q7",
            question = "Which type of celestial object has the most intense magnetic field in the known universe?",
            options = listOf(
                "White Dwarf",
                "Magnetar",
                "Red Supergiant",
                "Main-sequence O-type star"
            ),
            correctIndex = 1,
            scientificExplanation = "Magnetars are a subtype of neutron star with magnetic fields up to 10^11 Tesla (a quadrillion Gauss), powerful enough to dissolve electron orbital shells in nearby atoms.",
            category = "Stellar Physics",
            difficulty = "Intermediate"
        ),
        QuizQuestion(
            id = "q8",
            question = "What phenomenon causes the light from distant galaxies to shift toward longer, redder wavelengths?",
            options = listOf(
                "Gravitational absorption",
                "Cosmological redshift due to cosmic expansion",
                "Interstellar diamond dust scattering",
                "Solar wind deflections"
            ),
            correctIndex = 1,
            scientificExplanation = "As photons travel through expanding space over billions of years, their wavelengths are stretched proportionally to the scale factor of the universe, shifting absorption lines toward the red end of the spectrum.",
            category = "Cosmology",
            difficulty = "Novice"
        ),
        QuizQuestion(
            id = "q9",
            question = "What famous theoretical radiation predicts that black holes slowly lose mass and eventually evaporate?",
            options = listOf(
                "Synchrotron Radiation",
                "Hawking Radiation",
                "Cherenkov Radiation",
                "Bremmsstrahlung Radiation"
            ),
            correctIndex = 1,
            scientificExplanation = "Stephen Hawking formulated in 1974 that quantum vacuum fluctuations near an event horizon cause black holes to emit blackbody thermal radiation, slowly losing mass until explosive evaporation.",
            category = "Quantum Space",
            difficulty = "Intermediate"
        ),
        QuizQuestion(
            id = "q10",
            question = "If an astronaut travels at 99.5% the speed of light for 1 year according to their own ship clock, roughly how much time passes on Earth?",
            options = listOf(
                "Exactly 1 year",
                "Approximately 10 years",
                "About 1 month",
                "100 years"
            ),
            correctIndex = 1,
            scientificExplanation = "Using the Lorentz factor γ = 1 / √(1 - v²/c²), at v = 0.995c, γ ≈ 10.01. Thus, 1 year aboard the relativistic ship corresponds to ~10 years elapsed on Earth.",
            category = "Relativistic Physics",
            difficulty = "Astrophysicist"
        )
    )
}

object LightTimePresetsData {
    val presets: List<LightTimePreset> = listOf(
        LightTimePreset(
            id = "moon",
            name = "The Moon (Perigee/Apogee average)",
            distanceDisplay = "384,400 km (~0.00257 AU)",
            distanceKm = 384400.0,
            lightTravelSeconds = 1.282,
            lightTravelFormatted = "1.28 seconds",
            historicalEarthAnchor = "Just over 1 second ago — shorter than a human heartbeat.",
            scientificNote = "Laser ranging retroreflectors left by Apollo missions bounce photons off the Moon to measure this distance down to millimeter precision."
        ),
        LightTimePreset(
            id = "sun",
            name = "The Sun (1 AU)",
            distanceDisplay = "149,597,870 km (1 AU)",
            distanceKm = 149597870.0,
            lightTravelSeconds = 499.0,
            lightTravelFormatted = "8 minutes 19 seconds",
            historicalEarthAnchor = "About the time it takes to brew a pot of coffee and read morning headlines.",
            scientificNote = "If the Sun were to suddenly blink out, Earth would continue in its orbit in the dark for over 8 minutes before feeling any gravitational or visual change."
        ),
        LightTimePreset(
            id = "mars_closest",
            name = "Mars (Closest Approach)",
            distanceDisplay = "54.6 million km (0.36 AU)",
            distanceKm = 54600000.0,
            lightTravelSeconds = 182.1,
            lightTravelFormatted = "3 minutes 2 seconds",
            historicalEarthAnchor = "Longer than a quick phone call, creating communication lag for Mars rovers.",
            scientificNote = "Because radio waves travel at light speed, Mars rovers like Perseverance must drive autonomously with hazard detection AI."
        ),
        LightTimePreset(
            id = "jupiter",
            name = "Jupiter (Opposition)",
            distanceDisplay = "588 million km (3.93 AU)",
            distanceKm = 588000000.0,
            lightTravelSeconds = 1961.0,
            lightTravelFormatted = "32 minutes 41 seconds",
            historicalEarthAnchor = "Half an hour ago on Earth.",
            scientificNote = "Ole Rømer in 1676 first estimated the finite speed of light by measuring the variations in the timing of Jupiter's moon Io's eclipses."
        ),
        LightTimePreset(
            id = "voyager_1",
            name = "Voyager 1 (Interstellar Space)",
            distanceDisplay = "24.5 billion km (~163.8 AU)",
            distanceKm = 24500000000.0,
            lightTravelSeconds = 81723.0,
            lightTravelFormatted = "22 hours 42 minutes",
            historicalEarthAnchor = "Yesterday on Earth.",
            scientificNote = "Voyager 1 is humanity's most distant manufactured object, transmitting radio signals that take nearly a full day to reach NASA Deep Space Network antennas."
        ),
        LightTimePreset(
            id = "alpha_centauri",
            name = "Proxima / Alpha Centauri",
            distanceDisplay = "4.246 light-years (4.017 × 10¹³ km)",
            distanceKm = 4.017e13,
            lightTravelSeconds = 4.246 * 365.25 * 86400,
            lightTravelFormatted = "4.25 years",
            historicalEarthAnchor = "A high-school freshman started and finished secondary school while this light was in transit.",
            scientificNote = "The nearest star system to our Sun. Even with the fastest chemical rockets ever built, a probe would take over 75,000 years to reach it."
        ),
        LightTimePreset(
            id = "betelgeuse",
            name = "Betelgeuse (Red Supergiant in Orion)",
            distanceDisplay = "642.5 light-years",
            distanceKm = 642.5 * 9.461e12,
            lightTravelSeconds = 642.5 * 365.25 * 86400,
            lightTravelFormatted = "642.5 years",
            historicalEarthAnchor = "During the 14th century (Black Death and early European Renaissance).",
            scientificNote = "If Betelgeuse already exploded as a supernova anytime in the last 600 years, its light is currently rushing toward us and could brighten our night sky tomorrow."
        ),
        LightTimePreset(
            id = "andromeda_m31",
            name = "Andromeda Galaxy (M31)",
            distanceDisplay = "2.537 million light-years",
            distanceKm = 2.537e6 * 9.461e12,
            lightTravelSeconds = 2.537e6 * 365.25 * 86400,
            lightTravelFormatted = "2.54 million years",
            historicalEarthAnchor = "Early hominids like Australopithecus and Homo habilis were first walking East African savannas.",
            scientificNote = "This is the most distant object visible to the naked human eye in dark skies."
        ),
        LightTimePreset(
            id = "observable_universe_edge",
            name = "Edge of Observable Universe (CMB Horizon)",
            distanceDisplay = "46.5 billion light-years (co-moving)",
            distanceKm = 46.5e9 * 9.461e12,
            lightTravelSeconds = 13.8e9 * 365.25 * 86400,
            lightTravelFormatted = "13.8 billion years (Lookback Time)",
            historicalEarthAnchor = "The universe was newly born; no stars, galaxies, or rocky planets existed yet.",
            scientificNote = "The light we receive from this cosmic horizon originated as 3000 K glowing hydrogen plasma during recombination, stretched by spatial expansion into microwave frequencies."
        )
    )
}
