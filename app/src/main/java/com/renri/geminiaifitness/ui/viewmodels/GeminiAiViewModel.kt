package com.renri.geminiaifitness.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renri.geminiaifitness.data.GeminiAiService
import com.renri.geminiaifitness.data.WorkoutResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.renri.geminiaifitness.ui.models.Difficulty


class GeminiAiViewModel : ViewModel() {

    // Holds the generated workout response
    private val _workoutResponse = MutableStateFlow<WorkoutResponse?>(null)
    val workoutResponse: StateFlow<WorkoutResponse?> = _workoutResponse

    // Holds error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Function to fetch workouts from Gemini AI
    fun generateWorkout(userInput: String, difficulty: Difficulty, workOutSettingsViewModel: WorkOutSettingsViewModel) {
        viewModelScope.launch {
            try {
                // ✅ Fetch user's stored equipment
                val userSettings = workOutSettingsViewModel.userSettings.value
                val equipment = userSettings["equipment"] ?: "Bodyweight only"

                // ✅ Construct a concise AI request
                val refinedUserInput = buildString {
                    append("Generate a structured workout plan with the following details:\n")
                    append(userInput).append("\n\n")

                    // ✅ Define workout schedule based on difficulty level
                    append("Workout Schedule:\n")
                    when (difficulty) {
                        Difficulty.Easy -> append("- **Monday, Thursday**\n")
                        Difficulty.Medium -> append("- **Monday, Wednesday, Friday, Sunday**\n")
                        Difficulty.Hard -> append("- **Monday - Saturday**\n")
                    }

                    append("\nFor each workout session, provide ONLY the following:\n")
                    append("- **Day:** (e.g., Monday)\n")
                    append("- **Warm-up:** List 2-3 warm-up exercises.\n")
                    append("- **Workout:** List 4-6 exercises, sets, and reps.\n")
                    append("- **Cool-down:** List 2-3 cool-down exercises.\n\n")

                    append("Ensure that:\n")
                    append("- Workouts are **30-45 minutes long**.\n")
                    append("- Exercises use only the following equipment: $equipment.\n")
                    append("- No unnecessary explanations. **Just list the workouts.**")
                }

                val response = GeminiAiService.getWorkouts(refinedUserInput)
                _workoutResponse.value = response
                _errorMessage.value = null // Clear previous errors
            } catch (e: Exception) {
                _errorMessage.value = "Failed to generate workout: ${e.message}"
            }
        }
    }

}
