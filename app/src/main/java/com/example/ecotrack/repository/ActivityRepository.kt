package com.example.ecotrack.repository

import com.example.ecotrack.model.ActivityDao
import com.example.ecotrack.model.ActivityEntity
import kotlinx.coroutines.flow.Flow

class ActivityRepository(private val dao: ActivityDao) {

    fun getAllActivities(): Flow<List<ActivityEntity>> = dao.getAllActivities()

    suspend fun addActivity(activity: ActivityEntity) = dao.insertActivity(activity)

    suspend fun updateActivity(activity: ActivityEntity) = dao.updateActivity(activity)

    suspend fun deleteActivity(activity: ActivityEntity) = dao.deleteActivity(activity)
}