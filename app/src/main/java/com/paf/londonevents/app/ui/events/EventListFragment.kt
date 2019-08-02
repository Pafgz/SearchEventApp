package com.paf.londonevents.app.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.paf.londonevents.R
import com.paf.londonevents.app.ui.common.SearchObservable
import com.paf.londonevents.app.utils.setImage
import com.paf.londonevents.app.viewmodel.EventListViewModel
import com.paf.londonevents.core.datasource.DataLoadingState
import com.paf.londonevents.core.datasource.DataLoadingState.*
import com.paf.londonevents.core.model.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.list_layout.*
import java.util.concurrent.TimeUnit


class EventListFragment: Fragment(), SwipeRefreshLayout.OnRefreshListener, EventAdapter.Listener {

    private lateinit var eventAdapter: EventAdapter
    private var searchSubscription: Disposable? = null

    private val listViewModel: EventListViewModel by lazy {
        ViewModelProviders.of(this).get(EventListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventAdapter = EventAdapter()
        eventAdapter.delegate = this
        swipeRefreshView.setOnRefreshListener(this)

        listViewModel.loadEvents()
        observeViewModel()

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.adapter = eventAdapter
        recyclerView.layoutManager = layoutManager

        initObservableSearchView()
    }

    private fun observeViewModel(){

        listViewModel.eventListLiveData.observe(this, Observer {
            eventAdapter.setItems(it)
        })

        listViewModel.dataStateLiveData.observe(this, Observer {
            onDataLoadingStateChanged(it)
        })

        listViewModel.messageLiveData.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun onDataLoadingStateChanged(dataLoadingState: DataLoadingState){
        when(dataLoadingState){
            LOADING -> swipeRefreshView.isRefreshing = true
            LOADED, FAILED, DEFAULT -> swipeRefreshView.isRefreshing = false
        }
    }

    override fun addToFavorite(event: Event, favoriteIcon: ImageView) {
        favoriteIcon.setImage(R.drawable.ic_full_favorite)
        listViewModel.saveToFavorites(event)
    }

    override fun removeFromFavorite(event: Event, favoriteIcon: ImageView) {
        favoriteIcon.setImage(R.drawable.ic_empty_favorite)
        listViewModel.removeFromFavorites(event)
    }

    private fun initObservableSearchView(){
        searchSubscription = SearchObservable.fromView(mainSearchView)
            .debounce(300, TimeUnit.MILLISECONDS)
            .filter { text -> text.isNotEmpty() }
            .distinctUntilChanged()
            .switchMap{ query -> listViewModel.searchEvents(query) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    listViewModel.onLoadListSuccess(result)
                    recyclerView.smoothScrollToPosition(0)
                },
                { error -> listViewModel.onLoadListError(error) }
            )
    }

    override fun onRefresh() {
        listViewModel.loadEvents()
    }

    override fun onDestroy() {
        searchSubscription?.dispose()
        super.onDestroy()
    }
}