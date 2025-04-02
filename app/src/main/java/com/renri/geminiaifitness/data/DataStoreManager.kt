package com.renri.geminiaifitness.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.renri.geminiaifitness.ui.models.Difficulty
import com.renri.geminiaifitness.ui.models.WorkoutEntry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

// Extension function for DataStore
val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {

    // --- Keys ---
    private val DIFFICULTY_KEY = stringPreferencesKey("difficulty_level")
    private val AGE_KEY = stringPreferencesKey("age")
    private val WEIGHT_KEY = stringPreferencesKey("weight")
    private val HEIGHT_KEY = stringPreferencesKey("height")
    private val WEIGHT_GOAL_KEY = stringPreferencesKey("weight_goal")
    private val EQUIPMENT_KEY = stringPreferencesKey("equipment")
    private val INCLUDE_CARDIO_KEY = booleanPreferencesKey("include_cardio")
    private val WORKOUT_ENTRIES_KEY = stringPreferencesKey("workout_entries") //  New key

    // --- Difficulty Flow ---
    val difficultyFlow: Flow<Difficulty> = context.dataStore.data.map { preferences ->
        when (preferences[DIFFICULTY_KEY]) {
            "Medium" -> Difficulty.Medium
            "Hard" -> Difficulty.Hard
            else -> Difficulty.Easy  // Default
        }
    }

    suspend fun saveDifficulty(difficulty: Difficulty) {
        context.dataStore.edit { preferences ->
            preferences[DIFFICULTY_KEY] = difficulty.name
        }
    }

    // --- User Settings ---
    suspend fun saveUserSettings(
        age: String,
        weight: String,
        height: String,
        weightGoal: String,
        equipment: String,
        includeCardio: Boolean
    ) {
        context.dataStore.edit { preferences ->
            preferences[AGE_KEY] = age
            preferences[WEIGHT_KEY] = weight
            preferences[HEIGHT_KEY] = height
            preferences[WEIGHT_GOAL_KEY] = weightGoal
            preferences[EQUIPMENT_KEY] = equipment
            preferences[INCLUDE_CARDIO_KEY] = includeCardio
        }
    }

    val userSettingsFlow: Flow<Map<String, Any>> = context.dataStore.data.map { preferences ->
        mapOf(
            "age" to (preferences[AGE_KEY] ?: ""),
            "weight" to (preferences[WEIGHT_KEY] ?: ""),
            "height" to (preferences[HEIGHT_KEY] ?: ""),
            "weightGoal" to (preferences[WEIGHT_GOAL_KEY] ?: ""),
            "equipment" to (preferences[EQUIPMENT_KEY] ?: ""),
            "includeCardio" to (preferences[INCLUDE_CARDIO_KEY] ?: false)
        )
    }

    fun getUserSettings(): Map<String, Any> = runBlocking {
        context.dataStore.data.first().let { preferences ->
            mapOf(
                "age" to (preferences[AGE_KEY] ?: ""),
                "weight" to (preferences[WEIGHT_KEY] ?: ""),
                "height" to (preferences[HEIGHT_KEY] ?: ""),
                "weightGoal" to (preferences[WEIGHT_GOAL_KEY] ?: ""),
                "equipment" to (preferences[EQUIPMENT_KEY] ?: ""),
                "includeCardio" to (preferences[INCLUDE_CARDIO_KEY] ?: false)
            )
        }
    }

    // --- Workout Entries (NEW) ---
    suspend fun saveWorkoutEntries(entries: List<WorkoutEntry>) {
        val json = Json.encodeToString(entries)
        context.dataStore.edit { preferences ->
            preferences[WORKOUT_ENTRIES_KEY] = json
        }
    }

    fun getWorkoutEntries(): Flow<List<WorkoutEntry>> {
        return context.dataStore.data.map { preferences ->
            preferences[WORKOUT_ENTRIES_KEY]?.let { json ->
                try {
                    Json.decodeFromString<List<WorkoutEntry>>(json)
                } catch (e: Exception) {
                    emptyList()
                }
            } ?: emptyList()
        }
    }
}
