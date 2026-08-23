package com.example.data.datasource

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.Base64
import com.example.data.model.SpaceImageStyle
import com.example.data.model.SpaceVisualClassification
import java.io.ByteArrayOutputStream
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

object SpaceImageGenerator {

    /**
     * Checks if a user prompt is requesting an image visualization
     */
    fun isImageRequest(query: String): Boolean {
        val lower = query.lowercase().trim()
        val triggers = listOf(
            "generate", "create", "draw", "visualize", "show me", "render",
            "paint", "picture", "photo", "image", "illustrate", "depict",
            "look like", "what does", "appearance of", "view of", "show an image",
            "make an image", "see a", "see the"
        )
        return triggers.any { lower.contains(it) }
    }

    /**
     * Detects requested style or defaults to Photorealistic / Scientific
     */
    fun detectStyle(query: String): SpaceImageStyle {
        return SpaceImageStyle.fromString(query)
    }

    /**
     * Automatically constructs a rich, scientifically grounded prompt for the image generator.
     */
    fun buildOptimizedImagePrompt(subject: String, style: SpaceImageStyle): String {
        val cleanSubject = subject.trim()
        val baseDescription = when {
            cleanSubject.contains("black hole", ignoreCase = true) || cleanSubject.contains("singularity", ignoreCase = true) ->
                "A rotating supermassive Kerr black hole in deep space, showing an ultra-luminous glowing accretion disk warped by extreme gravitational lensing above and below the black event horizon shadow, asymmetric Doppler beaming making the approaching plasma side brighter, sharp circular photon sphere ring at 1.5 Schwarzschild radii, distorted cosmic background starfield"

            cleanSubject.contains("ton 618", ignoreCase = true) ->
                "Hypermassive black hole TON 618 (66 billion solar masses), hyper-luminous quasar accretion disk outshining 100 trillion suns, blinding relativistic polar plasma jets shooting millions of light years into intergalactic space, faint circular scale overlay representing the entire Solar System dwarfed inside the event horizon"

            cleanSubject.contains("neutron star", ignoreCase = true) || cleanSubject.contains("pulsar", ignoreCase = true) || cleanSubject.contains("magnetar", ignoreCase = true) ->
                "Ultra-dense neutron star pulsar, highly magnetic glowing blue-white relativistic sphere with glowing magnetic dipole flux loops, twin collimated relativistic synchrotron particle beams blasting from the magnetic poles, surrounded by an ionized pulsar wind nebula"

            cleanSubject.contains("galaxy collision", ignoreCase = true) || cleanSubject.contains("milky way and andromeda", ignoreCase = true) || cleanSubject.contains("merger", ignoreCase = true) ->
                "Gravitational merger of two majestic spiral galaxies (like Milky Way and Andromeda), massive tidal gravitational tails of billions of stars stretching across intergalactic void, glowing pink and blue starburst star-forming regions, dual supermassive black hole nuclei spiraling inward"

            cleanSubject.contains("exoplanet", ignoreCase = true) || cleanSubject.contains("rings", ignoreCase = true) || cleanSubject.contains("planet", ignoreCase = true) ->
                "A pristine exoplanet with a magnificent, vast illuminated planetary ring system casting sharp diagonal shadows across its cloudy turbulent atmosphere, illuminated by a distant binary star system with deep space nebula in background"

            cleanSubject.contains("quasar", ignoreCase = true) || cleanSubject.contains("agn", ignoreCase = true) ->
                "An ultra-luminous Quasar in the early universe, massive active galactic nucleus, blazing superheated relativistic accretion disk feeding a central black hole with high-energy relativistic plasma jets penetrating the host galaxy"

            cleanSubject.contains("supernova", ignoreCase = true) || cleanSubject.contains("nebula", ignoreCase = true) ->
                "Expanding supernova remnant nebula, intricate glowing filaments of hydrogen-alpha red and oxygen-III cyan ionized gas shockwaves, central cooling white dwarf/neutron star, illuminated cosmic dust clouds"

            cleanSubject.contains("telescope", ignoreCase = true) || cleanSubject.contains("jwst", ignoreCase = true) || cleanSubject.contains("hubble", ignoreCase = true) ->
                "James Webb Space Telescope deep space view of a massive gravitational lens cluster (Einstein ring), distant red-shifted early galaxies warped into arcs around a foreground galaxy cluster, characteristic 6-point diffraction spikes"

            cleanSubject.contains("voyager", ignoreCase = true) || cleanSubject.contains("spacecraft", ignoreCase = true) || cleanSubject.contains("probe", ignoreCase = true) ->
                "Voyager 1 interstellar spacecraft drifting in the dark interstellar medium beyond the heliopause, high-gain parabolic antenna pointing back toward the distant pinpoint Sun, golden record visible, dark cosmic background"

            else ->
                "Astrophysical visualization of $cleanSubject, scientifically accurate cosmic phenomenon, deep space environment with stars and interstellar dust"
        }

        return "$baseDescription, ${style.promptModifier}, scientifically plausible astronomical illustration, high dynamic range, masterwork astronomy composition."
    }

    /**
     * Determines proper classification badge according to scientific constraints
     */
    fun determineClassification(subject: String, style: SpaceImageStyle): SpaceVisualClassification {
        val lower = subject.lowercase()
        return when {
            style == SpaceImageStyle.TELESCOPE_STYLE -> SpaceVisualClassification.TELESCOPE_SIMULATION
            style == SpaceImageStyle.RENDER_3D -> SpaceVisualClassification.PHYSICS_3D_RENDER
            style == SpaceImageStyle.SCIENTIFIC_ILLUSTRATION -> SpaceVisualClassification.ARTIST_IMPRESSION
            lower.contains("black hole") || lower.contains("exoplanet") || lower.contains("quasar") || lower.contains("big bang") ->
                SpaceVisualClassification.AI_VISUALIZATION
            else -> SpaceVisualClassification.AI_VISUALIZATION
        }
    }

    /**
     * High-fidelity procedural astronomical bitmap generator for offline/fallback rendering.
     * Generates a 960x540 (16:9) crisp, colorful, scientifically grounded space visual.
     */
    fun generateProceduralSpaceBitmap(subject: String, style: SpaceImageStyle): Bitmap {
        val width = 960
        val height = 540
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val lower = subject.lowercase()
        val random = Random(subject.hashCode())

        // 1. Draw Deep Space Background with Starfield
        drawCosmicBackground(canvas, width, height, random, style)

        // 2. Draw Specific Celestial Phenomenon
        when {
            lower.contains("black hole") || lower.contains("ton 618") || lower.contains("singularity") || lower.contains("event horizon") -> {
                drawBlackHole(canvas, width, height, isSupermassive = lower.contains("ton 618"), renderStyle = style)
            }
            lower.contains("neutron star") || lower.contains("pulsar") || lower.contains("magnetar") -> {
                drawNeutronStarPulsar(canvas, width, height, renderStyle = style)
            }
            lower.contains("galaxy") || lower.contains("collision") || lower.contains("merger") || lower.contains("milky way") || lower.contains("andromeda") -> {
                drawGalaxyCollision(canvas, width, height, renderStyle = style)
            }
            lower.contains("quasar") || lower.contains("agn") -> {
                drawQuasar(canvas, width, height, renderStyle = style)
            }
            lower.contains("exoplanet") || lower.contains("ring") || lower.contains("planet") -> {
                drawRingedExoplanet(canvas, width, height, renderStyle = style)
            }
            lower.contains("nebula") || lower.contains("supernova") || lower.contains("stellar") -> {
                drawNebulaSupernova(canvas, width, height, renderStyle = style)
            }
            lower.contains("voyager") || lower.contains("spacecraft") || lower.contains("satellite") -> {
                drawSpacecraftInVoid(canvas, width, height, renderStyle = style)
            }
            else -> {
                drawGenericCosmicWonder(canvas, width, height, renderStyle = style)
            }
        }

        // 3. Style Specific Overlays (e.g. Telescope diffraction spikes, Technical Grid, Labeled Overlay)
        applyStyleEffects(canvas, width, height, style)

        return bitmap
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }

    private fun drawCosmicBackground(canvas: Canvas, w: Int, h: Int, random: Random, renderStyle: SpaceImageStyle) {
        val bgPaint = Paint().apply {
            shader = RadialGradient(
                w * 0.5f, h * 0.5f, w * 0.7f,
                intArrayOf(Color.rgb(15, 23, 42), Color.rgb(7, 10, 22), Color.rgb(2, 4, 10)),
                floatArrayOf(0f, 0.6f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), bgPaint)

        // Cosmic Dust Haze
        val dustPaint = Paint().apply {
            shader = RadialGradient(
                w * 0.7f, h * 0.3f, w * 0.45f,
                intArrayOf(Color.argb(45, 99, 102, 241), Color.argb(20, 168, 85, 247), Color.TRANSPARENT),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, w.toFloat(), h.toFloat(), dustPaint)

        // Starfield
        val starPaint = Paint().apply { isAntiAlias = true }
        for (i in 0 until 180) {
            val sx = random.nextFloat() * w
            val sy = random.nextFloat() * h
            val size = 0.6f + random.nextFloat() * 1.8f
            val alpha = 80 + random.nextInt(175)
            val isBlue = random.nextBoolean()
            starPaint.color = if (isBlue) Color.argb(alpha, 190, 225, 255) else Color.argb(alpha, 255, 245, 220)
            canvas.drawCircle(sx, sy, size, starPaint)
        }
    }

    private fun drawBlackHole(canvas: Canvas, w: Int, h: Int, isSupermassive: Boolean, renderStyle: SpaceImageStyle) {
        val cx = w * 0.5f
        val cy = h * 0.52f
        val shadowRadius = if (isSupermassive) 85f else 75f

        // 1. Relativistic Outer Lensing Glow
        val lensGlow = Paint().apply {
            shader = RadialGradient(
                cx, cy, shadowRadius * 3.4f,
                intArrayOf(
                    Color.argb(190, 255, 140, 0),
                    Color.argb(120, 234, 88, 12),
                    Color.argb(40, 147, 51, 234),
                    Color.TRANSPARENT
                ),
                floatArrayOf(0.2f, 0.45f, 0.75f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawCircle(cx, cy, shadowRadius * 3.4f, lensGlow)

        // 2. Gravitationally Lensed Upper & Lower Accretion Arc (Wrapped above/behind the shadow)
        val upperArcPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 32f
            shader = LinearGradient(
                cx - shadowRadius * 2.2f, cy, cx + shadowRadius * 2.2f, cy,
                intArrayOf(
                    Color.argb(255, 255, 240, 180), // Doppler boosted brighter side
                    Color.argb(230, 255, 120, 0),
                    Color.argb(140, 194, 65, 12)
                ),
                null,
                Shader.TileMode.CLAMP
            )
        }
        val upperArcRect = android.graphics.RectF(
            cx - shadowRadius * 1.85f, cy - shadowRadius * 1.6f,
            cx + shadowRadius * 1.85f, cy + shadowRadius * 0.7f
        )
        canvas.drawArc(upperArcRect, 185f, 170f, false, upperArcPaint)

        // 3. Main Horizontal Accretion Disk Ellipse
        val diskPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 26f
            shader = LinearGradient(
                cx - shadowRadius * 3.2f, cy, cx + shadowRadius * 3.2f, cy,
                intArrayOf(
                    Color.rgb(255, 250, 200), // Intense Doppler Boost
                    Color.rgb(255, 138, 0),
                    Color.rgb(220, 38, 38),
                    Color.argb(120, 126, 34, 206)
                ),
                floatArrayOf(0f, 0.35f, 0.7f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        val diskRect = android.graphics.RectF(
            cx - shadowRadius * 3.2f, cy - shadowRadius * 0.48f,
            cx + shadowRadius * 3.2f, cy + shadowRadius * 0.48f
        )
        canvas.drawOval(diskRect, diskPaint)

        // 4. Photon Sphere Ring (1.5 r_s)
        val photonSpherePaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 3.5f
            color = Color.argb(240, 255, 255, 255)
        }
        canvas.drawCircle(cx, cy, shadowRadius * 1.25f, photonSpherePaint)

        // 5. Pure Black Event Horizon Shadow (Singularity center)
        val shadowPaint = Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
        }
        canvas.drawCircle(cx, cy, shadowRadius, shadowPaint)

        // 6. If TON 618, draw relativistic polar jets & Solar System scale marker
        if (isSupermassive) {
            val jetPaint = Paint().apply {
                isAntiAlias = true
                strokeWidth = 14f
                shader = LinearGradient(
                    cx, cy - shadowRadius, cx, cy - shadowRadius * 3.8f,
                    Color.argb(240, 147, 197, 253),
                    Color.TRANSPARENT,
                    Shader.TileMode.CLAMP
                )
            }
            canvas.drawLine(cx, cy - shadowRadius, cx, 15f, jetPaint)
            val jetBottomPaint = Paint().apply {
                isAntiAlias = true
                strokeWidth = 14f
                shader = LinearGradient(
                    cx, cy + shadowRadius, cx, cy + shadowRadius * 3.8f,
                    Color.argb(240, 147, 197, 253),
                    Color.TRANSPARENT,
                    Shader.TileMode.CLAMP
                )
            }
            canvas.drawLine(cx, cy + shadowRadius, cx, h.toFloat() - 15f, jetBottomPaint)

            // Solar System Scale Comparison Ring
            val scaleRingPaint = Paint().apply {
                isAntiAlias = true
                style = Paint.Style.STROKE
                strokeWidth = 1.5f
                color = Color.argb(180, 234, 179, 8)
                pathEffect = android.graphics.DashPathEffect(floatArrayOf(8f, 6f), 0f)
            }
            canvas.drawCircle(cx, cy, shadowRadius * 0.35f, scaleRingPaint)
        }
    }

    private fun drawNeutronStarPulsar(canvas: Canvas, w: Int, h: Int, renderStyle: SpaceImageStyle) {
        val cx = w * 0.5f
        val cy = h * 0.5f
        val starRadius = 38f

        // Relativistic Polar Beams (45 degree tilt)
        val beamPaint = Paint().apply {
            isAntiAlias = true
            strokeWidth = 24f
            shader = LinearGradient(
                cx, cy, cx + 380f, cy - 220f,
                Color.argb(240, 96, 165, 250),
                Color.TRANSPARENT,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawLine(cx, cy, cx + 420f, cy - 240f, beamPaint)
        canvas.drawLine(cx, cy, cx - 420f, cy + 240f, beamPaint)

        // Magnetic Field Dipole Loops
        val magPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 2f
            color = Color.argb(120, 147, 197, 253)
        }
        val pathLeft = Path().apply {
            moveTo(cx, cy - starRadius)
            cubicTo(cx - 160f, cy - 140f, cx - 160f, cy + 140f, cx, cy + starRadius)
        }
        canvas.drawPath(pathLeft, magPaint)
        val pathRight = Path().apply {
            moveTo(cx, cy - starRadius)
            cubicTo(cx + 160f, cy - 140f, cx + 160f, cy + 140f, cx, cy + starRadius)
        }
        canvas.drawPath(pathRight, magPaint)

        // Glowing Blue Star Core
        val coreGlow = Paint().apply {
            shader = RadialGradient(
                cx, cy, starRadius * 3f,
                intArrayOf(Color.WHITE, Color.rgb(56, 189, 248), Color.rgb(37, 99, 235), Color.TRANSPARENT),
                floatArrayOf(0f, 0.35f, 0.7f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawCircle(cx, cy, starRadius * 3f, coreGlow)
        val whiteCore = Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
        }
        canvas.drawCircle(cx, cy, starRadius, whiteCore)
    }

    private fun drawGalaxyCollision(canvas: Canvas, w: Int, h: Int, renderStyle: SpaceImageStyle) {
        val cx1 = w * 0.38f
        val cy1 = h * 0.52f
        val cx2 = w * 0.64f
        val cy2 = h * 0.44f

        // Galaxy 1 Spiral Disk
        val g1Paint = Paint().apply {
            shader = RadialGradient(
                cx1, cy1, 190f,
                intArrayOf(Color.WHITE, Color.rgb(244, 114, 182), Color.rgb(99, 102, 241), Color.TRANSPARENT),
                floatArrayOf(0f, 0.3f, 0.65f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.save()
        canvas.rotate(25f, cx1, cy1)
        canvas.drawOval(android.graphics.RectF(cx1 - 210f, cy1 - 70f, cx1 + 210f, cy1 + 70f), g1Paint)
        canvas.restore()

        // Galaxy 2 Spiral Disk
        val g2Paint = Paint().apply {
            shader = RadialGradient(
                cx2, cy2, 160f,
                intArrayOf(Color.WHITE, Color.rgb(56, 189, 248), Color.rgb(147, 51, 234), Color.TRANSPARENT),
                floatArrayOf(0f, 0.35f, 0.7f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.save()
        canvas.rotate(-35f, cx2, cy2)
        canvas.drawOval(android.graphics.RectF(cx2 - 170f, cy2 - 60f, cx2 + 170f, cy2 + 60f), g2Paint)
        canvas.restore()

        // Interacting Tidal Bridge / Starburst Trail
        val bridgePaint = Paint().apply {
            isAntiAlias = true
            strokeWidth = 18f
            shader = LinearGradient(
                cx1, cy1, cx2, cy2,
                Color.argb(190, 244, 114, 182),
                Color.argb(190, 56, 189, 248),
                Shader.TileMode.CLAMP
            )
        }
        val bridgePath = Path().apply {
            moveTo(cx1 + 50f, cy1 - 30f)
            quadTo(w * 0.5f, h * 0.28f, cx2 - 40f, cy2 + 20f)
        }
        canvas.drawPath(bridgePath, bridgePaint)
    }

    private fun drawQuasar(canvas: Canvas, w: Int, h: Int, renderStyle: SpaceImageStyle) {
        val cx = w * 0.5f
        val cy = h * 0.54f

        // High Energy Polar Jet
        val jetPaint = Paint().apply {
            isAntiAlias = true
            strokeWidth = 36f
            shader = LinearGradient(
                cx, cy, cx, 10f,
                intArrayOf(Color.WHITE, Color.rgb(96, 165, 250), Color.argb(0, 59, 130, 246)),
                floatArrayOf(0f, 0.4f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawLine(cx, cy, cx, 10f, jetPaint)
        canvas.drawLine(cx, cy, cx, h.toFloat() - 10f, jetPaint)

        // Blazing Accretion Torus
        val torusPaint = Paint().apply {
            shader = RadialGradient(
                cx, cy, 240f,
                intArrayOf(Color.WHITE, Color.rgb(251, 146, 60), Color.rgb(220, 38, 38), Color.TRANSPARENT),
                floatArrayOf(0f, 0.25f, 0.6f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawOval(android.graphics.RectF(cx - 280f, cy - 65f, cx + 280f, cy + 65f), torusPaint)

        // Core Black Hole
        val bh = Paint().apply { color = Color.BLACK; isAntiAlias = true }
        canvas.drawCircle(cx, cy, 32f, bh)
    }

    private fun drawRingedExoplanet(canvas: Canvas, w: Int, h: Int, renderStyle: SpaceImageStyle) {
        val cx = w * 0.45f
        val cy = h * 0.52f
        val planetRadius = 110f

        // Back Ring Segment
        val backRingPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 32f
            shader = LinearGradient(
                cx - 320f, cy, cx + 320f, cy,
                Color.argb(160, 226, 232, 240),
                Color.argb(90, 148, 163, 184),
                Shader.TileMode.CLAMP
            )
        }
        val ringRect = android.graphics.RectF(cx - 300f, cy - 70f, cx + 300f, cy + 70f)
        canvas.save()
        canvas.rotate(-18f, cx, cy)
        canvas.drawArc(ringRect, 180f, 180f, false, backRingPaint)
        canvas.restore()

        // Planet Body with Atmospheric Gradient
        val planetPaint = Paint().apply {
            shader = RadialGradient(
                cx - 40f, cy - 35f, planetRadius * 1.25f,
                intArrayOf(Color.rgb(186, 230, 253), Color.rgb(14, 116, 144), Color.rgb(3, 7, 18)),
                floatArrayOf(0f, 0.55f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawCircle(cx, cy, planetRadius, planetPaint)

        // Front Ring Segment (over planet)
        val frontRingPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 32f
            shader = LinearGradient(
                cx - 320f, cy, cx + 320f, cy,
                Color.argb(220, 241, 245, 249),
                Color.argb(140, 203, 213, 225),
                Shader.TileMode.CLAMP
            )
        }
        canvas.save()
        canvas.rotate(-18f, cx, cy)
        canvas.drawArc(ringRect, 0f, 180f, false, frontRingPaint)
        canvas.restore()
    }

    private fun drawNebulaSupernova(canvas: Canvas, w: Int, h: Int, renderStyle: SpaceImageStyle) {
        val cx = w * 0.5f
        val cy = h * 0.5f

        // Outer Shockwave
        val shock = Paint().apply {
            shader = RadialGradient(
                cx, cy, 230f,
                intArrayOf(Color.argb(180, 239, 68, 68), Color.argb(140, 168, 85, 247), Color.TRANSPARENT),
                floatArrayOf(0.3f, 0.7f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawCircle(cx, cy, 230f, shock)

        // Filamentary Gas Web
        val innerGlow = Paint().apply {
            shader = RadialGradient(
                cx, cy, 120f,
                intArrayOf(Color.WHITE, Color.rgb(56, 189, 248), Color.TRANSPARENT),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawCircle(cx, cy, 120f, innerGlow)
    }

    private fun drawSpacecraftInVoid(canvas: Canvas, w: Int, h: Int, renderStyle: SpaceImageStyle) {
        val cx = w * 0.5f
        val cy = h * 0.5f

        // Golden Record / Dish
        val dishPaint = Paint().apply {
            isAntiAlias = true
            color = Color.rgb(234, 179, 8)
        }
        canvas.drawCircle(cx, cy, 55f, dishPaint)

        val dishInner = Paint().apply {
            isAntiAlias = true
            color = Color.rgb(202, 138, 4)
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }
        canvas.drawCircle(cx, cy, 38f, dishInner)

        // Boom Arms
        val boomPaint = Paint().apply {
            isAntiAlias = true
            strokeWidth = 5f
            color = Color.rgb(226, 232, 240)
        }
        canvas.drawLine(cx, cy, cx - 180f, cy - 90f, boomPaint)
        canvas.drawLine(cx, cy, cx + 160f, cy + 110f, boomPaint)
        canvas.drawLine(cx, cy, cx + 190f, cy - 70f, boomPaint)
    }

    private fun drawGenericCosmicWonder(canvas: Canvas, w: Int, h: Int, renderStyle: SpaceImageStyle) {
        val cx = w * 0.5f
        val cy = h * 0.5f

        val cosmic = Paint().apply {
            shader = RadialGradient(
                cx, cy, 210f,
                intArrayOf(Color.WHITE, Color.rgb(168, 85, 247), Color.rgb(30, 58, 138), Color.TRANSPARENT),
                floatArrayOf(0f, 0.4f, 0.75f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawCircle(cx, cy, 210f, cosmic)
    }

    private fun applyStyleEffects(canvas: Canvas, w: Int, h: Int, renderStyle: SpaceImageStyle) {
        when (renderStyle) {
            SpaceImageStyle.TELESCOPE_STYLE -> {
                // Characteristic JWST 6-Point Diffraction Spikes on center
                val spikePaint = Paint().apply {
                    isAntiAlias = true
                    strokeWidth = 1.5f
                    color = Color.argb(190, 255, 255, 255)
                }
                val cx = w * 0.5f
                val cy = h * 0.5f
                for (deg in listOf(0f, 60f, 120f)) {
                    val rad = Math.toRadians(deg.toDouble())
                    val dx = (cos(rad) * 350).toFloat()
                    val dy = (sin(rad) * 350).toFloat()
                    canvas.drawLine(cx - dx, cy - dy, cx + dx, cy + dy, spikePaint)
                }
            }
            SpaceImageStyle.SCIENTIFIC_ILLUSTRATION -> {
                // Subtle Grid Coordinate reticle overlay
                val gridPaint = Paint().apply {
                    isAntiAlias = true
                    style = Paint.Style.STROKE
                    strokeWidth = 0.8f
                    color = Color.argb(40, 56, 189, 248)
                }
                canvas.drawCircle(w * 0.5f, h * 0.5f, 180f, gridPaint)
                canvas.drawCircle(w * 0.5f, h * 0.5f, 260f, gridPaint)
            }
            SpaceImageStyle.CINEMATIC -> {
                // Anamorphic horizontal flare
                val flarePaint = Paint().apply {
                    isAntiAlias = true
                    strokeWidth = 2.5f
                    shader = LinearGradient(
                        0f, h * 0.5f, w.toFloat(), h * 0.5f,
                        intArrayOf(Color.TRANSPARENT, Color.argb(160, 56, 189, 248), Color.WHITE, Color.argb(160, 56, 189, 248), Color.TRANSPARENT),
                        floatArrayOf(0f, 0.45f, 0.5f, 0.55f, 1f),
                        Shader.TileMode.CLAMP
                    )
                }
                canvas.drawLine(0f, h * 0.5f, w.toFloat(), h * 0.5f, flarePaint)
            }
            else -> {}
        }
    }
}
