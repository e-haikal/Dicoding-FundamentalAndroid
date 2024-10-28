package com.siaptekno.dicodingevent.data.di

import android.content.Context
import com.siaptekno.dicodingevent.data.EventRepository
import com.siaptekno.dicodingevent.data.local.room.EventDatabase
import com.siaptekno.dicodingevent.data.remote.retrofit.ApiConfig
import com.siaptekno.dicodingevent.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()
        return EventRepository.getInstance(apiService, dao, appExecutors)
    }
}