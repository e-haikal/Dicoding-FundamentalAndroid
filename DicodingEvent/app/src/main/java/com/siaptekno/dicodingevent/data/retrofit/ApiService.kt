/*
 * ApiService.kt
 *
 * This file defines the ApiService interface for making API calls using Retrofit.
 *
 * Key Components:
 * - Contains a method `getEvents()` to retrieve event details based on the provided event ID.
 * - Uses Retrofit annotations to define the HTTP method (GET) and path parameters.
 * - The method returns a Call object that encapsulates the HTTP response.
 *
 * Implement this interface in your networking code to interact with the API.
 */

package com.siaptekno.dicodingevent.data.retrofit

import com.siaptekno.dicodingevent.data.response.Response
import retrofit2.Call
import retrofit2.http.*

// Define the ApiService interface for API calls
interface ApiService {
    // Define a GET request to fetch a list of events
    @GET("events") // Specify the endpoint with a path parameter
    // Define a query parameter for filtering active events
    fun getEvents(
        // Define query parameters
        @Query("active") active: Int = 1,
        @Query("limit") limit: Int = 40
    ): Call<Response> // Return a Call object for the API response
}