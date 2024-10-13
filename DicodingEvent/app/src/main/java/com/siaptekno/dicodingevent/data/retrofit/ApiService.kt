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
    // Define a GET request to fetch event details using an event ID
    @GET("events/{id}") // Specify the endpoint with a path parameter
    fun getEvents(
        @Path("id") id: String, // The event ID to retrieve
    ): Call<Response> // Return a Call object for the API response
}