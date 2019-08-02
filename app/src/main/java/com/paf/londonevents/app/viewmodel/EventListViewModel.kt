package com.paf.londonevents.app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paf.londonevents.app.HasDependencies
import com.paf.londonevents.app.getDependency
import com.paf.londonevents.app.utils.CoroutineManager
import com.paf.londonevents.app.utils.ManagesCoroutines
import com.paf.londonevents.core.datasource.DataLoadingState
import com.paf.londonevents.core.datasource.DataLoadingState.*
import com.paf.londonevents.core.datasource.EventsRemoteDataSource
import com.paf.londonevents.core.model.Event
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class EventListViewModel: ViewModel(), HasDependencies, ManagesCoroutines {

    override var coroutineManager = CoroutineManager()
    val eventService: EventsRemoteDataSource by lazy { getDependency<EventsRemoteDataSource>() }
    val eventListLiveData = MutableLiveData<List<Event>>()
    val dataStateLiveData = MutableLiveData<DataLoadingState>()
    val messageLiveData = MutableLiveData<String>()
    private var loadSubscription: Disposable? = null

    fun loadEvents(){
        loadSubscription = eventService.loadEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onLoadList() }
            .subscribe(
                { result -> onLoadListSuccess(result) },
                { error -> onLoadListError(error) }
            )
    }

    fun saveToFavorites(event: Event) {
        event.isFavorite = true
        launch {
            eventService.saveEvent(event)
        }
    }

    fun removeFromFavorites(event: Event) {
        launch {
            eventService.unSaveEvent(event)
        }
    }

    fun searchEvents(eventTitle: String): Observable<List<Event>> {
        return getDependency<EventsRemoteDataSource>().searchEvents(eventTitle)
    }

    fun onLoadList() {
        dataStateLiveData.value = LOADING
    }

    fun onLoadListSuccess(eventList: List<Event>){
        dataStateLiveData.value = LOADED
        eventListLiveData.value = eventList
    }

    fun onLoadListError(error: Throwable){
        dataStateLiveData.value = FAILED
        messageLiveData.value = "Error fetching events"
    }

    override fun onCleared() {
        super.onCleared()
        loadSubscription?.dispose()
    }
}