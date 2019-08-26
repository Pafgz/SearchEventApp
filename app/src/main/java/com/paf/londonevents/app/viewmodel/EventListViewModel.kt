package com.paf.londonevents.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.paf.londonevents.app.HasDependencies
import com.paf.londonevents.app.getDependency
import com.paf.londonevents.app.utils.CoroutineManager
import com.paf.londonevents.app.utils.ManagesCoroutines
import com.paf.londonevents.core.datasource.DataLoadingState
import com.paf.londonevents.core.datasource.DataLoadingState.*
import com.paf.londonevents.core.datasource.EventDataSourceFactory
import com.paf.londonevents.core.datasource.EventsRemoteDataSource
import com.paf.londonevents.core.model.Event
import com.paf.londonevents.data.events.EventResponse
import com.paf.londonevents.data.events.EventSearchQueryParams
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch

class EventListViewModel: ViewModel(), HasDependencies, ManagesCoroutines {

    override var coroutineManager = CoroutineManager()
    val eventService: EventsRemoteDataSource by lazy { getDependency<EventsRemoteDataSource>() }

    private var loadSubscription = CompositeDisposable()

    val pageListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(5)
        .setPageSize(10)
        .setPrefetchDistance(1)
        .build()

    val eventDataSourceFactory = EventDataSourceFactory(loadSubscription)

    val eventListLiveData : LiveData<PagedList<Event>> = LivePagedListBuilder(
        eventDataSourceFactory, pageListConfig)
        .build()

    val dataStateLiveData = MutableLiveData<DataLoadingState>()
    val messageLiveData = MutableLiveData<String>()

    fun saveToFavorites(event: Event) {
        event.isFavorite = true
        launch {
            eventService.saveEvent(event)
        }
    }

    fun removeFromFavorites(event: Event) {
        event.isFavorite = false
        launch {
            eventService.unSaveEvent(event)
        }
    }

    fun searchEvents(eventTitle: String): Observable<EventResponse> {
        return getDependency<EventsRemoteDataSource>().searchEvents(EventSearchQueryParams())
    }

    fun onLoadListSuccess(eventList: List<Event>){
        dataStateLiveData.value = LOADED
    }

    fun onLoadListError(error: Throwable){
        dataStateLiveData.value = FAILED
        messageLiveData.value = "Error fetching events"
    }

    fun refresh() {
        eventDataSourceFactory.dataSourceLiveData.value?.invalidate()
    }

    override fun onCleared() {
        super.onCleared()
        loadSubscription.dispose()
    }
}