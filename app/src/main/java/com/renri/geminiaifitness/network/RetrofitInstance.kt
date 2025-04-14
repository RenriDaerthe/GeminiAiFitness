package com.renri.geminiaifitness.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:3000/"

    // Create an OkHttpClient with increased timeouts
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)  // Increase connection timeout
        .readTimeout(60, TimeUnit.SECONDS)     // Increase read timeout
        .writeTimeout(60, TimeUnit.SECONDS)    // Increase write timeout
        .build()

    // Retrofit instance with OkHttpClient
    val api: GeminiBackendApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Set the OkHttpClient with custom timeouts
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiBackendApi::class.java)
    }
}
