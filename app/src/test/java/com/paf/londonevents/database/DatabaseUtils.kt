package com.paf.londonevents.database

import android.net.Uri
import com.paf.londonevents.app.utils.CoroutineManager
import com.paf.londonevents.app.utils.DateUtils
import com.paf.londonevents.app.utils.ManagesCoroutines
import com.paf.londonevents.core.model.Event
import com.paf.londonevents.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DatabaseUtils: ManagesCoroutines {
    override var coroutineManager = CoroutineManager()

    fun fillDatabaseWithFakeData(dataBase: AppDatabase): ArrayList<Event> {

        val event1 = Event(
            "Chivas Guadalajara v. Club America",
            "Soldier Field",
            Uri.parse("https://s1.ticketm.net/dam/a/10d/792ce874-3198-400b-9e69-f9ce8293a10d_1113381_RETINA_LANDSCAPE_16_9.jpg"),
            DateUtils.getDateFromString("2019-09-08T22:30:00Z"),
            false,
            "vvG18Z4m501KZN"
        )
        val event2 = Event(
            "Chivas Guadalajara",
            "O2",
            Uri.parse("https://s1.ticketm.net/dam/a/10d/792ce874-3198-400b-9e69-f9ce8293a10d_1113381_RETINA_LANDSCAPE_16_9.jpg"),
            DateUtils.getDateFromString("2019-09-08T22:30:00Z"),
            false,
            "vvG1459m501KZN"
        )
        val event3 = Event(
            "ELO - Jeff Lynn",
            "O2",
            Uri.parse("https://s1.ticketm.net/dam/a/10d/792ce874-3198-400b-9e69-f9ce8293a10d_1113381_RETINA_LANDSCAPE_16_9.jpg"),
            DateUtils.getDateFromString("2019-09-08T22:30:00Z"),
            false,
            "vvG1459dfi1KZN"
        )

        val list = ArrayList<Event>()
        list.apply {
            add(event1)
            add(event2)
            add(event3)
        }

        launch {
            withContext(IO){
                insertEvent(dataBase, event1)
                insertEvent(dataBase, event2)
                insertEvent(dataBase, event3)
            }
        }

        return list
    }

    fun insertEvent(dataBase: AppDatabase, event: Event) {
        launch {
            withContext(IO){
                dataBase.eventDao().insertEvent(event)
            }
        }
    }

}