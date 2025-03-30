package com.renri.geminiaifitness.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.renri.geminiaifitness.ui.models.Difficulty
import com.renri.geminiaifitness.ui.viewmodels.DifficultyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifficultySettings(
    navController: NavController,
    viewModel: DifficultyViewModel  // ✅ Uses shared ViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Difficulty Settings") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    viewModel.setDifficulty(Difficulty.Easy)  // ✅ Updates ViewModel
                    navController.popBackStack()  // ✅ Returns to Main Screen
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Easy")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.setDifficulty(Difficulty.Medium)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
            ) {
                Text("Medium")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.setDifficulty(Difficulty.Hard)
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
            ) {
                Text("Hard")
            }
        }
    }
}
