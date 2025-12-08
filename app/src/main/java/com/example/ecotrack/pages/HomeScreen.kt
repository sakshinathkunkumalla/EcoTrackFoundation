package com.example.ecotrack.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecotrack.model.ActivityEntity
import com.example.ecotrack.viewmodel.ActivityViewModel
import com.example.ecotrack.viewmodel.ChallengeViewModel
import com.example.ecotrack.viewmodel.RewardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    activityViewModel: ActivityViewModel,
    challengeViewModel: ChallengeViewModel,
    rewardViewModel: RewardViewModel,
    onLogout: () -> Unit,
    onAddActivity: () -> Unit,
    onEditActivity: (activityId: Int) -> Unit,
    onNavigateToChallenges: () -> Unit,
    onNavigateToRewards: () -> Unit
) {
    val activities by activityViewModel.activities.collectAsState(initial = emptyList())
    val challenges by challengeViewModel.challenges.collectAsState(initial = emptyList())
    val rewards by rewardViewModel.rewards.collectAsState(initial = emptyList())

    val completedChallenges = challenges.count { it.progress >= it.target }
    val redeemedRewards = rewards.count { it.redeemed }

    val scrollState = rememberScrollState()

    Scaffold(
        // Top Bar
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "EcoTrack Dashboard",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    // Logout Button
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E88E5)
                )
            )
        },

        // FAB Button
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddActivity,
                containerColor = Color(0xFF1E88E5),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Activity")
            }
        },

        containerColor = Color(0xFFF2F5F7)
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Text(
                "Welcome Back!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E88E5)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text("Here’s your personalized dashboard.", fontSize = 16.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            // Dashboard row 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardCard(
                    title = "Activities",
                    value = activities.size.toString(),
                    icon = Icons.Filled.DirectionsWalk,
                    modifier = Modifier.weight(1f)
                ) { onAddActivity() }

                DashboardCard(
                    title = "CO₂ Saved",
                    value = String.format("%.1f kg", activityViewModel.totalCO2Saved()),
                    icon = Icons.Filled.AutoGraph,
                    modifier = Modifier.weight(1f)
                ) { }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dashboard row 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardCard(
                    title = "Challenges",
                    value = "${completedChallenges} / ${challenges.size}",
                    icon = Icons.Filled.Flag,
                    modifier = Modifier.weight(1f)
                ) { onNavigateToChallenges() }

                DashboardCard(
                    title = "Rewards",
                    value = "${redeemedRewards} / ${rewards.size}",
                    icon = Icons.Filled.CardGiftcard,
                    modifier = Modifier.weight(1f)
                ) { onNavigateToRewards() }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                "Recent Activities",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E88E5)
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (activities.isEmpty()) {
                Text("No activities logged yet.", color = Color.Gray)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    activities.forEach { activity ->
                        ActivityCard(
                            activity = activity,
                            onEdit = { onEditActivity(activity.id) },
                            onDelete = { activityViewModel.deleteActivity(activity) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// Dashboard Card

@Composable
fun DashboardCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    accent: Color = Color(0xFF1E88E5),
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = title, tint = accent, modifier = Modifier.size(32.dp))

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF263238)
                )
                Text(
                    text = title,
                    fontSize = 13.sp,
                    color = Color(0xFF546E7A)
                )
            }
        }
    }
}

// Activity Card
@Composable
fun ActivityCard(
    activity: ActivityEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Text(activity.type, color = Color(0xFF1E88E5), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

                Text(
                    "~${String.format("%.1f", activity.co2Saved)} kg CO₂",
                    color = Color.Gray,
                    fontSize = 13.sp
                )

                Text(
                    "${activity.durationMinutes} min • ${activity.caloriesBurned} cal",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Row {
                TextButton(onClick = onEdit) {
                    Text("Edit", color = Color(0xFF1E88E5))
                }
                TextButton(onClick = onDelete) {
                    Text("Delete", color = Color.Red)
                }
            }
        }
    }
}