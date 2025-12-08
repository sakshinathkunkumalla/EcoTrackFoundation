package com.example.ecotrack.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardDao {

    // Fetch Rewards
    @Query("SELECT * FROM rewards ORDER BY id DESC")
    fun getAllRewards(): Flow<List<RewardEntity>>

    // Add Reward
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reward: RewardEntity)

    // Update Rewards
    @Update
    suspend fun update(reward: RewardEntity)

    // Delete Rewards
    @Delete
    suspend fun delete(reward: RewardEntity)
}