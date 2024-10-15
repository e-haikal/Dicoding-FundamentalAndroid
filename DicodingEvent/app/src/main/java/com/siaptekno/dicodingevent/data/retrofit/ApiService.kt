package com.siaptekno.dicodingevent.data.retrofit

import com.siaptekno.dicodingevent.data.response.EventResponse
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
    ): Call<EventResponse> // Return a Call object for the API response
}