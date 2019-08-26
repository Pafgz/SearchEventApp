package com.paf.londonevents.data.parsers

import android.net.Uri
import com.paf.londonevents.app.utils.DateUtils
import com.paf.londonevents.core.model.Event
import com.paf.londonevents.data.common.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class EventListParser: JSONParser<List<Event>> {

    override fun parse(json: JSON): List<Event> {
      
        val events: JSONArray = json("_embedded.events")!!
        val eventList = ArrayList<Event>()

        for (i in 0 until events.length()) {
            val eventJson = JSON(events[i] as JSONObject)
            val event = parseEvent(eventJson)
            eventList.add(event)
        }

        return eventList
    }

    private fun parseEvent(json: JSON): Event{

        val eventName: String? = json("name")
        val venueName: String? = parseVenueName(json)
        val imageUri: Uri? = ImageParser().parse(json)
        val startDateTime: Date? = parseDate(json)
        val id: String = json("id") ?: getRandomString()

        return Event(eventName, venueName, imageUri, startDateTime, false, id)
    }

    private fun parseVenueName(json: JSON): String?{
        val venues: JSONArray? = json("_embedded.venues")

        return if(venues != null && venues.length() > 0){
            val venue: JSON? = JSON(venues[0] as JSONObject)

            if(venue != null) venue<String>("name")
            else null
        }
        else {
            null
        }
    }

    private fun parseDate(json: JSON): Date? {
        val dateTime: String? = json("dates.start.dateTime")

        return   dateTime?.let { DateUtils.getDateFromString(it) }
    }

}
