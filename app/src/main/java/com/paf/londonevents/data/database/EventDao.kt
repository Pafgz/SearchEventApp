package com.paf.londonevents.data.database

import androidx.room.*
import com.paf.londonevents.core.model.Event
import io.reactivex.Observable

@Dao
interface EventDao {
    @Query("SELECT * FROM event WHERE is_favorite = 1")
    fun getAllFavoriteEvents(): Observable<List<Event>>

    @Query("SELECT * FROM event WHERE id = :id AND is_favorite = 1")
    fun getFavoriteEventWithId(id: String): Observable<Event>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: Event)

    @Delete
    fun delete(event: Event)
}