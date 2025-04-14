package com.renri.geminiaifitness.ui.viewmodels

sealed class ResultState {
    object Idle : ResultState()  // Added idle state to prevent loading on startup
    object Loading : ResultState()
    data class Success(val data: Any) : ResultState()  // Adjust the type as needed
    data class Error(val message: String) : ResultState()
}
