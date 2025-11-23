package com.example.ecotrack.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecotrack.viewmodel.ActivityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    activityViewModel: ActivityViewModel,
    onLogout: () -> Unit,
    onAddActivity: () -> Unit,
    onEditActivity: (activityId: Int) -> Unit
) {
    val activities by activityViewModel.activities.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EcoTrack Dashboard", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00796B)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddActivity,
                containerColor = Color(0xFF00796B)
            ) {
                Text("+", color = Color.White, fontSize = 24.sp)
            }
        },
        containerColor = Color(0xFFE0F2F1)
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            // Header
            Text(
                text = "Welcome Back!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00796B)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Here’s your eco-dashboard overview.",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Dashboard Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardCard(
                    title = "Activities",
                    value = activities.size.toString(),
                    modifier = Modifier.weight(1f)
                )
                DashboardCard(
                    title = "CO₂ Saved",
                    value = String.format("%.2f kg", activityViewModel.totalCO2Saved()),
                    modifier = Modifier.weight(1f)
                )
                DashboardCard(
                    title = "Challenges",
                    value = "3", // Placeholder, can be dynamic later
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Activities
            Text(
                text = "Recent Activities",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF00796B)
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (activities.isEmpty()) {
                Text(
                    text = "No activities logged yet.",
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(activities) { activity ->
                        ActivityCard(
                            activity = activity,
                            onEdit = { onEditActivity(activity.id) },
                            onDelete = { activityViewModel.deleteActivity(activity) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logout Button
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
            ) {
                Text(
                    text = "Log Out",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun DashboardCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF00796B)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ActivityCard(
    activity: com.example.ecotrack.model.ActivityEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.type,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00796B)
                )
                Text(
                    text = String.format("~%.2f kg CO₂", activity.co2Saved),
                    color = Color.Gray
                )
                Text(
                    text = "${activity.durationMinutes} min • ${activity.caloriesBurned} cal",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Row {
                TextButton(onClick = onEdit) {
                    Text("Edit", color = Color(0xFF00796B))
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = onDelete) {
                    Text("Delete", color = Color.Red)
                }
            }
        }
    }
}
