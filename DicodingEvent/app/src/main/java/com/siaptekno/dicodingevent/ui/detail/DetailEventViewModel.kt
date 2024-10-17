package com.siaptekno.dicodingevent.ui.detail

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.siaptekno.dicodingevent.data.response.Event
import com.siaptekno.dicodingevent.data.response.EventDetailResponse
import com.siaptekno.dicodingevent.data.response.EventResponse
import com.siaptekno.dicodingevent.data.response.ListEventsItem
import com.siaptekno.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventViewModel : ViewModel() {
    private val _detailEvents = MutableLiveData<Event>()
    val detailEvents: LiveData<Event> get() = _detailEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    fun fetchDetailEvent(eventId: Int) {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        apiService.getEventById(eventId).enqueue(object : Callback<EventDetailResponse> {
            override fun onResponse(
                call: Call<EventDetailResponse>,
                response: Response<EventDetailResponse>
            ) {
                if (response.isSuccessful) {
                    _detailEvents.value = response.body()?.event
                } else {
                    Log.e("DetailEventViewModel", "onFailure: ${response.message()}")
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}



