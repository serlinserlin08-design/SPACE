package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.CosmicDao
import com.example.data.local.entity.FavoriteEntity
import com.example.data.local.entity.QuizScoreEntity

@Database(
    entities = [FavoriteEntity::class, QuizScoreEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CosmicDatabase : RoomDatabase() {
    abstract fun cosmicDao(): CosmicDao

    companion object {
        @Volatile
        private var INSTANCE: CosmicDatabase? = null

        fun getDatabase(context: Context): CosmicDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CosmicDatabase::class.java,
                    "cosmic_time_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
