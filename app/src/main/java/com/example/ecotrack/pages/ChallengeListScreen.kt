package com.example.ecotrack.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ecotrack.model.ChallengeEntity
import com.example.ecotrack.viewmodel.ChallengeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeListScreen(
    vm: ChallengeViewModel,
    onOpenChallenge: (Int) -> Unit,
    onAddChallenge: () -> Unit,
    onBack: () -> Unit
) {
    val challenges by vm.challenges.collectAsState()   // observe list

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Challenges", color = Color.White) },
                navigationIcon = {
                    // back arrow
                    IconButton(onClick = onBack) {
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
        },

        // add button
        floatingActionButton = {
            FloatingActionButton(onClick = onAddChallenge) {
                Icon(Icons.Default.Add, contentDescription = "Add Challenge")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            if (challenges.isEmpty()) {
                // empty text
                Text("No challenges yet. Add one to start your eco-journey!")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(challenges) { challenge ->
                        ChallengeCard(
                            challenge = challenge,
                            // open item
                            onOpen = { onOpenChallenge(challenge.id) },
                            // update progress
                            onJoin = { vm.incrementProgress(challenge) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChallengeCard(
    challenge: ChallengeEntity,
    onOpen: () -> Unit,
    onJoin: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // title
            Text(challenge.title, style = MaterialTheme.typography.titleMedium)

            // Description
            Text(challenge.description, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(10.dp))

            // full bar
            LinearProgressIndicator(
                progress = challenge.progress.toFloat() / challenge.target,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // count text
            Text(
                "${challenge.progress}/${challenge.target} completed",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row {
                // view btn
                TextButton(onClick = onOpen) { Text("View") }
                Spacer(modifier = Modifier.width(8.dp))
                // progress btn
                TextButton(onClick = onJoin) { Text("Mark Progress") }
            }
        }
    }
}