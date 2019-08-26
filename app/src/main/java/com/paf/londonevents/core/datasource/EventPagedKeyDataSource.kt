package com.paf.londonevents.core.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.paf.londonevents.app.HasDependencies
import com.paf.londonevents.app.getDependency
import com.paf.londonevents.core.model.Event
import com.paf.londonevents.data.events.EventSearchQueryParams
import com.paf.londonevents.data.events.EventsRepository
import io.reactivex.disposables.CompositeDisposable

class EventPagedKeyDataSource(val subscription: CompositeDisposable): PageKeyedDataSource<Int, Event>(), HasDependencies {

    val eventService: EventsRemoteDataSource by lazy { getDependency<EventsRemoteDataSource>() }
    val messageLiveData = MutableLiveData<String>()
    val dataStateLiveData = MutableLiveData<DataLoadingState>()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Event>) {
        Log.d("DATASOURCE LOADING", "INITIAL LOADING")
        onLoadList()
        subscription.add(EventsRepository.loadFirstEvents()
            .subscribe(
                { result ->
                    callback.onResult(result.eventList, result.nextPageNbr-1, result.nextPageNbr)
                    onLoadListSuccess()
                },
                { error -> onLoadListError(error)
                    Log.e("ERROR LOADING", error.message)
                }
            )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Event>) {
        Log.d("DATASOURCE LOADING", "LOADING PAGE " + params.key)
        onLoadList()
        subscription.add(eventService.searchEvents(EventSearchQueryParams(), params.key)
            .subscribe(
                { result ->
                    callback.onResult(result.eventList, result.nextPageNbr)
                    onLoadListSuccess()
                },
                { error -> onLoadListError(error)
                    Log.e("ERROR LOADING", error.message)
                }
            ))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Event>) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun onLoadList() {
        dataStateLiveData.postValue(DataLoadingState.LOADING)
    }

    fun onLoadListSuccess(){
        dataStateLiveData.postValue(DataLoadingState.LOADED)
        Log.d("DATASOURCE LOADING", "PAGE LOADED")
    }

    fun onLoadListError(error: Throwable){
        dataStateLiveData.postValue(DataLoadingState.FAILED)
        messageLiveData.postValue("Error fetching events")
    }
}