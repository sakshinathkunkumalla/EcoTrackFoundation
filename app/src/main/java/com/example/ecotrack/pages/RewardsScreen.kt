package com.example.ecotrack.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecotrack.model.RewardEntity
import com.example.ecotrack.viewmodel.RewardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsScreen(vm: RewardViewModel, onBack: () -> Unit = {}) {
    val rewards by vm.rewards.collectAsState() // collect rewards

    Scaffold(
        // Top Bar
        topBar = {
            TopAppBar(
                title = { Text("Rewards") },
                navigationIcon = {
                    // back button
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            if (rewards.isEmpty()) {
                Text("No rewards yet. Complete challenges to earn rewards!") // empty text
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) { // list rewards
                    items(rewards) { reward ->
                        RewardCard(reward, onRedeem = { vm.updateReward(reward.copy(redeemed = true)) }) // redeem reward
                    }
                }
            }
        }
    }
}

@Composable
fun RewardCard(reward: RewardEntity, onRedeem: () -> Unit) {
    // card layout
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {

            // reward title
            Text(reward.title, style = MaterialTheme.typography.titleMedium)

            // reward description
            Text(reward.description)
            Spacer(Modifier.height(12.dp))
            if (!reward.redeemed) {
                // redeem button
                Button(onClick = onRedeem) { Text("Redeem") }
            } else {
                // redeemed text
                Text("Reward Redeemed ✓", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}