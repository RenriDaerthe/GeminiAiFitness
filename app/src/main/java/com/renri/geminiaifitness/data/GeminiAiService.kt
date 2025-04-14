package com.renri.geminiaifitness.data

import com.renri.geminiaifitness.network.GeminiBackendApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Body


// ===========================
// 🔐 Gemini AI Service (Workout Generator)
// ===========================

// API Request Model for Workout Generation
data class WorkoutRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)

// API Response Model for Workout Generation
data class WorkoutResponse(
    val candidates: List<Candidate>
)

data class Candidate(
    val content: Content
)

// Create an OkHttp client with logging for network calls
private val logging = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(logging)  // Attach the logging interceptor
    .connectTimeout(180, TimeUnit.SECONDS) // Time to establish a connection
    .readTimeout(180, TimeUnit.SECONDS) // Time to read the response
    .writeTimeout(180, TimeUnit.SECONDS) // Time to send data
    .build()

// Retrofit Gemini AI Interface for workout generation
interface GeminiAiApi {
    @POST("models/gemini-2.0-flash:generateContent")
    suspend fun generateWorkout(
        @Query("key") apiKey: String,
        @Body request: WorkoutRequest
    ): WorkoutResponse
}

// ===========================
// 🔐 Unified Gemini AI and Backend Service
// ===========================

object GeminiAiService {
    private const val BASE_URL_AI = "https://generativelanguage.googleapis.com/v1beta/" // AI Service URL
    private const val BASE_URL_BACKEND = "http://10.0.2.2:3000/"  // Backend Service URL (for login/register)

    // Retrofit for AI API (workout generation)
    private val retrofitAi = Retrofit.Builder()
        .baseUrl(BASE_URL_AI)
        .client(okHttpClient)  // Use OkHttp client with logging
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Retrofit for Backend API (login/register)
    private val retrofitBackend = Retrofit.Builder()
        .baseUrl(BASE_URL_BACKEND)
        .client(okHttpClient)  // Use OkHttp client with logging
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // AI API service
    val apiAi = retrofitAi.create(GeminiAiApi::class.java)  // Make `apiAi` public

    // Backend API service
    val apiBackend = retrofitBackend.create(GeminiBackendApi::class.java)  // API for login/register

    // Function to get workouts (Gemini AI)
    suspend fun getWorkouts(userInput: String): WorkoutResponse {
        val request = WorkoutRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = userInput)
                    )
                )
            )
        )
        return apiAi.generateWorkout("", request) // Replace with your API Key
    }
}
