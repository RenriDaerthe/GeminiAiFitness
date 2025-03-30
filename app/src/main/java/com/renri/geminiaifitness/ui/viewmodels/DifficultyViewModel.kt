package com.renri.geminiaifitness.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.renri.geminiaifitness.ui.models.Difficulty
import com.renri.geminiaifitness.data.DataStoreManager

class DifficultyViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = DataStoreManager(application)

    private val _difficulty = MutableStateFlow(Difficulty.Easy)  // Default is Easy
    val difficulty: StateFlow<Difficulty> = _difficulty

    init {
        viewModelScope.launch {
            dataStore.difficultyFlow.collectLatest { savedDifficulty ->
                _difficulty.value = savedDifficulty  // ✅ Load saved difficulty
            }
        }
    }

    fun setDifficulty(newDifficulty: Difficulty) {
        viewModelScope.launch {
            _difficulty.value = newDifficulty  // ✅ Update in-memory
            dataStore.saveDifficulty(newDifficulty)  // ✅ Save to DataStore
        }
    }
}
