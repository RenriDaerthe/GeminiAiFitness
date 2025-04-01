package com.renri.geminiaifitness.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient

// API Request Model
data class WorkoutRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)

// API Response Model
data class WorkoutResponse(
    val candidates: List<Candidate>
)

data class Candidate(
    val content: Content
)


private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(180, TimeUnit.SECONDS) // Time to establish a connection
    .readTimeout(180, TimeUnit.SECONDS) // Time to read the response
    .writeTimeout(180, TimeUnit.SECONDS) // Time to send data
    .build()

// Retrofit API Interface
interface GeminiAiApi {
    @POST("models/gemini-2.0-flash:generateContent")
    suspend fun generateWorkout(
        @Query("key") apiKey: String,
        @Body request: WorkoutRequest
    ): WorkoutResponse
}

// Gemini AI Service
object GeminiAiService {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/"
    private const val API_KEY = "" // Replace this with your real API key

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(GeminiAiApi::class.java)

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
        return api.generateWorkout(API_KEY, request)
    }
}
