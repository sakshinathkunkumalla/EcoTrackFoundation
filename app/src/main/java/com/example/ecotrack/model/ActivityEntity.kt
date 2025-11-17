package com.example.ecotrack.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String,
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val co2Saved: Double,
    val notes: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)