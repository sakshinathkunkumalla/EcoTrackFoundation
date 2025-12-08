package com.example.ecotrack.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {

    // Get All Challenges
    @Query("SELECT * FROM challenges ORDER BY id DESC")
    fun getAllChallenges(): Flow<List<ChallengeEntity>>

    // Add Challenges
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(challenge: ChallengeEntity)

    // Update Challenges
    @Update
    suspend fun update(challenge: ChallengeEntity)

    // Delete Challenges
    @Delete
    suspend fun delete(challenge: ChallengeEntity)
}