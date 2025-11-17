package com.example.ecotrack.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecotrack.model.ActivityEntity
import com.example.ecotrack.viewmodel.ActivityViewModel

@Composable
fun ActivityScreen(
    viewModel: ActivityViewModel,
    activityId: Int? = null,
    onBack: () -> Unit
) {
    val activities by viewModel.activities.collectAsState()
    val activityToEdit = activityId?.let { id -> activities.find { it.id == id } }

    var type by remember { mutableStateOf(activityToEdit?.type ?: "") }
    var duration by remember { mutableStateOf(activityToEdit?.durationMinutes?.toString() ?: "") }
    var calories by remember { mutableStateOf(activityToEdit?.caloriesBurned?.toString() ?: "") }
    var co2 by remember { mutableStateOf(activityToEdit?.co2Saved?.toString() ?: "") }
    var notes by remember { mutableStateOf(activityToEdit?.notes ?: "") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        OutlinedTextField(
            value = type,
            onValueChange = { type = it },
            label = { Text("Activity Type") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duration (minutes)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Calories Burned") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = co2,
            onValueChange = { co2 = it },
            label = { Text("CO₂ Saved (kg)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val newActivity = ActivityEntity(
                    id = activityToEdit?.id ?: 0,
                    type = type,
                    durationMinutes = duration.toIntOrNull() ?: 0,
                    caloriesBurned = calories.toIntOrNull() ?: 0,
                    co2Saved = co2.toDoubleOrNull() ?: 0.0,
                    notes = notes
                )

                if (activityToEdit != null) {
                    viewModel.updateActivity(newActivity)
                } else {
                    viewModel.addActivity(newActivity)
                }
                onBack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (activityToEdit != null) "Update Activity" else "Add Activity")
        }
    }
}