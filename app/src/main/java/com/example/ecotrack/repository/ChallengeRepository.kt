package com.example.ecotrack.repository

import com.example.ecotrack.model.ChallengeDao
import com.example.ecotrack.model.ChallengeEntity
import kotlinx.coroutines.flow.Flow

class ChallengeRepository(private val dao: ChallengeDao) {
    // fetch challenges
    fun getAllChallenges(): Flow<List<ChallengeEntity>> = dao.getAllChallenges()

    // add challenge
    suspend fun insert(challenge: ChallengeEntity) = dao.insert(challenge)

    // update challenge
    suspend fun update(challenge: ChallengeEntity) = dao.update(challenge)

    // remove challenge
    suspend fun delete(challenge: ChallengeEntity) = dao.delete(challenge)
}