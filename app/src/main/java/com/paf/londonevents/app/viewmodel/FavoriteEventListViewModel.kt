package com.paf.londonevents.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.paf.londonevents.app.HasDependencies
import com.paf.londonevents.app.getDependency
import com.paf.londonevents.app.utils.CoroutineManager
import com.paf.londonevents.app.utils.ManagesCoroutines
import com.paf.londonevents.core.datasource.DataLoadingState
import com.paf.londonevents.core.datasource.DataLoadingState.*
import com.paf.londonevents.core.datasource.EventsRemoteDataSource
import com.paf.londonevents.core.model.Event
import com.paf.londonevents.data.events.EventsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class FavoriteEventListViewModel: ViewModel(), HasDependencies, ManagesCoroutines {

    override var coroutineManager = CoroutineManager()
    val eventService: EventsRemoteDataSource by lazy { getDependency<EventsRemoteDataSource>() }

    val pageListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(5)
        .setPageSize(10)
        .setPrefetchDistance(0)
        .setEnablePlaceholders(true)
        .build()

    val eventListLiveData : LiveData<PagedList<Event>> = LivePagedListBuilder(
        EventsRepository.loadAllEvents(), pageListConfig)
        .build()

    val dataStateLiveData = MutableLiveData<DataLoadingState>()
    val messageLiveData = MutableLiveData<String>()
    private var loadSubscription: Disposable? = null

    fun loadFavoriteEvents(){
        loadSubscription = eventService.loadFavoriteEvents()
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

    private fun onLoadList() {
        dataStateLiveData.value = LOADING
    }

    private fun onLoadListSuccess(eventList: List<Event>){
        dataStateLiveData.value = LOADED
    }

    private fun onLoadListError(error: Throwable) {
        dataStateLiveData.value = FAILED
        messageLiveData.value = "Error fetching favorite events"
    }

    override fun onCleared() {
        super.onCleared()
        loadSubscription?.dispose()
    }
}