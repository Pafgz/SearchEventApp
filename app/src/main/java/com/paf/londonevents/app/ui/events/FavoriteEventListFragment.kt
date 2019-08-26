package com.paf.londonevents.app.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.paf.londonevents.R
import com.paf.londonevents.app.utils.setImage
import com.paf.londonevents.app.viewmodel.FavoriteEventListViewModel
import com.paf.londonevents.core.datasource.DataLoadingState
import com.paf.londonevents.core.datasource.DataLoadingState.*
import com.paf.londonevents.core.model.Event
import kotlinx.android.synthetic.main.list_layout.*

class FavoriteEventListFragment: Fragment(), EventPagedListAdapter.Listener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var eventAdapter: EventPagedListAdapter
    private val listViewModel: FavoriteEventListViewModel by lazy {
        ViewModelProviders.of(this).get(FavoriteEventListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.list_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainSearchView.visibility = GONE

        eventAdapter = EventPagedListAdapter()
        eventAdapter.delegate = this
        swipeRefreshView.setOnRefreshListener(this)

        //listViewModel.loadFavoriteEvents()
        observeViewModel()

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.adapter = eventAdapter
        recyclerView.layoutManager = layoutManager
    }

    private fun observeViewModel(){

        listViewModel.eventListLiveData.observe(this, Observer {
            render(it)
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

    private fun render(pagedNoteList: PagedList<Event>) {
        eventAdapter.submitList(pagedNoteList)
    }

    override fun addToFavorite(event: Event, favoriteIcon: ImageView) {
        favoriteIcon.setImage(R.drawable.ic_full_favorite)
        listViewModel.saveToFavorites(event)
    }

    override fun removeFromFavorite(event: Event, favoriteIcon: ImageView) {
        favoriteIcon.setImage(R.drawable.ic_empty_favorite)
        listViewModel.removeFromFavorites(event)
    }

    override fun onRefresh() {
       // listViewModel.loadFavoriteEvents()
    }
}