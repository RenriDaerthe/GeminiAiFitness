package com.renri.geminiaifitness.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renri.geminiaifitness.network.RetrofitInstance // Import RetrofitInstance
import com.renri.geminiaifitness.ui.models.LoginRequest
import com.renri.geminiaifitness.ui.models.LoginResponse
import com.renri.geminiaifitness.ui.models.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData



class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<ResultState>(ResultState.Idle)  // Default to Idle
    val loginState: StateFlow<ResultState> = _loginState
    private val _registerState = MutableStateFlow<ResultState>(ResultState.Idle) // Idle as default state
    val registerState: StateFlow<ResultState> = _registerState

    fun login(username: String, password: String) {
        _loginState.value = ResultState.Loading  // Set state to loading
        viewModelScope.launch {
            try {
                // Call the loginUser function from RetrofitInstance (or backend API)
                val response = RetrofitInstance.api.loginUser(LoginRequest(username, password))

                // Handle response based on success or failure
                if (response.isSuccessful && response.body() != null) {
                    _loginState.value = ResultState.Success(response.body()!!)
                } else {
                    _loginState.value = ResultState.Error("Login failed: ${response.code()}")
                }
            } catch (e: Exception) {
                // Handle any network or other exceptions
                _loginState.value = ResultState.Error("Exception: ${e.message}")
            }
        }
    }

    // Register function - using GeminiBackendApi for backend calls
    fun register(username: String, password: String) {
        _registerState.value = ResultState.Loading  // Set state to loading
        viewModelScope.launch {
            try {
                // Call the registerUser function from RetrofitInstance (or backend API)
                val response = RetrofitInstance.api.registerUser(RegisterRequest(username, password))

                // Handle response based on success or failure
                if (response.isSuccessful && response.body() != null) {
                    _registerState.value = ResultState.Success(response.body()!!)
                } else {
                    _registerState.value = ResultState.Error("Registration failed: ${response.code()}")
                }
            } catch (e: Exception) {
                // Handle any network or other exceptions
                _registerState.value = ResultState.Error("Exception: ${e.message}")
            }
        }
    }
}

