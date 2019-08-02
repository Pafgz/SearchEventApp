package com.paf.londonevents.app.viewmodel.common

import androidx.lifecycle.MutableLiveData
import com.paf.londonevents.core.datasource.DataLoadingState
import com.paf.londonevents.core.model.Event

interface DataLoadable<T> {
    fun onLoadList()

    /*fun onLoadListSuccess(list: List<T>, liveDataList: MutableLiveData<List<T>>){
        dataStateLiveData.value = DataLoadingState.LOADED
        liveDataList.value = eventList
    }

    fun onLoadListError(){
        dataStateLiveData.value = DataLoadingState.FAILED
        messageLiveData.value = "Error fetching events"
    }*/
}