package com.example.ecotrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ecotrack.model.RewardEntity
import com.example.ecotrack.repository.RewardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RewardViewModel(private val repository: RewardRepository) : ViewModel() {
    // internal rewards
    private val _rewards = MutableStateFlow<List<RewardEntity>>(emptyList())
    val rewards: StateFlow<List<RewardEntity>> = _rewards.asStateFlow()

    init {
        // collect rewards
        viewModelScope.launch {
            repository.getAllRewards().collectLatest { _rewards.value = it }
        }
    }

    // add reward
    fun addReward(reward: RewardEntity) = viewModelScope.launch { repository.insert(reward) }

    // update reward
    fun updateReward(reward: RewardEntity) = viewModelScope.launch { repository.update(reward) }

    // remove reward
    fun deleteReward(reward: RewardEntity) = viewModelScope.launch { repository.delete(reward) }

    class Factory(private val repository: RewardRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RewardViewModel::class.java)) {
                // create viewmodel
                return RewardViewModel(repository) as T
            }
            // unknown class
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}