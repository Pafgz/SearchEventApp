package com.paf.londonevents.core.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.paf.londonevents.core.model.Event
import io.reactivex.disposables.CompositeDisposable

class EventDataSourceFactory(val subscription: CompositeDisposable): DataSource.Factory<Int, Event>() {

    val dataSourceLiveData = MutableLiveData<EventPagedKeyDataSource>()

    override fun create(): DataSource<Int, Event> {
        val dataSource = EventPagedKeyDataSource(subscription)
        dataSourceLiveData.postValue(dataSource)
        return dataSource
    }
}