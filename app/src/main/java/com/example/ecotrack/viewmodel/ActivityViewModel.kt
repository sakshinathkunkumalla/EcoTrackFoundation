package com.example.ecotrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ecotrack.model.ActivityEntity
import com.example.ecotrack.repository.ActivityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActivityViewModel(private val repository: ActivityRepository) : ViewModel() {

    private val _activities = MutableStateFlow<List<ActivityEntity>>(emptyList())
    val activities: StateFlow<List<ActivityEntity>> = _activities.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllActivities().collectLatest {
                _activities.value = it
            }
        }
    }

    // Add activities
    fun addActivity(activity: ActivityEntity) = viewModelScope.launch {
        repository.addActivity(activity)
    }

    // Update activities
    fun updateActivity(activity: ActivityEntity) = viewModelScope.launch {
        repository.updateActivity(activity)
    }

    // Delete activities
    fun deleteActivity(activity: ActivityEntity) = viewModelScope.launch {
        repository.deleteActivity(activity)
    }

    // Helper functions
    fun totalCO2Saved(): Double = _activities.value.sumOf { it.co2Saved }
    fun totalCaloriesBurned(): Int = _activities.value.sumOf { it.caloriesBurned }
    fun totalDurationMinutes(): Int = _activities.value.sumOf { it.durationMinutes }

    // Factory for Compose / NavHost
    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: ActivityRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ActivityViewModel::class.java)) {
                return ActivityViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}