package com.example.ecotrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ecotrack.model.ChallengeEntity
import com.example.ecotrack.model.RewardEntity
import com.example.ecotrack.repository.ChallengeRepository
import com.example.ecotrack.repository.RewardRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChallengeViewModel(
    private val challengeRepo: ChallengeRepository,
    private val rewardRepo: RewardRepository
) : ViewModel() {

    // internal challenges
    private val _challenges = MutableStateFlow<List<ChallengeEntity>>(emptyList())
    val challenges: StateFlow<List<ChallengeEntity>> = _challenges.asStateFlow()

    // internal rewards
    private val _rewards = MutableStateFlow<List<RewardEntity>>(emptyList())
    val rewards: StateFlow<List<RewardEntity>> = _rewards.asStateFlow()

    init {
        // update challenges
        viewModelScope.launch {
            challengeRepo.getAllChallenges().collect { list ->
                _challenges.value = list
            }
        }
        // update rewards
        viewModelScope.launch {
            rewardRepo.getAllRewards().collect { list ->
                _rewards.value = list
            }
        }
    }

    // add challenge
    fun addChallenge(challenge: ChallengeEntity) = viewModelScope.launch {
        challengeRepo.insert(challenge)
    }

    // update challenge
    fun updateChallenge(challenge: ChallengeEntity) = viewModelScope.launch {
        challengeRepo.update(challenge)
    }

    // remove challenge
    fun deleteChallenge(challenge: ChallengeEntity) = viewModelScope.launch {
        challengeRepo.delete(challenge)
    }

    // increment progress
    fun incrementProgress(challenge: ChallengeEntity) = viewModelScope.launch {
        if (challenge.progress >= challenge.target) return@launch
        val updated = challenge.copy(progress = challenge.progress + 1)
        challengeRepo.update(updated) // save update

        // add reward
        if (updated.progress == updated.target) {
            rewardRepo.insert(
                RewardEntity(
                    title = "Completed: ${updated.title}",
                    description = "Awarded for completing this challenge",
                    redeemed = false
                )
            )
        }
    }

    // count challenges
    val totalChallenges = challenges.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    // count rewards
    val totalRewards = rewards.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    // progress percent
    fun completionPercentage(challenge: ChallengeEntity): Float =
        if (challenge.target == 0) 0f else challenge.progress.toFloat() / challenge.target.toFloat()

    class Factory(
        private val challengeRepo: ChallengeRepository,
        private val rewardRepo: RewardRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChallengeViewModel::class.java)) {
                // create viewmodel
                return ChallengeViewModel(challengeRepo, rewardRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel")
        }
    }
}