/*
 * ApiConfig.kt
 *
 * This file configures and provides an instance of the ApiService for making network requests using Retrofit.
 *
 * Key Components:
 * - Uses OkHttpClient for handling HTTP requests.
 * - Includes HttpLoggingInterceptor to log request and response details for debugging.
 * - Sets the base URL for the API.
 * - Utilizes GsonConverterFactory to convert JSON responses into Kotlin objects.
 *
 * Use the `getApiService()` method to obtain an instance of ApiService for your API calls.
 */

package com.siaptekno.dicodingevent.data.remote.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Define the ApiConfig class to hold the API configuration
class ApiConfig {
    // Companion object allows you to call the method without creating an instance of ApiConfig
    companion object {
        // Function to create and return an instance of ApiService
        fun getApiService(): ApiService {
            // Create a logging interceptor to log the details of the HTTP requests/responses
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            // Build the OkHttpClient with the logging interceptor
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor) // Add the logging interceptor to the client
                .build()

            // Build the Retrofit instance
            val retrofit = Retrofit.Builder()
                .baseUrl("https://event-api.dicoding.dev/") // Set the base URL for the API
                .addConverterFactory(GsonConverterFactory.create()) // Add Gson converter to handle JSON
                .client(client) // Set the OkHttpClient we built earlier
                .build()

            // Create and return the ApiService instance from Retrofit
            return retrofit.create(ApiService::class.java)
        }
    }
}