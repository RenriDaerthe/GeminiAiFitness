package com.renri.geminiaifitness.ui


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.renri.geminiaifitness.ui.navigation.Screen
import androidx.compose.runtime.saveable.rememberSaveable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDifficultyScreen(navController: NavController) {
    var selectedDifficulty by rememberSaveable { mutableStateOf("None") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Difficulty") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Choose your workout difficulty:",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { selectedDifficulty = "Easy" }) {
                Text("Easy")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { selectedDifficulty = "Medium" }) {
                Text("Medium")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { selectedDifficulty = "Hard" }) {
                Text("Hard")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Selected Difficulty: $selectedDifficulty",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate(Screen.Main.route) }) {
                Text("Back to Home")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsDifficultyScreenPreview1() {
    SettingsDifficultyScreen(navController = rememberNavController())
}
