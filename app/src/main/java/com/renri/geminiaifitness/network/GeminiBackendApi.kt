package com.renri.geminiaifitness.network

import com.renri.geminiaifitness.ui.models.LoginRequest
import com.renri.geminiaifitness.ui.models.LoginResponse
import com.renri.geminiaifitness.ui.models.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Interface for backend API calls (Login and Register)
interface GeminiBackendApi {

    // Login endpoint
    @POST("login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    // Register endpoint
    @POST("register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<Void>
}
