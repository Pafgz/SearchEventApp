package com.paf.londonevents.app.ui.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.paf.londonevents.R
import com.paf.londonevents.app.utils.WeakReference
import com.paf.londonevents.app.utils.getString
import com.paf.londonevents.app.utils.setImage
import com.paf.londonevents.core.model.Event
import kotlinx.android.synthetic.main.event_item_view.view.*

class EventPagedListAdapter: PagedListAdapter<Event, EventViewHolder>(EVENT_COMPARATOR), EventViewHolder.Listener {

    companion object {
        /**
         * This diff callback informs the PagedListAdapter how to compute list differences when new
         * PagedLists arrive.
         */
        private val EVENT_COMPARATOR = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean =
                oldItem == newItem
        }
    }


    interface Listener {
        fun addToFavorite(event: Event, favoriteIcon: ImageView)
        fun removeFromFavorite(event: Event, favoriteIcon: ImageView)
    }

    var delegate: Listener? by WeakReference()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item_view, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
            holder.delegate = this
        }
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty()){
            val item = getItem(position)

        }
        else{
            onBindViewHolder(holder, position)
        }
    }

    override fun onClickFavoriteIcon(event: Event, favoriteIcon: ImageView) {
        if (event.isFavorite){
            delegate?.removeFromFavorite(event, favoriteIcon)
        }
        else {
            delegate?.addToFavorite(event, favoriteIcon)
        }
    }
}


class EventViewHolder(view: View): RecyclerView.ViewHolder(view) {
    interface Listener {
        fun onClickFavoriteIcon(event: Event, favoriteIcon: ImageView)
    }

    var delegate: Listener? by WeakReference()

    fun bind(event: Event){
        itemView.apply {
            event.imageUri?.let { eventImageView.setImage(it) }
            eventNameView.text = event.name
            venueNameView.text = event.venueName
            dateView.text = event.startDateTime?.getString()

            if(event.isFavorite)  favoriteIcon.setImage(R.drawable.ic_full_favorite)
            else favoriteIcon.setImage(R.drawable.ic_empty_favorite)

            favoriteIcon.setOnClickListener { delegate?.onClickFavoriteIcon(event, favoriteIcon) }
        }
    }
}