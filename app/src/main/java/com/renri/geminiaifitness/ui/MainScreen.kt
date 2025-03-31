package com.renri.geminiaifitness.ui

import android.view.ViewGroup
import android.widget.CalendarView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.renri.geminiaifitness.ui.models.Difficulty
import com.renri.geminiaifitness.ui.viewmodels.DifficultyViewModel
import com.renri.geminiaifitness.ui.navigation.Screen
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: DifficultyViewModel  // Shared ViewModel
) {
    val difficulty by viewModel.difficulty.collectAsState() //  Automatically updates

    val backgroundColor = when (difficulty) {
        Difficulty.Easy -> Color(0xFF4CAF50)  // Green for Easy
        Difficulty.Medium -> Color(0xFFFF9800)  // Orange for Medium
        Difficulty.Hard -> Color(0xFFF44336)  // Red for Hard
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GeminiAiFitness") },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)  //  Changes dynamically
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Calendar View
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { context ->
                        CalendarView(context).apply {
                            val today = Calendar.getInstance()
                            date = today.timeInMillis
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Text(
                text = "Difficulty: $difficulty",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}
