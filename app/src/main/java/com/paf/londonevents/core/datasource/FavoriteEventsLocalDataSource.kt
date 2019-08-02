package com.paf.londonevents.core.datasource

import com.paf.londonevents.core.model.Event
import io.reactivex.Observable

interface FavoriteEventsLocalDataSource {
    fun loadFavoriteEvents(): Observable<List<Event>>
}