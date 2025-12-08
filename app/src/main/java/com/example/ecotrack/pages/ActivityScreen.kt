package com.example.ecotrack.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecotrack.model.ActivityEntity
import com.example.ecotrack.viewmodel.ActivityViewModel
import androidx.compose.ui.graphics.Color


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    viewModel: ActivityViewModel,
    activityId: Int? = null,
    navController: NavController
) {
    // collect activities
    val activities by viewModel.activities.collectAsState()

    // find activity
    val activityToEdit = activityId?.let { id -> activities.find { it.id == id } }

    var type by remember { mutableStateOf(activityToEdit?.type ?: "") }
    var duration by remember { mutableStateOf(activityToEdit?.durationMinutes?.toString() ?: "") }
    var calories by remember { mutableStateOf(activityToEdit?.caloriesBurned?.toString() ?: "") }
    var co2 by remember { mutableStateOf(activityToEdit?.co2Saved?.toString() ?: "") }
    var notes by remember { mutableStateOf(activityToEdit?.notes ?: "") }

    Column(modifier = Modifier.fillMaxSize()) {

        // Top AppBar
        TopAppBar(
            title = {
                Text(
                    text = if (activityToEdit != null) "Edit Activity" else "Add Activity",
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("home") }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF1E88E5)
            )
        )

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            // type input
            OutlinedTextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Activity Type") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // duration input
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (minutes)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // calories input
            OutlinedTextField(
                value = calories,
                onValueChange = { calories = it },
                label = { Text("Calories Burned") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // co2 input
            OutlinedTextField(
                value = co2,
                onValueChange = { co2 = it },
                label = { Text("CO₂ Saved (kg)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // notes input
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
                        // update activity
                        viewModel.updateActivity(newActivity)
                    } else {
                        // add activity
                        viewModel.addActivity(newActivity)
                    }
                    // navigate home
                    navController.navigate("home")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E88E5),
                    contentColor = Color.White
                )
            ) {
                Text(if (activityToEdit != null) "Update Activity" else "Add Activity")
            }
        }
    }
}