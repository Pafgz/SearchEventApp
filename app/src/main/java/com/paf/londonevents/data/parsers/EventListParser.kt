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
        val content = json("_embedded") ?: json
        val events = content.jsonArrayOrNull("events") ?: json.jsonArrayOrNull("events")

        val eventList = ArrayList<Event>()

        if (events != null) {
            for (i in 0 until events.length()) {
                val eventJson = JSON(events[i] as JSONObject)
                val event = parseEvent(eventJson)
                eventList.add(event)
            }
        }

        return eventList
    }

    private fun parseEvent(json: JSON): Event{

        val eventName: String? = json("name")
        val venueName: String? = parseVenueName(json)
        val imageUri: Uri? = parseImage(json)
        val startDateTime: Date? = parseDate(json)
        val id: String = json("id") ?: getRandomString()

        return Event(eventName, venueName, imageUri, startDateTime, false, id)
    }

    private fun parseImage(json: JSON): Uri? {
        val images = json.jsonArrayOrNull("images")
        return if (images != null && images.length() > 0){
            val item = JSON(images[5].toString())
            val url: String? = item("url")
            Uri.parse(url)
        }
        else {
            null
        }
    }

    private fun parseVenueName(json: JSON): String?{
        val content: JSON? = json("_embedded")
        val venues: JSONArray? = content?.jsonArrayOrNull("venues")

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
        val dates: JSON? = json("dates")
        val startDate: JSON? = dates?.let { it("start") }
        val dateTime: String? = startDate?.let { it("dateTime") }

        return   dateTime?.let { DateUtils.getDateFromString(it) }
    }


}
