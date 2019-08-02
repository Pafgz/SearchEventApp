package com.paf.londonevents

import android.net.Uri
import com.paf.londonevents.app.utils.DateUtils
import com.paf.londonevents.core.model.Event
import com.paf.londonevents.data.parsers.EventListParser
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EventParserTest {

    private val eventJson = getJsonFromJsonFile("EventJson")

    @Test
    fun testParseEventListFromJson(){
        val expectedList = ArrayList<Event>()
        val expectedEvent = Event(
            "Chivas Guadalajara v. Club America",
            "Soldier Field",
            Uri.parse("https://s1.ticketm.net/dam/a/10d/792ce874-3198-400b-9e69-f9ce8293a10d_1113381_RETINA_LANDSCAPE_16_9.jpg"),
            DateUtils.getDateFromString("2019-09-08T22:30:00Z"),
            false,
            "vvG18Z4m501KZN"
        )
        expectedList.add(expectedEvent)

        val actualList = EventListParser().parse(eventJson)

        assertEquals(expectedList, actualList)
    }
}