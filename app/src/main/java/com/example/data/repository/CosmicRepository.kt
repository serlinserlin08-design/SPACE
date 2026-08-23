package com.example.data.repository

import com.example.data.datasource.CosmicDataSources
import com.example.data.datasource.LightTimePresetsData
import com.example.data.local.dao.CosmicDao
import com.example.data.local.entity.FavoriteEntity
import com.example.data.local.entity.QuizScoreEntity
import com.example.data.model.ObjectCategory
import com.example.data.model.QuizQuestion
import com.example.data.model.SpaceFact
import com.example.data.model.SpaceObject
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class CosmicRepository(private val cosmicDao: CosmicDao) {

    val allFavorites: Flow<List<FavoriteEntity>> = cosmicDao.getAllFavorites()
    val allQuizScores: Flow<List<QuizScoreEntity>> = cosmicDao.getAllQuizScores()

    fun isFavorite(id: String): Flow<Boolean> = cosmicDao.isFavorite(id)

    suspend fun toggleFavorite(
        id: String,
        itemType: String,
        title: String,
        subtitle: String,
        category: String,
        detailText: String,
        isCurrentlyFavorite: Boolean
    ) {
        if (isCurrentlyFavorite) {
            cosmicDao.deleteFavoriteById(id)
        } else {
            cosmicDao.insertFavorite(
                FavoriteEntity(
                    id = id,
                    itemType = itemType,
                    title = title,
                    subtitle = subtitle,
                    category = category,
                    detailText = detailText
                )
            )
        }
    }

    suspend fun removeFavorite(id: String) {
        cosmicDao.deleteFavoriteById(id)
    }

    suspend fun recordQuizScore(score: Int, total: Int, difficulty: String) {
        cosmicDao.insertQuizScore(
            QuizScoreEntity(
                score = score,
                totalQuestions = total,
                difficulty = difficulty
            )
        )
    }

    fun getTodayFact(): SpaceFact {
        val calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val index = (dayOfYear - 1) % CosmicDataSources.spaceFacts.size
        return CosmicDataSources.spaceFacts[index]
    }

    fun getAllFacts(): List<SpaceFact> = CosmicDataSources.spaceFacts

    fun getSpaceObjects(category: ObjectCategory, searchQuery: String = ""): List<SpaceObject> {
        return CosmicDataSources.spaceObjects.filter { obj ->
            val matchesCategory = (category == ObjectCategory.ALL || obj.category == category)
            val matchesQuery = searchQuery.isBlank() ||
                    obj.name.contains(searchQuery, ignoreCase = true) ||
                    obj.designation.contains(searchQuery, ignoreCase = true) ||
                    obj.tags.any { it.contains(searchQuery, ignoreCase = true) }
            matchesCategory && matchesQuery
        }
    }

    fun getQuizQuestions(difficulty: String? = null): List<QuizQuestion> {
        return if (difficulty == null || difficulty == "All") {
            CosmicDataSources.quizQuestions
        } else {
            CosmicDataSources.quizQuestions.filter { it.difficulty.equals(difficulty, ignoreCase = true) }
        }
    }

    fun getLightTimePresets() = LightTimePresetsData.presets
}
