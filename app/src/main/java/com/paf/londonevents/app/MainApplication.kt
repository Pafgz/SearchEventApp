package com.paf.londonevents.app

import android.app.Application
import androidx.room.Room
import com.paf.londonevents.core.datasource.EventsRemoteDataSource
import com.paf.londonevents.data.database.AppDatabase
import com.paf.londonevents.data.database.Database
import com.paf.londonevents.data.repository.EventsRepository
import com.paf.londonevents.domain.ApiProvider
import com.paf.londonevents.domain.service.EventsService

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Database.database = Room
            .databaseBuilder(applicationContext, AppDatabase::class.java, "event-database")
            //.fallbackToDestructiveMigration()
            .build()

        injectDependencies()
    }

    private fun injectDependencies() {
        DependencyInjection.apply {
            register<EventsRemoteDataSource> { EventsRepository }
            register<EventsService> { ApiProvider.getDiscoveryApi().create(EventsService::class.java) }
        }
    }
}