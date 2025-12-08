package com.example.ecotrack.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ActivityEntity::class, ChallengeEntity::class, RewardEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // activity dao
    abstract fun activityDao(): ActivityDao
    // challenge dao
    abstract fun challengeDao(): ChallengeDao
    // reward dao
    abstract fun rewardDao(): RewardDao

    companion object {
        @Volatile
        // singleton instance
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "eco_track_db"
                ).build() // build database
                INSTANCE = instance // save instance
                instance // return instance
            }
        }
    }
}