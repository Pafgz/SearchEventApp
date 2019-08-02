package com.paf.londonevents.data.repository

import com.paf.londonevents.app.HasDependencies
import com.paf.londonevents.app.MainApplication
import com.paf.londonevents.app.getDependency
import com.paf.londonevents.core.datasource.EventsRemoteDataSource
import com.paf.londonevents.core.model.Event
import com.paf.londonevents.data.common.JSON
import com.paf.londonevents.data.database.Database
import com.paf.londonevents.data.database.EventDao
import com.paf.londonevents.data.parsers.EventListParser
import com.paf.londonevents.domain.service.EventsService
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object EventsRepository: EventsRemoteDataSource, HasDependencies {

    private val service : EventsService by lazy { getDependency<EventsService>() }
    private val localEventService: EventDao by lazy { Database.database.eventDao() }

    override fun searchEvents(eventTitle: String?): Observable<List<Event>> {
        val events = getEventsWithParams(eventTitle)
        val favorites = loadFavoriteEvents()
        return Observables.zip(events, favorites) { eventList, favoriteList ->

            eventList.forEach { event ->
                event.isFavorite = favoriteList.any { event.id == it.id }
            }
            eventList
        }
    }


    private fun getEventsWithParams(eventTitle: String?): Observable<List<Event>> {
        return Observable.create { emitter ->

            val params =  HashMap<String, String>()
            params["city"] = "london"
            params["locale"] = "en-gb"
            params["size"] = "50"
            params["sort"] = "date,asc"
            eventTitle?.let { params["keyword"] = eventTitle }

            service.fetchEvents(params).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val json = JSON(response.body())
                    val apiList = EventListParser().parse(json)

                    emitter.onNext(apiList)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    emitter.onError(t)
                }
            })
        }
    }

    override fun loadEvents(): Observable<List<Event>> {
       return searchEvents()
    }

    override fun loadFavoriteEvents(): Observable<List<Event>> {
        return localEventService.getAllFavoriteEvents()
    }

    override fun loadEvent(event: Event): Observable<Event> {
        return localEventService.getFavoriteEventWithId(event.id)
    }

    override suspend fun saveEvent(event: Event) = withContext(IO){
               localEventService.insertEvent(event)
    }


    override suspend fun unSaveEvent(event: Event) = withContext(IO){
            localEventService.delete(event)
    }

}