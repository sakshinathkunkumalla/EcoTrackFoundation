package com.example.ecotrack.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    // fetch activities
    @Query("SELECT * FROM activities ORDER BY timestamp DESC")
    fun getAllActivities(): Flow<List<ActivityEntity>>

    // add activity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity)

    // Update activity
    @Update
    suspend fun updateActivity(activity: ActivityEntity)

    // Delete activity
    @Delete
    suspend fun deleteActivity(activity: ActivityEntity)
}