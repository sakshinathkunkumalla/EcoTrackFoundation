package com.example.ecotrack.viewmodel

import androidx.lifecycle.ViewModel
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

    fun addActivity(activity: ActivityEntity) = viewModelScope.launch {
        repository.addActivity(activity)
    }

    fun updateActivity(activity: ActivityEntity) = viewModelScope.launch {
        repository.updateActivity(activity)
    }

    fun deleteActivity(activity: ActivityEntity) = viewModelScope.launch {
        repository.deleteActivity(activity)
    }

    fun totalCO2Saved(): Double {
        return _activities.value.sumOf { it.co2Saved }
    }
}