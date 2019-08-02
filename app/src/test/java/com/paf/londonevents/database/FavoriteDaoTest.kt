package com.paf.londonevents.database

import android.net.Uri
import androidx.room.Room
import com.paf.londonevents.app.utils.DateUtils
import com.paf.londonevents.core.model.Event
import com.paf.londonevents.data.database.AppDatabase
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FavoriteDaoTest {

    /*private lateinit var appDatabase: AppDatabase

    @Before
    fun setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            Implem,
            AppDatabase::class.java)
            .build()
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun testFetchFavoriteEvents(){
        val expectedList = DatabaseUtils.fillDatabaseWithFakeData(appDatabase)

        val actualEventList = appDatabase.eventDao().getAllFavoriteEvents()
        Assert.assertEquals(expectedList, actualEventList)
    }

    @Test
    fun testInsertEvent(){
        val event = Event(
            "Chivas Guadalajara v. Club America",
            "Soldier Field",
            Uri.parse("https://s1.ticketm.net/dam/a/10d/792ce874-3198-400b-9e69-f9ce8293a10d_1113381_RETINA_LANDSCAPE_16_9.jpg"),
            DateUtils.getDateFromString("2019-09-08T22:30:00Z"),
            false,
            "vvG18Z4m501KZN"
        )
        val expectedList = ArrayList<Event>()
        expectedList.add(event)

        DatabaseUtils.insertEvent(appDatabase, event)

        val actualEventList = appDatabase.eventDao().getAllFavoriteEvents()
        Assert.assertEquals(expectedList, actualEventList)
    }*/

}