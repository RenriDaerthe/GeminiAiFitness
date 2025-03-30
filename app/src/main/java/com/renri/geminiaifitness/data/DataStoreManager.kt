package com.renri.geminiaifitness.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.renri.geminiaifitness.ui.models.Difficulty

// Extension function for DataStore
val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {
    private val DIFFICULTY_KEY = stringPreferencesKey("difficulty_level")

    // Read difficulty setting
    val difficultyFlow: Flow<Difficulty> = context.dataStore.data.map { preferences ->
        when (preferences[DIFFICULTY_KEY]) {
            "Medium" -> Difficulty.Medium
            "Hard" -> Difficulty.Hard
            else -> Difficulty.Easy  // Default
        }
    }

    // Save difficulty setting
    suspend fun saveDifficulty(difficulty: Difficulty) {
        context.dataStore.edit { preferences ->
            preferences[DIFFICULTY_KEY] = difficulty.name
        }
    }
}
