package com.paf.londonevents.data.events

import androidx.paging.DataSource.Factory
import com.paf.londonevents.app.HasDependencies
import com.paf.londonevents.app.getDependency
import com.paf.londonevents.app.utils.toJsonUsableString
import com.paf.londonevents.core.datasource.EventsRemoteDataSource
import com.paf.londonevents.core.model.Event
import com.paf.londonevents.data.common.JSON
import com.paf.londonevents.data.database.Database
import com.paf.londonevents.data.database.EventDao
import com.paf.londonevents.data.parsers.EventListParser
import com.paf.londonevents.data.parsers.EventNextPageParser
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

    override fun searchEvents(queryParam: EventSearchQueryParams, pageNumber: Int): Observable<EventResponse> {
        val events = getEventsWithParams(queryParam, pageNumber)
        val favorites = loadFavoriteEvents()
        return Observables.zip(events, favorites) { eventListResponse, favoriteList ->

            eventListResponse.eventList.forEach { event ->
                event.isFavorite = favoriteList.any { event.id == it.id }
            }
            EventResponse(eventListResponse.eventList, eventListResponse.nextPageNbr)
        }
    }


    private fun getEventsWithParams(queryParams: EventSearchQueryParams, pageNumber: Int): Observable<EventResponse> {
        return Observable.create { emitter ->

            val params =  HashMap<String, String>()
            params["city"] = queryParams.city
            params["locale"] = queryParams.local
            params["size"] = queryParams.pageSize.toString()
            params["sort"] = queryParams.sort
            params["page"] = pageNumber.toString()
            params["startDateTime"] = queryParams.startDateTime.toJsonUsableString()
            queryParams.eventTitle?.let { params["keyword"] = it }

            service.fetchEvents(params).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val json = JSON(response.body())
                    val apiList = EventListParser().parse(json)

                    val nextPageNbr = EventNextPageParser().parse(json)

                    emitter.onNext(EventResponse(apiList, nextPageNbr))
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    emitter.onError(t)
                }
            })
        }
    }

    override fun loadFirstEvents(): Observable<EventResponse> {
       return searchEvents(EventSearchQueryParams())
    }

    override fun loadFavoriteEvents(): Observable<List<Event>> {
        return localEventService.getAllFavoriteEvents()
    }

    fun loadAllEvents(): Factory<Int, Event> {
        return localEventService.getAllEvents()
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