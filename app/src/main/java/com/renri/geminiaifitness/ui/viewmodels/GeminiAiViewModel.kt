package com.renri.geminiaifitness.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.renri.geminiaifitness.data.DataStoreManager
import com.renri.geminiaifitness.data.GeminiAiService
import com.renri.geminiaifitness.data.WorkoutResponse
import com.renri.geminiaifitness.ui.models.Difficulty
import com.renri.geminiaifitness.ui.models.WorkoutEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GeminiAiViewModel(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _workoutResponse = MutableStateFlow<WorkoutResponse?>(null)
    val workoutResponse: StateFlow<WorkoutResponse?> = _workoutResponse

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _workoutEntries = MutableStateFlow<List<WorkoutEntry>>(emptyList())
    val workoutEntries: StateFlow<List<WorkoutEntry>> = _workoutEntries

    init {
        viewModelScope.launch {
            dataStoreManager.getWorkoutEntries().collect {
                _workoutEntries.value = it
            }
        }
    }


    fun generateWorkout(
        difficulty: Difficulty,
        workOutSettingsViewModel: WorkOutSettingsViewModel
    ) {
        viewModelScope.launch {
            try {
                val userSettings = workOutSettingsViewModel.userSettings.value
                val equipment = userSettings["equipment"] ?: "Bodyweight only"

                val refinedUserInput = buildString {
                    append("Generate a weekly structured workout plan using the following format:")
                    append("\n\n")
                    append("Day: <Day of the Week>\n")
                    append("Warm-up:\n")
                    append("- Exercise Name - Duration (e.g., 30 sec)\n")
                    append("Workout:\n")
                    append("- Exercise Name - 3 sets x Reps (e.g., 3 sets x 10 reps)\n")
                    append("Cool-down:\n")
                    append("- Exercise Name - Duration (e.g., 1 min)\n\n")
                    append("Make sure to use only the following equipment: $equipment.\n")
                    append("The workout should be based on this difficulty: $difficulty.\n")
                    append("Include workouts on these days: ")
                    when (difficulty) {
                        Difficulty.Easy -> append("Monday, Thursday\n")
                        Difficulty.Medium -> append("Monday, Wednesday, Friday, Sunday\n")
                        Difficulty.Hard -> append("Monday to Saturday\n")
                    }
                    append("Keep the total workout duration per day around 30–45 minutes.\n")
                    append("Avoid explanations. Only list the exercises following the exact format above.")
                }

                val response = GeminiAiService.getWorkouts(refinedUserInput)
                _workoutResponse.value = response
                _errorMessage.value = null

                val parsedEntries = listOf<WorkoutEntry>() // placeholder
                dataStoreManager.saveWorkoutEntries(parsedEntries)
                _workoutEntries.value = parsedEntries

            } catch (e: Exception) {
                _errorMessage.value = "Failed to generate workout: ${e.message}"
            }
        }
    }


    fun saveParsedWorkout() {
        viewModelScope.launch {
            try {
                val rawText = workoutResponse.value
                    ?.candidates?.firstOrNull()
                    ?.content?.parts
                    ?.joinToString("\n") { it.text }
                    ?: return@launch


                val parsedEntries = parseWorkoutResponse(rawText)

                dataStoreManager.saveWorkoutEntries(parsedEntries)
                _workoutEntries.value = parsedEntries
            } catch (e: Exception) {
                _errorMessage.value = "Failed to save workout: ${e.message}"
            }
        }
    }

    private fun parseWorkoutResponse(text: String): List<WorkoutEntry> {
        val workouts = mutableListOf<WorkoutEntry>()
        val lines = text.split("\n").map { it.trim() }

        var currentDay: String? = null
        var warmUp = mutableListOf<String>()
        var mainWorkout = mutableListOf<String>()
        var coolDown = mutableListOf<String>()

        var section = ""

        for (line in lines) {
            when {
                line.contains("Day:", ignoreCase = true) -> {
                    // Save previous
                    currentDay?.let {
                        workouts.add(
                            WorkoutEntry(
                                dayOfWeek = it,
                                warmUp = warmUp.toList(),
                                mainWorkout = mainWorkout.toList(),
                                coolDown = coolDown.toList()
                            )
                        )
                    }
                    // Reset
                    warmUp.clear()
                    mainWorkout.clear()
                    coolDown.clear()
                    section = ""
                    currentDay = line.substringAfter(":").replace("*", "").trim()
                }

                line.contains("Warm-up", ignoreCase = true) -> section = "warmup"
                line.contains("Workout", ignoreCase = true) -> section = "main"
                line.contains("Cool-down", ignoreCase = true) -> section = "cooldown"

                line.startsWith("*") || line.startsWith("-") -> {
                    val clean = line.removePrefix("*").removePrefix("-").trim()
                    when (section) {
                        "warmup" -> warmUp.add(clean)
                        "main" -> mainWorkout.add(clean)
                        "cooldown" -> coolDown.add(clean)
                    }
                }
            }
        }

        // Save last one
        currentDay?.let {
            workouts.add(
                WorkoutEntry(
                    dayOfWeek = it,
                    warmUp = warmUp,
                    mainWorkout = mainWorkout,
                    coolDown = coolDown
                )
            )
        }

        return workouts
    }

    class Factory(private val dataStoreManager: DataStoreManager) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GeminiAiViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GeminiAiViewModel(dataStoreManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
