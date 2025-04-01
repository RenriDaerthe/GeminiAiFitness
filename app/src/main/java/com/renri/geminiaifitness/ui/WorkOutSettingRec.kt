package com.renri.geminiaifitness.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.renri.geminiaifitness.ui.viewmodels.GeminiAiViewModel
import com.renri.geminiaifitness.ui.viewmodels.DifficultyViewModel
import com.renri.geminiaifitness.ui.viewmodels.WorkOutSettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkOutSettingRec(
    navController: NavController,
    geminiViewModel: GeminiAiViewModel = viewModel(),
    difficultyViewModel: DifficultyViewModel = viewModel(),
    workOutSettingsViewModel: WorkOutSettingsViewModel = viewModel()
) {
    val workoutResponse by geminiViewModel.workoutResponse.collectAsState()
    val errorMessage by geminiViewModel.errorMessage.collectAsState()
    val difficulty by difficultyViewModel.difficulty.collectAsState() //  Get stored difficulty
    val userSettings by workOutSettingsViewModel.userSettings.collectAsState()

    var userInput by remember { mutableStateOf("") }

    //  Update userInput with stored data, including difficulty
    LaunchedEffect(userSettings, difficulty) {
        userInput = buildString {
            append("Create a ")
            append(
                when (difficulty) {
                    com.renri.geminiaifitness.ui.models.Difficulty.Easy -> "beginner-friendly workout"
                    com.renri.geminiaifitness.ui.models.Difficulty.Medium -> "intermediate-level workout"
                    com.renri.geminiaifitness.ui.models.Difficulty.Hard -> "high-intensity advanced workout"
                }
            )
            append(" for a ${userSettings["age"]} year old.\n")
            append("Current weight: ${userSettings["weight"]} lb, Height: ${userSettings["height"]} in.\n")
            append("Target weight: ${userSettings["weightGoal"]} lb.\n")
            append("Available equipment: ${userSettings["equipment"]}.\n")
            append("Include cardio: ${if (userSettings["includeCardio"] as Boolean) "Yes" else "No"}.\n")
            append("Ensure the workout aligns with the selected difficulty: $difficulty.")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gemini AI Workout") },
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
            Text("Using stored difficulty: $difficulty")

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                label = { Text("Describe your workout goal") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { geminiViewModel.generateWorkout(userInput, difficulty, workOutSettingsViewModel) }
            ) {
                Text("Generate Workout")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Scrollable LazyColumn for AI-generated workout response
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    when {
                        errorMessage != null -> {
                            Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error)
                        }
                        workoutResponse == null -> {
                            Text("Waiting for response...")
                        }
                        else -> {
                            Text("Generated Workout:")

                            workoutResponse?.candidates?.firstOrNull()?.content?.parts?.forEach {
                                Text(
                                    text = "- ${it.text}",
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
