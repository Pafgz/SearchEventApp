package com.paf.londonevents

import com.paf.londonevents.data.common.JSON
import com.paf.londonevents.data.common.compareTo
import com.paf.londonevents.data.common.get
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

        val eventJson = JSON(json[0] as JSONObject)
        val actualResult = parseTestModel(eventJson)

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun testMapJSONListFromJson(){
        val json: JSONArray = testJson("_embedded.events")!!
        val expectedResult = JsonModel(
            "Chivas Guadalajara v. Club America",
            "Joris",
                    listOf("photo1", "photo2", "photo3", "photo4"),
            "Soldier Field",
            "2019-09-08T22:30:00Z"
            )

        val eventJson = JSON(json[0] as JSONObject)
        val actualResult = parseTestModel(eventJson)

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


private fun parseDefaultJsonModel(json: JSON): DefaultJsonModel{

    val model = DefaultJsonModel()
    model::name < json["name"]
    model::nickname < json["person.team.admin.user.nickname"]
    model.venueName = parseVenueName(json)!!
    model::dateTime < json["dates.start.dateTime"]
    model.photos = parsePhotos(json)

    return model
}

class DefaultJsonModel(
    var name: String = "Patron",
    var nickname: String = "Enrico",
    var photos: List<String>? = null,
    var venueName: String = "Stade de France",
    var dateTime: String = "The current date"
)
