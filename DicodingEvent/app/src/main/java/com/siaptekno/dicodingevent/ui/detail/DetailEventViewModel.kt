package com.siaptekno.dicodingevent.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.siaptekno.dicodingevent.data.response.EventResponse
import com.siaptekno.dicodingevent.data.response.ListEventsItem
import com.siaptekno.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventViewModel : ViewModel() {

    // MutableLiveData to hold the event details
    private val _eventDetail = MutableLiveData<ListEventsItem>()
    val eventDetail: LiveData<ListEventsItem> get() = _eventDetail

    // MutableLiveData to handle loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Function to fetch event details from the API
    fun fetchEventDetails(eventId: Int) {
        _isLoading.value = true // Start loading
        Log.d("DetailEventViewModel", "Fetching details for EVENT_ID: $eventId")
        val apiService = ApiConfig.getApiService()

        // Fetch all events first to find the event by ID
        apiService.getEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                if (response.isSuccessful) {
                    // Find the event with the given ID
                    response.body()?.listEvents?.find { it.id == eventId }?.let { event ->
                        _eventDetail.value = event // Store the event details in LiveData
                        Log.d("DetailEventViewModel", "Event details found for ID: $eventId")
                    }
                } else {
                    // Handle error response if needed
                    Log.e("DetailEventViewModel", "Event not found for ID: $eventId")

                }
                _isLoading.value = false // Stop loading
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false // Stop loading on failure
                // Handle failure case, log or show error message
            }
        })
    }
}
