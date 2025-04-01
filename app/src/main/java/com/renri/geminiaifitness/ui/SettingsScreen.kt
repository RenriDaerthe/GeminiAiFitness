package com.renri.geminiaifitness.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.renri.geminiaifitness.ui.navigation.Screen
import com.renri.geminiaifitness.ui.viewmodels.DifficultyViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    difficultyViewModel: DifficultyViewModel = viewModel()
) {
    val difficulty by difficultyViewModel.difficulty.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text("Difficulty Level: $difficulty", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.SettingsDifficulty.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Change Difficulty")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.WorkOutSettings.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Workout Settings")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.WorkOutSettingRec.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("GeminiAi Re-Roll")
            }
        }
    }
}
