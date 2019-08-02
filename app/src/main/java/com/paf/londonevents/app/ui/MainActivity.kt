package com.paf.londonevents.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import com.paf.londonevents.R
import com.paf.londonevents.app.ui.events.EventListFragment
import com.paf.londonevents.app.ui.events.FavoriteEventListFragment
import com.paf.londonevents.data.repository.EventsRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var pagerAdapter: PagerAdapter
    private val eventListFragment = EventListFragment()
    private val favoriteEventListFragment = FavoriteEventListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pagerAdapter = ViewPagerAdapter(supportFragmentManager, listOf(eventListFragment, favoriteEventListFragment))
        mainViewPager.adapter = pagerAdapter
        mainTabLayout.addTab(mainTabLayout.newTab().setText("Events"))
        mainTabLayout.addTab(mainTabLayout.newTab().setText("Favorites"))
    }
}
