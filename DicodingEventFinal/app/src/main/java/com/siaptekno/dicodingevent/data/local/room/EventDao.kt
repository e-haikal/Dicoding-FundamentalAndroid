package com.siaptekno.dicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.siaptekno.dicodingevent.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    fun getEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM events WHERE bookmarked = 1")
    fun getBookmarkedEvents(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvents(events: List<EventEntity>)

    @Update
    fun updateEvents(event: EventEntity)

    @Query("DELETE FROM events WHERE bookmarked = 0")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM events WHERE id = :id AND bookmarked = 1)")
    fun isEventsBookmarked(id: Int): Boolean

}


