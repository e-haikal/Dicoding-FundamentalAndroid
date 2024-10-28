package com.siaptekno.dicodingevent.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.siaptekno.dicodingevent.data.local.entity.EventEntity
import com.siaptekno.dicodingevent.data.local.room.EventDao
import com.siaptekno.dicodingevent.data.remote.response.EventResponse
import com.siaptekno.dicodingevent.data.remote.retrofit.ApiService
import com.siaptekno.dicodingevent.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<Result<List<EventEntity>>>()

    fun getEvents(): LiveData<Result<List<EventEntity>>> {
        result.value = Result.Loading
        val client = apiService.getEvents(0)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val listEvents = response.body()?.listEvents
                    val eventList = ArrayList<EventEntity>()
                    appExecutors.diskIO.execute {
                        listEvents?.forEach { event ->
                            val isBookmarked = eventDao.isEventsBookmarked(event.id)
                            val events = EventEntity(
                                event.id,
                                event.name,
                                event.summary,
                                event.description,
                                event.imageLogo,
                                event.mediaCover,
                                event.category,
                                event.ownerName,
                                event.cityName,
                                event.quota,
                                event.registrants,
                                event.beginTime,
                                event.endTime,
                                event.link,
                                isBookmarked
                            )
                            eventList.add(events)
                        }
                        eventDao.deleteAll()
                        eventDao.insertEvents(eventList)
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })

        val localData = eventDao.getEvents()
        result.addSource(localData) { newData: List<EventEntity> ->
            result.value = Result.Success(newData)
        }
        return result
    }

    fun getBookmarkedEvents(): LiveData<List<EventEntity>> {
        return eventDao.getBookmarkedEvents()
    }

    fun setBookmarkedEvents(event: EventEntity, bookmarkState: Boolean) {
        appExecutors.diskIO.execute {
            event.isBookmarked = bookmarkState
            eventDao.updateEvents(event)
        }
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }
}
