package com.paf.londonevents.core.datasource

import com.paf.londonevents.core.model.Event
import com.paf.londonevents.data.events.EventResponse
import com.paf.londonevents.data.events.EventSearchQueryParams
import io.reactivex.Observable

interface EventsRemoteDataSource {
    fun loadFirstEvents(): Observable<EventResponse>
    fun searchEvents(queryParam: EventSearchQueryParams, pageNumber: Int = 0): Observable<EventResponse>
    fun loadEvent(event: Event): Observable<Event>
    fun loadFavoriteEvents(): Observable<List<Event>>
    suspend fun saveEvent(event: Event)
    suspend fun unSaveEvent(event: Event)
}