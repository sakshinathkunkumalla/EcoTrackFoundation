package com.example.ecotrack.repository

import com.example.ecotrack.model.RewardDao
import com.example.ecotrack.model.RewardEntity
import kotlinx.coroutines.flow.Flow

class RewardRepository(private val dao: RewardDao) {

    // fetch rewards
    fun getAllRewards(): Flow<List<RewardEntity>> = dao.getAllRewards()

    // add reward
    suspend fun insert(reward: RewardEntity) = dao.insert(reward)

    // update reward
    suspend fun update(reward: RewardEntity) = dao.update(reward)

    // remove reward
    suspend fun delete(reward: RewardEntity) = dao.delete(reward)
}