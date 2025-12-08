package com.example.ecotrack.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecotrack.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(vm: DashboardViewModel = viewModel()) {

    // collect activities
    val activities by vm.activities.collectAsState()

    // main column
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // screen title
        Text("Dashboard", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(16.dp))

        MetricCard("Total Active Minutes", vm.getTotalSteps().toString()) // metric steps
        MetricCard("Calories Burned", vm.getTotalCalories().toString()) // metric calories
        MetricCard("CO₂ Saved (kg)", String.format("%.2f", vm.getTotalCO2())) // metric CO2
        Spacer(modifier = Modifier.height(16.dp))

        // section title
        Text("Recent Activities", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))

        // recent activities
        activities.take(5).forEach { activity ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {

                        // activity type
                        Text(activity.type, style = MaterialTheme.typography.subtitle1)

                        // activity details
                        Text("${activity.durationMinutes} min • ${activity.caloriesBurned} cal")
                    }
                    // CO2 saved
                    Text(String.format("%.2f kg CO₂", activity.co2Saved))
                }
            }
        }
    }
}

@Composable
fun MetricCard(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title) // metric title

            // metric value
            Text(value, style = MaterialTheme.typography.h6)
        }
    }
}