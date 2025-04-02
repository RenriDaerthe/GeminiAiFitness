package com.renri.geminiaifitness.ui

import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.renri.geminiaifitness.data.DataStoreManager
import com.renri.geminiaifitness.ui.models.Difficulty
import com.renri.geminiaifitness.ui.navigation.Screen
import com.renri.geminiaifitness.ui.viewmodels.DifficultyViewModel
import com.renri.geminiaifitness.ui.viewmodels.GeminiAiViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.navigation.NavController
import java.util.Locale
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: DifficultyViewModel = viewModel()
) {
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }

    val geminiViewModel: GeminiAiViewModel = viewModel(
        factory = GeminiAiViewModel.Factory(dataStoreManager)
    )

    val difficulty by viewModel.difficulty.collectAsState()
    val workoutEntries by geminiViewModel.workoutEntries.collectAsState()

    val backgroundColor = when (difficulty) {
        Difficulty.Easy -> Color(0xFF4CAF50)
        Difficulty.Medium -> Color(0xFFFF9800)
        Difficulty.Hard -> Color(0xFFF44336)
    }

    var selectedDay by remember { mutableStateOf(getDayOfWeek(System.currentTimeMillis())) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GeminiAiFitness") },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AndroidView(
                    factory = { context ->
                        CalendarView(context).apply {
                            val today = Calendar.getInstance()
                            date = today.timeInMillis
                            setOnDateChangeListener { _, year, month, dayOfMonth ->
                                val calendar = Calendar.getInstance().apply {
                                    set(year, month, dayOfMonth)
                                }
                                selectedDay = getDayOfWeek(calendar.timeInMillis)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Difficulty: $difficulty", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                Text("Selected Day: $selectedDay", style = MaterialTheme.typography.bodyMedium, color = Color.White)

                Spacer(modifier = Modifier.height(16.dp))
            }

            val filteredWorkouts = workoutEntries.filter { it.dayOfWeek.equals(selectedDay, ignoreCase = true) }

            if (filteredWorkouts.isNotEmpty()) {
                item {
                    Text("Your Saved Workouts", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(filteredWorkouts) { entry ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("📅 ${entry.dayOfWeek}", style = MaterialTheme.typography.titleSmall, color = Color.White)
                            Text("• Warm-up: ${entry.warmUp.joinToString()}", color = Color.White)
                            Text("• Workout: ${entry.mainWorkout.joinToString()}", color = Color.White)
                            Text("• Cool-down: ${entry.coolDown.joinToString()}", color = Color.White)
                        }
                    }
                }
            } else {
                item {
                    Text("No workouts saved for $selectedDay.", color = Color.White)
                }
            }
        }
    }
}

// ✅ Util for parsing selected day
fun getDayOfWeek(timeInMillis: Long): String {
    val calendar = Calendar.getInstance().apply {
        setTimeInMillis(timeInMillis)
    }
    return SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
}