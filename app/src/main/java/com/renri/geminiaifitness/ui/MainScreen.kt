package com.renri.geminiaifitness.ui

import android.view.ViewGroup
import android.widget.CalendarView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.renri.geminiaifitness.ui.navigation.Screen
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.saveable.rememberSaveable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    // ✅ Store selected difficulty (persists across recompositions)
    var selectedDifficulty by rememberSaveable { mutableStateOf("Good luck, Break a Sweat!") }

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
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = selectedDifficulty,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // CalendarView wrapped inside a Box
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { context ->
                        CalendarView(context).apply {
                            val today = Calendar.getInstance()
                            date = today.timeInMillis

                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, // Full width
                                ViewGroup.LayoutParams.WRAP_CONTENT  // Auto height
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 📌 Spacer to position the button correctly
            Spacer(modifier = Modifier.height(36.dp))

            // "Choose Your Poison" button
            Button(
                onClick = { navController.navigate(Screen.SettingsDifficulty.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Choose Your Poison")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(navController = rememberNavController())
}
