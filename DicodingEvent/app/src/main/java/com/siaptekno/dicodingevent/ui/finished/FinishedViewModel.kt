package com.siaptekno.dicodingevent.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.siaptekno.dicodingevent.data.response.EventResponse
import com.siaptekno.dicodingevent.data.response.ListEventsItem
import com.siaptekno.dicodingevent.data.retrofit.ApiConfig
import com.siaptekno.dicodingevent.ui.upcoming.UpcomingViewModel
import com.siaptekno.dicodingevent.ui.upcoming.UpcomingViewModel.Companion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {
    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "UpcomingViewModel"
    }


    init {
        fetchFinishedEvents()
    }

    fun fetchFinishedEvents() {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        apiService.getEvents(0, 40).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    _finishedEvents.value = response.body()?.listEvents
                    Log.d(TAG, "Fetched finished events: $finishedEvents")

                } else {
                    Log.e(FinishedViewModel.TAG, "onFailure: ${response.message()}")
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _finishedEvents.value = emptyList()
                _isLoading.value = false
            }
        })
    }
}