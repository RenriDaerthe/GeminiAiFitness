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
import com.renri.geminiaifitness.ui.viewmodels.WorkOutSettingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkOutSettingsScreen(navController: NavController, viewModel: WorkOutSettingsViewModel = viewModel()) {
    val coroutineScope = rememberCoroutineScope()

    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weightGoal by remember { mutableStateOf("") }
    var equipment by remember { mutableStateOf("") }
    var includeCardio by remember { mutableStateOf(false) } // Yes/No Toggle

    // Load stored settings when the screen opens
    LaunchedEffect(Unit) {
        val storedSettings = viewModel.getUserSettings()
        age = storedSettings["age"] as String
        weight = storedSettings["weight"] as String
        height = storedSettings["height"] as String
        weightGoal = storedSettings["weightGoal"] as String
        equipment = storedSettings["equipment"] as String
        includeCardio = storedSettings["includeCardio"] as Boolean
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout Settings") },
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
            Spacer(modifier = Modifier.height(16.dp))

            // "Personal" Section Header
            Text("Personal", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.align(Alignment.Start))

            OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight (lb)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("Height (in)") }, modifier = Modifier.fillMaxWidth())

            Text("Weight Goal", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.align(Alignment.Start))
            OutlinedTextField(value = weightGoal, onValueChange = { weightGoal = it }, label = { Text("Target Weight (lb)") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = equipment, onValueChange = { equipment = it }, label = { Text("Equipment (e.g., dumbbells, resistance bands)") }, modifier = Modifier.fillMaxWidth(), minLines = 1, maxLines = 10)

            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Include Cardio?", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                Switch(checked = includeCardio, onCheckedChange = { includeCardio = it })
            }

            Spacer(modifier = Modifier.weight(1f)) // Push button to bottom

            // ✅ Save button now stores user input in DataStore
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveUserSettings(age, weight, height, weightGoal, equipment, includeCardio)
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}
