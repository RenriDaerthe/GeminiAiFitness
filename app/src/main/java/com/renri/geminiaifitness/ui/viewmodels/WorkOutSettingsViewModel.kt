package com.renri.geminiaifitness.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.renri.geminiaifitness.data.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkOutSettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = DataStoreManager(application)

    private val _userSettings = MutableStateFlow<Map<String, Any>>(emptyMap())
    val userSettings: StateFlow<Map<String, Any>> = _userSettings

    init {
        viewModelScope.launch {
            _userSettings.value = dataStore.getUserSettings()
        }
    }

    fun getUserSettings(): Map<String, Any> {
        return _userSettings.value
    }

    fun saveUserSettings(age: String, weight: String, height: String, weightGoal: String, equipment: String, includeCardio: Boolean) {
        viewModelScope.launch {
            dataStore.saveUserSettings(age, weight, height, weightGoal, equipment, includeCardio)
            _userSettings.value = dataStore.getUserSettings() // Update UI state after saving
        }
    }
}
