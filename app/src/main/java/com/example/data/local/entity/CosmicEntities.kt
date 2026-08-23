package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val itemType: String, // "FACT" or "OBJECT"
    val title: String,
    val subtitle: String,
    val category: String,
    val detailText: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "quiz_scores")
data class QuizScoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val score: Int,
    val totalQuestions: Int,
    val difficulty: String,
    val timestamp: Long = System.currentTimeMillis()
)
