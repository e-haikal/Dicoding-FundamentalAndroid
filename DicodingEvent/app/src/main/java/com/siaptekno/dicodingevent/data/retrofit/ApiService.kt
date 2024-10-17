package com.siaptekno.dicodingevent.data.retrofit

import com.siaptekno.dicodingevent.data.response.EventDetailResponse
import com.siaptekno.dicodingevent.data.response.EventResponse
import com.siaptekno.dicodingevent.data.response.ListEventsItem
import retrofit2.Call
import retrofit2.http.*

// Define the ApiService interface for API calls
interface ApiService {
    // Define a GET request to fetch a list of events
    @GET("events") // Specify the endpoint with a path parameter
    // Define a query parameter for filtering active events
    fun getEvents(
        // Define query parameters
        @Query("active") active: Int,
        @Query("limit") limit: Int = 40
    ): Call<EventResponse> // Return a Call object for the API response


    // New function to fetch event details by ID
    @GET("events/{id}")
    fun getEventById(
        @Path("id") eventId: Int
    ): Call<EventDetailResponse> // This assumes the response is a single event item
}