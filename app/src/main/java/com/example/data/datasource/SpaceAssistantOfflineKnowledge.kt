package com.example.data.datasource

import com.example.data.model.AssistantMessage
import com.example.data.model.EpistemicStatus
import com.example.data.model.TelemetryHighlight
import com.example.data.model.VisualWidgetType

object SpaceAssistantOfflineKnowledge {

    val starterPrompts = listOf(
        "What does a neutron star look like inside?",
        "How does a black hole work?",
        "What is TON 618 and how huge is it?",
        "How does gravitational time dilation work?",
        "How does a galaxy merge?",
        "How far is Voyager 1 right now?",
        "What will happen when our Sun dies?",
        "How fast does light travel across the solar system?",
        "Could we ever build a warp drive?",
        "What is dark matter vs dark energy?"
    )

    fun findKnowledge(query: String): AssistantMessage? {
        val q = query.lowercase().trim()

        if (q.contains("neutron star") || (q.contains("neutron") && q.contains("look"))) {
            return AssistantMessage(
                isUser = false,
                text = "",
                directAnswer = "A neutron star is the collapsed ultra-dense core of a massive giant star, packed into a sphere only ~20 km (12 miles) wide with the mass of 1.4 to 2.1 Suns.",
                deepExplanation = "• **Outer Crust**: Solid iron-56 crystal lattice enveloped by relativistic electron gas.\n• **Inner Crust & 'Nuclear Pasta'**: Incredible pressure squeezes protons and electrons into neutrons, morphing into strange shapes (lasagna, spaghetti, gnocchi phases).\n• **Core**: Superfluid neutrons and superconducting protons with densities exceeding 4 × 10¹⁴ g/cm³.\n• **Magnetic Field**: Up to a trillion Gauss, producing high-energy relativistic beams from its magnetic poles (observed as pulsars as it spins hundreds of times per second).",
                epistemicStatus = EpistemicStatus.CONFIRMED_OBSERVATION,
                visualType = VisualWidgetType.NEUTRON_STAR_STRUCTURE,
                visualTitle = "Neutron Star Cross-Section & Pulsar Jet",
                visualCaption = "Cross-section schematic showing outer crystal crust, nuclear pasta transition layer, superfluid core, and relativistic magnetic polar jets.",
                visualSourceType = "Scientific Schematic (NASA / NICER Mission)",
                telemetryHighlights = listOf(
                    TelemetryHighlight("Diameter", "~20 km", "Manhattan Island scale"),
                    TelemetryHighlight("Mass", "1.4 – 2.1 M☉", "1.4x–2.1x our Sun"),
                    TelemetryHighlight("Surface Gravity", "2 × 10¹¹ g", "200 billion × Earth"),
                    TelemetryHighlight("Rotation Period", "1.4 ms – 11 s", "Up to 716 rev/sec")
                ),
                distanceIntuition = "A single teaspoon of neutron star matter has a mass of ~1 billion tons (equivalent to Mount Everest).",
                sourcesCited = listOf("NASA NICER Mission", "Chandra X-Ray Center", "Nature Astronomy 2021"),
                followUpQuestions = listOf(
                    "What is a magnetar vs pulsar?",
                    "What happens when two neutron stars collide?",
                    "Could a neutron star turn into a black hole?"
                )
            )
        }

        if (q.contains("ton 618") || q.contains("ton618") || q.contains("biggest black hole") || q.contains("largest black hole")) {
            return AssistantMessage(
                isUser = false,
                text = "",
                directAnswer = "TON 618 is an ultra-luminous, hyper-massive quasar powered by one of the largest confirmed supermassive black holes in the universe, weighing a staggering 66 billion solar masses.",
                deepExplanation = "• **Scale & Horizon**: The Schwarzschild event horizon has a diameter of roughly 390 billion kilometers (2,600 AU)—more than 40 times the size of Neptune's entire orbit around the Sun!\n• **Accretion Disk**: Gaseous matter orbits at 7,000 km/s, shining with the luminosity of 140 trillion Suns.\n• **Cosmic Distance**: Located 18.2 billion light-years (co-moving distance) in the constellation Canes Venatici, observed as it was only 3.4 billion years after the Big Bang.",
                epistemicStatus = EpistemicStatus.CONFIRMED_OBSERVATION,
                visualType = VisualWidgetType.TON_618_SUPERMASSIVE,
                visualTitle = "TON 618 Event Horizon vs Solar System",
                visualCaption = "Scale comparison demonstrating that our entire Solar System out to the Kuiper Belt would easily fit inside TON 618's event horizon.",
                visualSourceType = "Hydrodynamic Model & Astrophysical Scale Diagram",
                telemetryHighlights = listOf(
                    TelemetryHighlight("Mass", "66,000,000,000 M☉", "66 Billion Suns"),
                    TelemetryHighlight("Event Horizon Dia.", "390 Billion km", "~2,600 AU"),
                    TelemetryHighlight("Luminosity", "4 × 10⁴⁰ W", "140 Trillion Suns"),
                    TelemetryHighlight("Light Travel Lookback", "10.4 Billion yrs", "Early Cosmic Epoch")
                ),
                distanceIntuition = "A beam of light traveling at 300,000 km/s takes over 15 days just to cross the diameter of TON 618's event horizon!",
                sourcesCited = listOf("Sloan Digital Sky Survey (SDSS)", "Astrophysical Journal (Shemmer et al.)"),
                followUpQuestions = listOf(
                    "How could TON 618 grow so large so early in the universe?",
                    "What is Phoenix A black hole?",
                    "What happens if Earth fell toward TON 618?"
                )
            )
        }

        if (q.contains("black hole") && (q.contains("work") || q.contains("what is") || q.contains("horizon") || q.contains("singularity") || q.contains("inside"))) {
            return AssistantMessage(
                isUser = false,
                text = "",
                directAnswer = "A black hole is a region of spacetime where mass is concentrated into such extreme density that the gravitational curvature prevents anything—even light (c = 299,792 km/s)—from escaping.",
                deepExplanation = "• **Event Horizon (r_s = 2GM/c²)**: The mathematical boundary of no return. Once crossed, all possible future spacetime geodesics point inward toward the singularity.\n• **Photon Sphere (r = 1.5 r_s)**: The unstable orbit where photons can orbit the black hole in circles.\n• **Accretion Disk & Relativistic Beaming**: Infalling plasma is heated by viscous friction to tens of millions of degrees Kelvin, radiating intense X-rays.\n• **Gravitational Lensing**: Spacetime curvature bends light around the back of the black hole, making the accretion disk appear wrapped above and below the shadow.",
                epistemicStatus = EpistemicStatus.CONFIRMED_OBSERVATION,
                visualType = VisualWidgetType.EVENT_HORIZON,
                visualTitle = "General Relativity Black Hole Event Horizon",
                visualCaption = "Interactive simulation showing the Schwarzschild shadow, photon sphere at 1.5 r_s, Doppler boosted accretion disc, and gravitational light bending.",
                visualSourceType = "General Relativistic Ray-Tracing Simulation (EHT / NASA)",
                telemetryHighlights = listOf(
                    TelemetryHighlight("Escape Velocity", "≥ c (300,000 km/s)", "At & inside Event Horizon"),
                    TelemetryHighlight("Formula", "r_s = 2GM / c²", "Schwarzschild Radius"),
                    TelemetryHighlight("Photon Sphere", "1.5 × r_s", "Unstable Light Orbit"),
                    TelemetryHighlight("Hawking Radiation", "10⁻⁸ K to 10⁻¹⁸ K", "Quantum Vacuum Decay")
                ),
                distanceIntuition = "If our Sun were compressed into a black hole, its event horizon radius would be only 2.95 km (1.83 miles).",
                sourcesCited = listOf("Event Horizon Telescope (EHT) Collaboration", "Albert Einstein General Theory of Relativity (1915)", "Kip Thorne & Roger Penrose Research"),
                followUpQuestions = listOf(
                    "What is Hawking radiation?",
                    "What is spaghettification?",
                    "Can black holes ever merge?"
                )
            )
        }

        if (q.contains("time dilation") || q.contains("dilation") || q.contains("relativity") || q.contains("gravity slow time") || q.contains("speed of light time")) {
            return AssistantMessage(
                isUser = false,
                text = "",
                directAnswer = "Time dilation is a verified physical phenomenon where time passes at different rates for observers moving at different velocities (Special Relativity) or located in different gravitational fields (General Relativity).",
                deepExplanation = "• **Gravitational Dilation**: Clocks closer to a massive gravitational body run slower relative to clocks farther away: t_f = t_0 * √(1 - 2GM / (r * c²)).\n• **Velocity Dilation**: As an object approaches the speed of light c, its elapsed time dilates by the Lorentz factor: γ = 1 / √(1 - v²/c²).\n• **Real-World GPS Proof**: GPS satellites orbit Earth at 14,000 km/h (losing 7 µs/day due to speed) but in lower gravity (gaining 45 µs/day due to gravity). Engineers must compensate for a net +38 µs/day error!",
                epistemicStatus = EpistemicStatus.CONFIRMED_OBSERVATION,
                visualType = VisualWidgetType.GRAVITATIONAL_TIME_DILATION,
                visualTitle = "Spacetime Warp & Gravitational Time Dilation",
                visualCaption = "Interactive spacetime curvature grid demonstrating clock lag for an observer near a gravitational mass versus an observer in flat deep space.",
                visualSourceType = "Spacetime Metric Ray-Tracer (Einstein Field Equations)",
                telemetryHighlights = listOf(
                    TelemetryHighlight("Earth Grav. Lag", "+38 µs / day", "GPS Satellite Correction"),
                    TelemetryHighlight("At Event Horizon", "Dilation Factor → ∞", "Time freezes for outside observer"),
                    TelemetryHighlight("At 0.90c Speed", "γ = 2.294", "Clocks tick 2.29x slower"),
                    TelemetryHighlight("At 0.9999c Speed", "γ = 70.7", "1 hour aboard = ~3 days on Earth")
                ),
                distanceIntuition = "For a traveler spending 1 hour near the event horizon of a supermassive black hole, decades could elapse for people back on Earth.",
                sourcesCited = listOf("Pound-Rebka Experiment (Harvard 1959)", "Hafele-Keating Atomic Clock Experiment (1971)", "NIST Optical Atomic Clocks (2010)"),
                followUpQuestions = listOf(
                    "What is the Twin Paradox?",
                    "Why can nothing travel faster than light?",
                    "How does time dilation affect GPS systems?"
                )
            )
        }

        if (q.contains("galaxy merge") || q.contains("galaxies collide") || q.contains("andromeda") && q.contains("milky way") || q.contains("collision")) {
            return AssistantMessage(
                isUser = false,
                text = "",
                directAnswer = "In approximately 4.5 billion years, our Milky Way and the Andromeda Galaxy (M31) will undergo a gravitational collision and merger, eventually combining into a giant elliptical galaxy dubbed 'Milkdromeda'.",
                deepExplanation = "• **No Star Collisions**: Because interstellar space is vast (average stellar distance is ~4 light-years), the probability of two individual stars directly colliding is virtually zero.\n• **Tidal Tails & Gas Compression**: Mutual gravitational tidal forces will fling long streams of stars into galactic tails while interstellar gas clouds collide, sparking a major burst of star formation (starburst).\n• **Supermassive Black Hole Coalescence**: The central supermassive black holes (Sagittarius A* and Andromeda's SMBH) will orbit, emit gravitational waves, and coalesce into a single titan.",
                epistemicStatus = EpistemicStatus.COMPUTATIONAL_SIMULATION,
                visualType = VisualWidgetType.GALAXY_COLLISION,
                visualTitle = "Milky Way & Andromeda Merger Simulation",
                visualCaption = "Supercomputer N-body hydrodynamical simulation displaying tidal gravitational distortion, orbital pass-through, and final relaxation into an elliptical core.",
                visualSourceType = "Supercomputer N-Body Simulation (NASA / STScI / ESA)",
                telemetryHighlights = listOf(
                    TelemetryHighlight("Collision Time", "~4.5 Billion Years", "Future epoch"),
                    TelemetryHighlight("Andromeda Velocity", "110 km/s", "Approaching Milky Way"),
                    TelemetryHighlight("Current Distance", "2.537 Million ly", "Co-moving distance"),
                    TelemetryHighlight("Final Result", "Giant Elliptical", "'Milkdromeda'")
                ),
                distanceIntuition = "Andromeda is currently rushing toward us at 400,000 km/h (250,000 mph)—fast enough to travel from Earth to the Moon in one hour!",
                sourcesCited = listOf("Hubble Space Telescope Astrometry (van der Marel et al.)", "Gaia Mission DR3 Data (ESA)", "Astrophysical Journal (2012, 2021)"),
                followUpQuestions = listOf(
                    "What will Earth's night sky look like during the merger?",
                    "What will happen to our Sun and solar system?",
                    "Do supermassive black holes merge during galaxy collisions?"
                )
            )
        }

        if (q.contains("voyager") || q.contains("voyager 1") || q.contains("spacecraft distance") || q.contains("farthest spacecraft")) {
            return AssistantMessage(
                isUser = false,
                text = "",
                directAnswer = "Voyager 1 is humanity's most distant artificial spacecraft, currently traveling through interstellar space at a distance of over 24 billion kilometers (160 AU / 15 billion miles) from Earth.",
                deepExplanation = "• **Interstellar Space Transition**: In August 2012, Voyager 1 crossed the heliopause—the boundary where the Sun's solar wind stops and interstellar plasma begins.\n• **Radio Signal Delay**: A radio transmission traveling at the speed of light takes over 22.5 hours one-way to reach Earth from Voyager 1 (over 45 hours for a round-trip command).\n• **Power Source**: Powered by Radioisotope Thermoelectric Generators (RTGs) using decaying Plutonium-238, which are expected to power instruments until approximately 2025–2030.\n• **Interstellar Future**: In about 40,000 years, Voyager 1 will pass within 1.6 light-years of the red dwarf star Gliese 445.",
                epistemicStatus = EpistemicStatus.CONFIRMED_OBSERVATION,
                visualType = VisualWidgetType.VOYAGER_DISTANCE,
                visualTitle = "Voyager 1 Interstellar Telemetry & Path",
                visualCaption = "Live astronomical telemetry tracker mapping Voyager 1's position beyond the heliopause relative to Pluto and the Oort cloud.",
                visualSourceType = "NASA Jet Propulsion Laboratory (JPL) Deep Space Network",
                telemetryHighlights = listOf(
                    TelemetryHighlight("Distance", "> 24.3 Billion km", "~162.5 AU"),
                    TelemetryHighlight("Speed", "~17 km/s", "~38,000 mph relative to Sun"),
                    TelemetryHighlight("Light-Travel Delay", "> 22.5 Hours", "One-way radio time"),
                    TelemetryHighlight("Launch Date", "Sept 5, 1977", "Over 48 years in space")
                ),
                distanceIntuition = "Voyager 1 is so far away that sunlight takes over 22 hours to reach it, appearing only as an intensely bright point of light without disk size.",
                sourcesCited = listOf("NASA JPL Voyager Mission", "NASA Deep Space Network (DSN) Live Status", "Science Magazine (2013)"),
                followUpQuestions = listOf(
                    "What is on the Voyager Golden Record?",
                    "Where is Voyager 2 right now?",
                    "When will Voyager reach the Oort Cloud?"
                )
            )
        }

        if (q.contains("sun die") || q.contains("death of sun") || q.contains("stellar evolution") || q.contains("star life") || q.contains("supernova")) {
            return AssistantMessage(
                isUser = false,
                text = "",
                directAnswer = "In about 5 billion years, our Sun will exhaust its core hydrogen fuel, expand into a Red Giant star swallowing Mercury, Venus, and likely Earth, before shedding its outer layers into a planetary nebula and leaving behind a dense White Dwarf.",
                deepExplanation = "• **Main Sequence (Present)**: The Sun fuses ~600 million tons of hydrogen into helium every second in its core (hydrostatic equilibrium).\n• **Red Giant Phase (~5B yrs)**: Core contracts, heating the shell and expanding the Sun's outer atmosphere to >200x its current diameter ($1\\text{ AU}$).\n• **Planetary Nebula & White Dwarf**: The helium flash fuses helium into carbon-oxygen. Outer gaseous envelopes are ejected, leaving an Earth-sized carbon-oxygen white dwarf with 50% of the Sun's initial mass that slowly cools over trillions of years into a black dwarf.",
                epistemicStatus = EpistemicStatus.CONFIRMED_OBSERVATION,
                visualType = VisualWidgetType.STELLAR_EVOLUTION,
                visualTitle = "Stellar Evolution Lifecycle of a G-Type Star",
                visualCaption = "Interactive multi-phase stellar timeline tracing Nebular Collapse, Main Sequence, Red Giant Expansion, Planetary Nebula, and White Dwarf cooling.",
                visualSourceType = "Standard Stellar Astrophysics Model (Hertzsprung-Russell Diagram)",
                telemetryHighlights = listOf(
                    TelemetryHighlight("Current Age", "4.6 Billion Years", "Mid-life main sequence"),
                    TelemetryHighlight("Remaining Lifetime", "~5 Billion Years", "Before Red Giant phase"),
                    TelemetryHighlight("Max Red Giant Radius", "~1.0 – 1.2 AU", "Reaching Earth's orbit"),
                    TelemetryHighlight("Final Remnant", "White Dwarf", "Earth-sized, ~0.6 M☉")
                ),
                distanceIntuition = "The Sun has completed about 20 orbits around the center of the Milky Way galaxy since its birth (one cosmic year is ~230 million Earth years).",
                sourcesCited = listOf("NASA Solar Dynamics Observatory (SDO)", "ESA Gaia Mission Stellar Surveys", "Annual Review of Astronomy and Astrophysics"),
                followUpQuestions = listOf(
                    "Will the Sun explode in a supernova?",
                    "What is the Chandrasekhar limit?",
                    "What will happen to life on Earth as the Sun heats up?"
                )
            )
        }

        if (q.contains("light travel") || q.contains("speed of light") || q.contains("how fast light") || q.contains("light year")) {
            return AssistantMessage(
                isUser = false,
                text = "",
                directAnswer = "Light travels in a vacuum at exactly c = 299,792,458 meters per second (approximately 300,000 km/s or 186,282 miles/s). Because space is immense, light still takes measurable time to travel across celestial distances.",
                deepExplanation = "• **Earth to Moon**: 1.28 light-seconds (~384,400 km)\n• **Earth to Sun**: 8.3 light-minutes (1 Astronomical Unit / ~149.6 million km)\n• **Earth to Mars**: 3.1 to 22.2 light-minutes (depending on orbital alignment)\n• **Nearest Star (Proxima Centauri)**: 4.246 light-years (~40 trillion km)\n• **Nearest Major Galaxy (Andromeda)**: 2.537 million light-years\n• **Observable Universe Edge**: 46.5 billion light-years (due to metric cosmic expansion).",
                epistemicStatus = EpistemicStatus.CONFIRMED_OBSERVATION,
                visualType = VisualWidgetType.LIGHT_TRAVEL_CALCULATOR,
                visualTitle = "Cosmic Light-Travel Time & Distance Matrix",
                visualCaption = "Interactive photon propagation diagram displaying lookback time and light-travel delays across the Solar System, Milky Way, and Local Group.",
                visualSourceType = "Astrophysical Constants (CODATA / IAU)",
                telemetryHighlights = listOf(
                    TelemetryHighlight("Light Speed (c)", "299,792 km/s", "Universal Speed Limit"),
                    TelemetryHighlight("1 Light-Year", "9.46 Trillion km", "63,241 AU"),
                    TelemetryHighlight("Moon Lookback", "1.28 Seconds", "What we see is 1.28s in past"),
                    TelemetryHighlight("Sun Lookback", "8 min 20 sec", "What we see is 8m20s in past")
                ),
                distanceIntuition = "When you look at the stars tonight, you are not seeing the universe as it is now; you are looking directly into the deep archaeological past.",
                sourcesCited = listOf("International Astronomical Union (IAU)", "NIST Physical Measurement Laboratory", "NASA Goddard Space Flight Center"),
                followUpQuestions = listOf(
                    "Can anything in quantum mechanics travel faster than light?",
                    "Why does looking far away mean looking back in time?",
                    "How large is the observable universe?"
                )
            )
        }

        if (q.contains("warp drive") || q.contains("alcubierre") || q.contains("faster than light") || q.contains("wormhole")) {
            return AssistantMessage(
                isUser = false,
                text = "",
                directAnswer = "A warp drive is a theoretical metric engineering concept (most famously the Alcubierre Metric from 1994) where spacetime itself is contracted in front of a ship and expanded behind it, allowing effective faster-than-light travel without violating local Special Relativity.",
                deepExplanation = "• **General Relativity Compliance**: The spacecraft remains stationary inside a local 'warp bubble' of flat spacetime; spacetime itself moves, bypassing the local speed-of-light barrier.\n• **Major Physical Hurdles**: Original Alcubierre models require negative mass/energy density (exotic matter), which has not been proven to exist in macroscopic quantities.\n• **Recent 2021–2024 Research**: Physicists (e.g. Lentz, Bobrick, Martire) have proposed positive-energy soliton solutions, though they require colossal amounts of mass-energy (comparable to the mass of Jupiter) and face horizons of causal disconnection.",
                epistemicStatus = EpistemicStatus.THEORETICAL_PHYSICS,
                visualType = VisualWidgetType.GRAVITATIONAL_TIME_DILATION,
                visualTitle = "Alcubierre Spacetime Metric & Warp Bubble",
                visualCaption = "Mathematical visualization of spacetime compression in front of the craft and spacetime expansion behind the flat-space interior bubble.",
                visualSourceType = "Theoretical Spacetime Metric (Alcubierre 1994 / Classical and Quantum Gravity)",
                telemetryHighlights = listOf(
                    TelemetryHighlight("Status", "Purely Theoretical", "No physical implementation"),
                    TelemetryHighlight("Governing Theory", "General Relativity", "Metric engineering"),
                    TelemetryHighlight("Energy Requirement", "Jupiter-mass equivalent", "Even in positive-energy models"),
                    TelemetryHighlight("Local Acceleration", "0 g", "Free-fall inside bubble")
                ),
                distanceIntuition = "While science fiction depicts warp drives as common engine technology, in current physics it remains an unproven mathematical solution to Einstein's equations.",
                sourcesCited = listOf("Miguel Alcubierre (Classical and Quantum Gravity, 1994)", "Erik Lentz (Classical and Quantum Gravity, 2021)", "Applied Physics Research Group (2024)"),
                followUpQuestions = listOf(
                    "What is exotic matter and negative energy?",
                    "What is an Einstein-Rosen bridge (wormhole)?",
                    "Why is causality threatened by faster-than-light travel?"
                )
            )
        }

        if (q.contains("dark matter") || q.contains("dark energy") || q.contains("universe made of")) {
            return AssistantMessage(
                isUser = false,
                text = "",
                directAnswer = "Ordinary matter (atoms, stars, planets, and humans) makes up only ~5% of the universe. The remaining ~95% consists of Dark Matter (~27%) and Dark Energy (~68%).",
                deepExplanation = "• **Dark Matter (~27%)**: Non-baryonic matter that does not absorb, reflect, or emit light. We detect it solely through its gravitational influence on galactic rotation curves (Vera Rubin), gravitational lensing, and cosmic microwave background fluctuations.\n• **Dark Energy (~68%)**: A uniform energy density permeating empty space that exerts negative pressure, causing the expansion of the universe to accelerate (discovered in 1998 via Type Ia supernovae).\n• **Ordinary Matter (~5%)**: All elements on the periodic table, every star in every galaxy, all gas clouds, and every biological organism.",
                epistemicStatus = EpistemicStatus.CONFIRMED_OBSERVATION,
                visualType = VisualWidgetType.GALAXY_SCALE_COMPARISON,
                visualTitle = "Cosmic Energy Budget of the Universe",
                visualCaption = "Astrophysical census of mass-energy distribution derived from Planck Observatory satellite measurements of the Cosmic Microwave Background.",
                visualSourceType = "ESA Planck Satellite Cosmological Data (2018)",
                telemetryHighlights = listOf(
                    TelemetryHighlight("Dark Energy", "68.3%", "Accelerates cosmic expansion"),
                    TelemetryHighlight("Dark Matter", "26.8%", "Galactic gravitational scaffolding"),
                    TelemetryHighlight("Ordinary Matter", "4.9%", "All visible matter & stars"),
                    TelemetryHighlight("Hubble Constant", "67.4 – 73.0 km/s/Mpc", "Hubble tension under study")
                ),
                distanceIntuition = "There is roughly 5.5 times more dark matter by mass in the universe than all the stars, planets, and atoms combined.",
                sourcesCited = listOf("ESA Planck Observatory (2018 Final Release)", "James Webb Space Telescope (JWST)", "Vera C. Rubin Observatory Studies"),
                followUpQuestions = listOf(
                    "What are the leading candidate particles for dark matter?",
                    "What is the Hubble Tension?",
                    "How will dark energy affect the ultimate fate of the universe?"
                )
            )
        }

        return null
    }
}
