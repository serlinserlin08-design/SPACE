package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.datasource.CosmicDataSources
import com.example.data.datasource.LightTimePresetsData
import com.example.data.model.DistanceUnit
import com.example.data.model.ObjectCategory
import com.example.data.model.RelativityEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `verify app name resource`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Cosmic Time", appName)
    }

    @Test
    fun `verify space facts catalog has content`() {
        val facts = CosmicDataSources.spaceFacts
        assertTrue("Facts list should not be empty", facts.isNotEmpty())
        facts.forEach { fact ->
            assertTrue("Fact title should not be blank", fact.title.isNotBlank())
            assertTrue("Fact summary should not be blank", fact.summary.isNotBlank())
            assertNotNull(fact.certainty)
        }
    }

    @Test
    fun `verify space objects catalog categories`() {
        val objects = CosmicDataSources.spaceObjects
        assertTrue(objects.any { it.category == ObjectCategory.BLACK_HOLE })
        assertTrue(objects.any { it.category == ObjectCategory.NEUTRON_STAR_PULSAR })
        assertTrue(objects.any { it.category == ObjectCategory.GALAXY })
        assertTrue(objects.any { it.category == ObjectCategory.EXOPLANET })
    }

    @Test
    fun `verify special relativity velocity lorentz factor`() {
        // At v = 0, gamma = 1
        val gamma0 = RelativityEngine.computeVelocityLorentzFactor(0.0)
        assertEquals(1.0, gamma0, 0.0001)

        // At v = 0.866c, gamma ~ 2.0
        val gammaHalf = RelativityEngine.computeVelocityLorentzFactor(0.866025)
        assertEquals(2.0, gammaHalf, 0.01)
    }

    @Test
    fun `verify light time calculation for 1 AU`() {
        val seconds = RelativityEngine.computeLightTravelSecondsFromUnit(1.0, DistanceUnit.AU)
        // 1 AU light travel is approx 499 seconds (~8.3 minutes)
        assertEquals(499.0, seconds, 1.0)
    }

    @Test
    fun `verify quiz questions integrity`() {
        val questions = CosmicDataSources.quizQuestions
        assertTrue(questions.size >= 10)
        questions.forEach { q ->
            assertTrue(q.options.size >= 4)
            assertTrue(q.correctIndex in 0 until q.options.size)
            assertTrue(q.scientificExplanation.isNotBlank())
        }
    }

    @Test
    fun `verify space 3D model generator resolution for known and dynamic objects`() {
        val earthModel = com.example.data.datasource.Space3DModelGenerator.getModelForQuery("Earth")
        assertEquals("Earth", earthModel.name)
        assertTrue(earthModel.isConfirmedObservationalData)

        val ton618Model = com.example.data.datasource.Space3DModelGenerator.getModelForQuery("TON 618")
        assertEquals("TON 618", ton618Model.name)
        assertTrue(ton618Model.hasAccretionDisk)
        assertTrue(ton618Model.hasRelativisticJets)

        val dynamicModel = com.example.data.datasource.Space3DModelGenerator.getModelForQuery("Kepler-452b")
        assertNotNull(dynamicModel)
        assertTrue(dynamicModel.name.contains("Kepler-452b"))
    }
}
