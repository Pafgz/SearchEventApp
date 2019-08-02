package com.paf.londonevents.data.database

import androidx.room.*
import com.paf.londonevents.core.model.Event
import io.reactivex.Observable

@Dao
interface EventDao {
    @Query("SELECT * FROM event")
    fun getAllFavoriteEvents(): Observable<List<Event>>

    @Query("SELECT * FROM event WHERE id = :id AND is_favorite = 1")
    fun getFavoriteEventWithId(id: String): Observable<Event>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Insert
    fun insertAnEvent(event: Event)

    @Delete
    fun delete(event: Event)
}