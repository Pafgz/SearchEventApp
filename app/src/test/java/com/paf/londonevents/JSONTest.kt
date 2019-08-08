package com.paf.londonevents

import com.paf.londonevents.data.common.JSON
import com.paf.londonevents.data.common.invoke
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*

@RunWith(RobolectricTestRunner::class)
class JSONTest {

    private val testJson = getJsonFromJsonFile("FakeEdgeTestJson")

    @Test
    fun testParseEventListFromJson(){
        val json: JSONArray = testJson("_embedded.events")!!
        val expectedResult = JsonModel(
            "Chivas Guadalajara v. Club America",
            "Joris",
                    listOf("photo1", "photo2", "photo3", "photo4"),
            "Soldier Field",
            "2019-09-08T22:30:00Z"
            )

        var actualResult : JsonModel? = null
        val eventJson = JSON(json[0] as JSONObject)
        actualResult = parseTestModel(eventJson)


        assertEquals(expectedResult, actualResult)
    }
}

private fun parseTestModel(json: JSON): JsonModel{

    val name: String? = json("name")
    val nickname: String = json("person.team.admin.user.nickname")!!
    val venueName: String = parseVenueName(json)!!
    val startDateTime: String = json("dates.start.dateTime")!!
    val photos: List<String> = parsePhotos(json)

    return JsonModel(name, nickname, photos, venueName, startDateTime)
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

private fun parsePhotos(json: JSON): List<String>{
    val photos: JSONArray = json("person.team.admin.user.photos")!!

    val photoList = ArrayList<String>()

    for (i in 0 until photos.length()) {
        val photo: String = photos[i].toString()
        photoList.add(photo)
    }

    return photoList
}

data class JsonModel(
    val name: String?,
    val nickname: String?,
    val photos: List<String>,
    val venueName: String,
    val dateTime: String
)
