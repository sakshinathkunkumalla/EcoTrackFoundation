package com.example.ecotrack.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecotrack.model.ActivityEntity
import com.example.ecotrack.model.AppDatabase
import com.example.ecotrack.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).activityDao()
    private val repository = FirebaseRepository()

    private val _activities = MutableStateFlow<List<ActivityEntity>>(emptyList())
    val activities: StateFlow<List<ActivityEntity>> = _activities.asStateFlow()

    init {
        fetchLocalActivities()
        syncWithFirebase()
    }

    private fun fetchLocalActivities() {
        viewModelScope.launch {
            dao.getAllActivities().collect { list ->
                _activities.value = list
            }
        }
    }

    private fun syncWithFirebase() {
        viewModelScope.launch {
            // Push local activities to Firebase
            _activities.value.forEach { repository.addActivity(it) }

            // Optional: Fetch Firebase activities and merge with local
            val firebaseActivities = repository.getActivities()
            firebaseActivities.forEach {
                dao.insertActivity(it)
            }
        }
    }

    // Calculate total metrics
    fun getTotalSteps(): Int = _activities.value.sumOf { it.durationMinutes }
    fun getTotalCalories(): Int = _activities.value.sumOf { it.caloriesBurned }
    fun getTotalCO2(): Double = _activities.value.sumOf { it.co2Saved }
}
