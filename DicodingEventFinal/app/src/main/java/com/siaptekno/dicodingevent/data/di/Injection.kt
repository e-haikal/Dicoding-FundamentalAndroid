package com.siaptekno.dicodingevent.data.di

import android.content.Context
import com.siaptekno.dicodingevent.data.EventRepository
import com.siaptekno.dicodingevent.data.local.room.FavoriteEventDatabase
import com.siaptekno.dicodingevent.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteEventDatabase.getDatabase(context)
        val dao = database.favoriteDao()
        return EventRepository.getInstance(apiService, dao)
    }
}