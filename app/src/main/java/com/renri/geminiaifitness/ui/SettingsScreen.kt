package com.renri.geminiaifitness.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.renri.geminiaifitness.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                actions = {
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

            // "Choose Your Poison" button (Moved from MainScreen)
            Button(
                onClick = { navController.navigate(Screen.SettingsDifficulty.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Choose Your Poison")
            }

            Spacer(modifier = Modifier.height(16.dp))

            //WorkoutSettings Button
            Button(
                onClick = { navController.navigate(Screen.WorkOutSettings.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Workout Settings")
            }
        }
    }
}
