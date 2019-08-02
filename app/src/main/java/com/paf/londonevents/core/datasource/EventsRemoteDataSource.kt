package com.paf.londonevents.core.datasource

import com.paf.londonevents.core.model.Event
import io.reactivex.Observable

interface EventsRemoteDataSource {
    fun loadEvents(): Observable<List<Event>>
    fun searchEvents(eventTitle: String? = null): Observable<List<Event>>
    fun loadEvent(event: Event): Observable<Event>
    fun loadFavoriteEvents(): Observable<List<Event>>
    suspend fun saveEvent(event: Event)
    suspend fun unSaveEvent(event: Event)
}